package com.hh.common.util

import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import com.hh.common.BuildConfig
import com.hh.common.base.YshhApplication
import com.hh.common.util.HhfLog.d
import com.hh.common.util.HhfLog.e
import com.hh.common.util.HhfLog.i
import com.hh.common.util.HhfLog.v
import com.hh.common.util.HhfLog.w
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Android开发调试日志工具类
 * 需要一些权限: <br></br>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission> <br></br>
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"></uses-permission><br></br>
 * @author huanghai
 */
const val TAG = "HHLog"
fun String.logE(tag: String = TAG) =
    e(tag, this)
fun String.logV(tag: String = TAG) =
    v(tag, this)
fun String.logD(tag: String = TAG) =
    d(tag, this)
fun String.logI(tag: String = TAG) =
    i(tag, this)
fun String.logW(tag: String = TAG) =
    w(tag, this)


@SuppressLint("SimpleDateFormat")
object HhfLog {

    private val ISRELESE = BuildConfig.DEBUG
    private var isDebugModel = ISRELESE// 是否输出日志
    private var isSaveDebugInfo = ISRELESE// 是否保存调试日志
    private var isSaveCrashInfo = ISRELESE// 是否保存报错日志
    /**
     * 获取日志文件路径
     *
     * @return
     */
    private val file: String
        get() {
            val sdDir: File? = YshhApplication.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val cacheDir = File(sdDir.toString() + "/log")
            if (!cacheDir.exists())
                cacheDir.mkdir()

            val filePath = File(cacheDir.toString() + "/" + date() + ".txt")

            return filePath.toString()
        }
    fun v(tag: String, msg: String) {
        if (isDebugModel) {
            Log.v(tag, "--> $msg")
        }
    }

    fun d(tag: String, msg: String) {
        if (isDebugModel) {
            Log.d(tag, "--> $msg")
        }
    }

    fun i(tag: String, msg: String) {
        if (isDebugModel) {
            Log.i(tag, "--> $msg")
        }
    }

    fun w(tag: String, msg: String) {
        if (isDebugModel) {
            Log.w(tag, "--> $msg")
        }
    }

    /**
     * 调试日志，便于开发跟踪。
     *
     * @param tag
     * @param msg
     */
    fun  e(tag: String, msg: String) {
        try {
            if (isDebugModel) {
                Log.e(tag, "--> $msg")
            }

            if (isSaveDebugInfo) {
                object : Thread() {
                    override fun run() {
                        write(time() + tag + " --> " + msg + "\n")
                    }
                }.start()
            }
        } catch (e: Exception) {
        }

    }

    /**
     * try catch 时使用，上线产品可上传反馈。
     *
     * @param tag
     * @param tr
     */
    fun e(tag: String, tr: Throwable) {
        if (isSaveCrashInfo) {
            object : Thread() {
                override fun run() {
                    write(
                        time() + tag + " [CRASH] --> "
                            + getStackTraceString(tr) + "\n")
                }
            }.start()
        }
    }

    /**
     * 获取捕捉到的异常的字符串
     *
     * @param tr
     * @return
     */
    fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }

        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t = t.cause
        }

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        return sw.toString()
    }

    /**
     * 标识每条日志产生的时间
     *
     * @return
     */
    private fun time(): String {
        return ("["
                + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(
                System.currentTimeMillis())) + "] ")
    }

    /**
     * 以年月日作为日志文件名称
     *
     * @return
     */
    private fun date(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date(System
                .currentTimeMillis()))
    }

    /**
     * 保存到日志文件
     *
     * @param content
     */
    @Synchronized
    fun write(content: String) {
        try {
            val writer = FileWriter(file, true)
            writer.write(content)
            writer.close()
        } catch (e: IOException) {
            //e.printStackTrace();
        }

    }

}