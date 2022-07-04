package com.hh.mine.ui.integral

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.hh.common.base.BaseViewModel
import com.hh.common.bean.IntegralHistory
import com.hh.common.bean.UserInfo
import com.hh.common.util.CacheUtils
import com.hh.mine.ui.integral.paging.IntegralSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/24  16:27
 */
const val PAGE_SIZE = 20

class IntegralViewModel : BaseViewModel() {
    var viewStates by mutableStateOf(IntegralState())
        private set

    fun dispatch(action: IntegralAction) {
        when (action) {
            is IntegralAction.SetShowError -> viewStates = viewStates.copy(isShowError = action.s)
            is IntegralAction.SetRefresh -> viewStates = viewStates.copy(isRefresh = action.s)
            IntegralAction.GetList -> refreshList()
        }
    }

    private fun refreshList() {
        viewModelScope.launch {
            viewStates = viewStates.copy(list =  Pager(
                PagingConfig(pageSize = PAGE_SIZE),
                pagingSourceFactory = { IntegralSource() }).flow)
        }
    }
}

sealed class IntegralAction {
    data class SetShowError(val s: Boolean) : IntegralAction()
    data class SetRefresh(val s: Boolean) : IntegralAction()
    object GetList : IntegralAction()
}

data class IntegralState(
    val integral: String = Gson().fromJson(
        CacheUtils.userInfo,
        UserInfo::class.java
    ).coinCount.toString(),
    val list: Flow<PagingData<IntegralHistory>> = Pager(
        PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { IntegralSource() }).flow,
    val isRefresh: Boolean = false,
    val isShowError: Boolean = false
)