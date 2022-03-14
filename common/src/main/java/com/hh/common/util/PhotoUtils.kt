package com.hh.common.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File
import java.io.IOException
import java.io.OutputStream

/**
 * @ProjectName: playandroid
 * @Package: com.hh.common.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/12/30  17:31
 */
object PhotoUtils {
    fun createFile(context: Context, pickerInitialUri: File, pathName: String) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, pickerInitialUri.name)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.TITLE, "Image.jpg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$pathName")
        }
        val external: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val resolver: ContentResolver = context.contentResolver
        val insertUri: Uri? = resolver.insert(external, values)
        var os: OutputStream? = null
        try {
            if (insertUri != null) {
                os = resolver.openOutputStream(insertUri)
            }
            if (os != null) {
                val bitmap = BitmapFactory.decodeFile(pickerInitialUri.toString())
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os)
            }
        } catch (e: IOException) {
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
            }
        }
    }

    fun createFile(context: Context, bitmap: Bitmap, pathName: String,displayName : String) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        values.put(MediaStore.Images.Media.TITLE, "Image.jpg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$pathName")
        }
        val external: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val resolver: ContentResolver = context.contentResolver
        val insertUri: Uri? = resolver.insert(external, values)
        var os: OutputStream? = null
        try {
            if (insertUri != null) {
                os = resolver.openOutputStream(insertUri)
            }
            if (os != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            }
        } catch (e: IOException) {
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
            }
        }
    }

    fun getPath2uri(context: Context, fileUri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, fileUri)) {
            if (isExternalStorageDocument(fileUri)) {
                val docId = DocumentsContract.getDocumentId(fileUri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return context.getExternalFilesDir(null).toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(fileUri)) {
                val id = DocumentsContract.getDocumentId(fileUri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(fileUri)) {
                val docId = DocumentsContract.getDocumentId(fileUri)
                val split = docId.split(":").toTypedArray()
                val contentUri: Uri = when (split[0]) {
                    "image" -> {
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    else -> {
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } // MediaStore (and general)
        else if ("content".equals(fileUri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(fileUri)) {
                fileUri.lastPathSegment
            } else getDataColumn(context, fileUri, null, null)
        } else if ("file".equals(fileUri.scheme, ignoreCase = true)) {
            return fileUri.path
        }
        return null
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }


    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(column))
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

}