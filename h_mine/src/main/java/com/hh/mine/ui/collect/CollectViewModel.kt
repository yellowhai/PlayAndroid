package com.hh.mine.ui.collect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.base.launch
import com.hh.common.network.ExceptionHandle
import com.hh.common.network.Resource
import com.hh.common.ext.showToast
import com.hh.mine.R
import com.hh.mine.api.ApiService
import com.hh.mine.bean.CollectInside
import com.hh.mine.bean.CollectUrl
import com.hh.mine.ui.collect.paging.CollectSource
import com.hh.mine.ui.integral.PAGE_SIZE
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/25  13:53
 */
class CollectViewModel : BaseViewModel() {


    var viewStates by mutableStateOf(CollectState())
        private set

    fun dispatch(action: CollectAction) {
        when (action) {
            CollectAction.GetInsideList -> getInsideList()
            CollectAction.GetUrlList -> getUrlList()
            is CollectAction.SetInsideRefresh -> viewStates = viewStates.copy(isInsideRefresh = action.s)
            is CollectAction.SetUrlRefresh -> viewStates = viewStates.copy(isUrlRefresh = action.s)
            is CollectAction.UnCollect -> unCollect(action.id)
            is CollectAction.UnUrl -> unUrl(action.id)
        }
    }

    private fun unCollect(id: Int) {
        launch({
            TaskApi.create(ApiService::class.java).unCollect(id)
        }, {
            getInsideList()
        }, {
            context.showToast("Unfavorites failed")
        })
    }

    private fun unUrl(id: Int) {
        launch({
            TaskApi.create(ApiService::class.java).unCollectUrl(id)
        }, {
            getUrlList()
        }, {
            context.showToast("Unfavorites failed")
        })
    }

    private fun getInsideList() {
        viewModelScope.launch {
            viewStates = viewStates.copy(insideList =  Pager(
                PagingConfig(pageSize = PAGE_SIZE),
                pagingSourceFactory = { CollectSource() }).flow)
        }
    }

    private fun getUrlList() {
        viewModelScope.launch {
            flow {
                emit(
                    TaskApi.create(ApiService::class.java).getCollectUrlData()
                )
            }.onStart {
                viewStates = viewStates.copy(urlList = Resource.Loading())
            }.onCompletion {
                viewStates = viewStates.copy(isUrlRefresh = false)
            }.map {
                if (it.errorCode == 0) {
                    it.data
                        ?: throw Exception("data null")
                } else {
                    throw Exception(it.errorMsg)
                }
            }.onEach {
                viewStates = viewStates.copy(urlList = Resource.Success(it))
            }.catch {
                viewStates = if (it.message == "data null") {
                    viewStates.copy(urlList = Resource.NoData())
                } else {
                    viewStates.copy(urlList = Resource.DataError(ExceptionHandle.handleException(it)))
                }
            }.collect()
        }
    }
}

sealed class CollectAction {
    object GetInsideList : CollectAction()
    object GetUrlList : CollectAction()
    data class SetInsideRefresh(val s: Boolean) : CollectAction()
    data class SetUrlRefresh(val s: Boolean) : CollectAction()
    data class UnCollect(val id: Int) : CollectAction()
    data class UnUrl(val id: Int) : CollectAction()
}

@OptIn(ExperimentalPagerApi::class)
data class CollectState(
    val insideList: Flow<PagingData<CollectInside>> = Pager(
        PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { CollectSource() }).flow,
    val urlList: Resource<List<CollectUrl>> = Resource.NoData(),
    val isInsideRefresh: Boolean = false,
    val isUrlRefresh: Boolean = false,
    val TabTextWidthList: MutableList<Dp> = arrayListOf()
)

val collectTabList = listOf(
    context.getString(R.string.instation),
    context.getString(R.string.outside)
)
