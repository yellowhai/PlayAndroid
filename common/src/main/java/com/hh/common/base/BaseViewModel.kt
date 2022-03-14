package com.hh.common.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hh.common.network.BaseResponse
import com.hh.common.network.Resource
import com.hh.common.network.ApiResponse
import com.hh.common.network.AppException
import com.hh.common.network.ExceptionHandle
import com.hh.common.util.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * @author yshh
 * @date 2020/1/15
 */

abstract class BaseViewModel : ViewModel(), LifecycleObserver {
}

/**
 *  调用携程
 * @param block 操作耗时操作任务
 * @param success 成功回调
 * @param error 失败回调 可不给
 */
fun <T> BaseViewModel.launch(
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (Throwable) -> Unit = {}
) {
    viewModelScope.launch {
        runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            success(it)
        }.onFailure {
            error(it)
        }
    }
}

inline fun <reified T> request(
    crossinline block: suspend () -> BaseResponse<T>,
    netState: MutableStateFlow<Resource<T>>
) = flow {
    emit(block().apply {
        if(isSucces()){
            getResponseData()?.apply {
                netState.value = Resource.Success(this)
            }?:run {
                netState.value = Resource.NoData()
            }
        }
        else{
            netState.value = Resource.DataError(AppException(
                getResponseCode(),
                getResponseMsg()
            ))
        }
    })
}.onStart {
    emit(ApiResponse<T>().apply {
        netState.value = Resource.Loading()
    })
}.catch { t->
    T::class.java.simpleName.logE()
    emit(ApiResponse<T>().apply {
        netState.value = Resource.DataError(ExceptionHandle.handleException(t))
    })
}
