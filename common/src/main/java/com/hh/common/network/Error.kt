package com.hh.common.network

import android.net.ParseException
import android.util.MalformedJsonException
import coil.network.HttpException
import com.hh.common.R
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.util.stringResource
import org.json.JSONException
import java.net.ConnectException

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　: 错误枚举类
 */
enum class Error(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, context.stringResource(R.string.request_failed_try)),
    /**
     * 解析错误
     */
    PARSE_ERROR(1001, context.stringResource(R.string.analysis_error_try)),
    /**
     * 网络错误
     */
    NETWORK_ERROR(1002, context.stringResource(R.string.network_error_try)),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, context.stringResource(R.string.ssl_error_try)),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, context.stringResource(R.string.network_timeout_error_try));

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}

object ExceptionHandle {

    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = AppException(Error.NETWORK_ERROR,e)
                    return ex
                }
                is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = AppException(Error.PARSE_ERROR,e)
                    return ex
                }
                is ConnectException -> {
                    ex = AppException(Error.NETWORK_ERROR,e)
                    return ex
                }
                is javax.net.ssl.SSLException -> {
                    ex = AppException(Error.SSL_ERROR,e)
                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is AppException -> return it

                else -> {
                    ex = AppException(Error.UNKNOWN,e)
                    return ex
                }
            }
        }
        ex = AppException(Error.UNKNOWN,e)
        return ex
    }
}