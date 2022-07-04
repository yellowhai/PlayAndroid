package com.hh.mine.ui

import androidx.lifecycle.viewModelScope
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.mine.api.ApiService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/24  15:32
 */
class CpSettingViewModel : BaseViewModel() {
    fun logout(black : ()->Unit){
        viewModelScope.launch {
            flow {
                emit(TaskApi.create(ApiService::class.java).logout())
            }.onEach {
                black()
            }.collect()
        }
    }
}