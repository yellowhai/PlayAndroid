package com.hh.common.network

data class ApiResponse<T>(val errorCode: Int = -100,val errorMsg: String = "", val data: T? = null) : BaseResponse<T>() {

    override fun isSucces() = errorCode == 200

    override fun getResponseCode() = errorCode

    override fun getResponseData() = data

    override fun getResponseMsg() = errorMsg

}