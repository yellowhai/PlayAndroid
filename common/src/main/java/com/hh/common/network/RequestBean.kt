package com.hh.common.network

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/7  11:22
 */
abstract class BaseResponse<T> {

    //抽象方法，用户的基类继承该类时，需要重写该方法
    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T?

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

}


sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    class Loading<T> : Resource<T>()
    data class DataError<T>( val errorMsg: AppException? = null) : Resource<T>()
    class NoData<T> : Resource<T>()
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[exception= $errorMsg]"
            is Loading<T> -> "Loading"
            is NoData -> "NoData"
            else -> "error"
        }
    }
}