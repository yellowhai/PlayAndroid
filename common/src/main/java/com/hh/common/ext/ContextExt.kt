package com.hh.common.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.widget.Toast
import androidx.annotation.StringRes
import com.hh.common.R
import com.hh.common.base.YshhApplication
import com.vanpra.composematerialdialogs.MaterialDialog

/**
 * @ProjectName: playandroid
 * @Package: com.hh.common.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/12/30  17:04
 */
fun Context.showToast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/**
 * 获取外部存储根路径 Gets the external storage root path
 */
fun Context.getStorageEmulated0Dirs() : String{
    var externalFileRootDir = getExternalFilesDir(null)
    do {
        externalFileRootDir = externalFileRootDir!!.parentFile
    } while (externalFileRootDir!!.absolutePath.contains("/Android"))
    return  externalFileRootDir.absolutePath
}


/**
 * 获取屏幕宽度
 */
val screenWidth
    get() = Resources.getSystem().displayMetrics.widthPixels

/**
 * 获取屏幕高度
 */
val screenHeight
    get() = Resources.getSystem().displayMetrics.heightPixels

/**
 * 获取屏幕像素：对获取的宽高进行拼接。例：1080X2340。
 */
fun screenPixel(): String {
    YshhApplication.context.resources.displayMetrics.run {
        return "${widthPixels}X${heightPixels}"
    }
}

fun Context.stringResource(@StringRes rid:Int) : String{
    return resources.getString(rid)
}

