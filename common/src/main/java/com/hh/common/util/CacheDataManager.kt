package com.hh.common.util
import android.content.Context
import android.os.Environment
import com.hh.common.R
import java.io.File
import java.math.BigDecimal


object CacheDataManager {

    fun getTotalCacheSize(context: Context): String {
        context.run {
            var cacheSize = getFolderSize(cacheDir)
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                cacheSize += getFolderSize(externalCacheDir)
            }
            cacheSize += getFolderSize(getExternalFilesDir(Environment.DIRECTORY_PICTURES+stringResource(R.string.photo_path)))
//            cacheSize += getFolderSize(File(getStorageEmulated0Dirs() + "/" + Environment.DIRECTORY_PICTURES+ "/" +stringResource(R.string.photo_path)))
            return getFormatSize(cacheSize.toDouble())
        }
    }

    fun clearAllCache(context: Context) {
        context.let {
            deleteDir(it.cacheDir)
            it.getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/hhf")
                ?.let { it1 -> deleteDir(it1) }
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                if (it.externalCacheDir == null) {
                    it.showToast(it.stringResource(R.string.clear_cache_error))
                }
                return
            }
            it.externalCacheDir?.let { file ->
                if(deleteDir(file)){
                    it.showToast(it.stringResource(R.string.clearing_cache_succeeded))
                }
            }
        }
    }

}

private fun deleteDir(dir: File): Boolean {
    if (dir.isDirectory) {
        val children = dir.list()
        children?.run {
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
    }
    return dir.delete()
}

/**
 * 获取文件
 * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
 * 目录，一般放一些长时间保存的数据
 * Context.getExternalCacheDir() -->
 * SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
 */
fun getFolderSize(file: File?): Long {
    var size: Long = 0
    file?.run {
        try {
            val fileList = listFiles()
            fileList?.run {
                for (i in fileList.indices) {
                    // 如果下面还有文件
                    size += if (fileList[i].isDirectory) {
                        getFolderSize(fileList[i])
                    } else {
                        fileList[i].length()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return size
}

/**
 * 格式化单位
 */
fun getFormatSize(size: Double): String {

    val kiloByte = size / 1024
    if (kiloByte < 1) {
        return size.toString() + "KB"
    }

    val megaByte = kiloByte / 1024

    if (megaByte < 1) {
        val result1 = BigDecimal(kiloByte.toString())
        return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
    }

    val gigaByte = megaByte / 1024

    if (gigaByte < 1) {
        val result2 = BigDecimal(megaByte.toString())
        return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
    }

    val teraBytes = gigaByte / 1024

    if (teraBytes < 1) {
        val result3 = BigDecimal(gigaByte.toString())
        return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
    }

    val result4 = BigDecimal(teraBytes)
    return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
}