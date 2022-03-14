package com.hh.common.ext

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.gson.Gson
import com.hh.common.R
import com.hh.common.base.YshhApplication.Companion.context
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/6  10:50
 */

@SuppressLint("UseCompatLoadingForDrawables")
suspend fun String.toBitmap(): Bitmap = // Disable hardware bitmaps.
    withContext(IO) {
        var bitmap: Bitmap =
            context.resources.getDrawable(R.mipmap.ic_default_round, null).toBitmap()
        kotlin.runCatching {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(this@toBitmap)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()
            val d = (loader.execute(request) as SuccessResult)
            (d.drawable as BitmapDrawable).bitmap
        }.onSuccess {
            bitmap = it
        }
        bitmap
    }

/**
 * 将对象转为JSON字符串
 */
fun Any?.toJson():String{
    return Gson().toJson(this)
}

