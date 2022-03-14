package com.hh.common.util

import android.content.pm.PackageManager

import android.graphics.Bitmap
import android.graphics.Canvas

import android.graphics.drawable.LayerDrawable

import android.graphics.drawable.Drawable

import android.graphics.drawable.AdaptiveIconDrawable

import android.graphics.drawable.BitmapDrawable

import android.os.Build

import androidx.annotation.RequiresApi
import com.hh.common.base.YshhApplication.Companion.context
import java.lang.Exception


/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/6  10:22
 */
object AppIconHelper {
    /**
     * 获取当前应用程序的图标。
     */
    fun getAppIcon(): Bitmap? {
        if (Build.VERSION.SDK_INT >= 26) {
            return getAppIcon26()
        }
        try {
            val drawable = context.packageManager.getApplicationIcon(context.packageName!!)
            return (drawable as BitmapDrawable).bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getAppIcon26(): Bitmap? {
        try {
            val drawable = context.packageManager.getApplicationIcon(context.packageName!!)
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            } else if (drawable is AdaptiveIconDrawable) {
                val backgroundDr = drawable.background
                val foregroundDr = drawable.foreground
                val drr = arrayOfNulls<Drawable>(2)
                drr[0] = backgroundDr
                drr[1] = foregroundDr
                val layerDrawable = LayerDrawable(drr)
                val width = layerDrawable.intrinsicWidth
                val height = layerDrawable.intrinsicHeight
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
                layerDrawable.draw(canvas)
                return bitmap
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}