package com.hh.common.network

import com.hh.common.R
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.util.stringResource

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/7  11:34
 */
class AppException : Exception {

    var errorMsg: String //错误消息
    var errCode: Int = -100 //错误码
    var errorLog: String? //错误日志

    constructor(errCode: Int, error: String?, errorLog: String? = "") : super(error) {
        this.errorMsg = error ?: context.stringResource(R.string.request_failed_try)
        this.errCode = errCode
        this.errorLog = errorLog?:this.errorMsg
    }

    constructor(error: Error,e: Throwable?) {
        errCode = error.getKey()
        errorMsg = error.getValue()
        errorLog = e?.message
    }
}