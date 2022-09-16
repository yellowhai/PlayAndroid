package com.hh.playandroid.ui.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.hh.common.api.CommonApiService
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.launch
import com.hh.mine.ui.integral.PAGE_SIZE
import com.hh.playandroid.api.ApiServices
import com.hh.playandroid.bean.BannerResponse
import com.hh.playandroid.bean.ArticleBean
import com.hh.playandroid.ui.home.paging.HomeSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/7  10:10
 */
class HomeViewModel : BaseViewModel() {
    var viewStates by mutableStateOf(HomeState(homeList = Pager(
        PagingConfig(PAGE_SIZE),
        pagingSourceFactory = { HomeSource() }).flow.cachedIn(viewModelScope)))
        private set

    fun dispatch(action: HomeAction) {
        when (action) {
            HomeAction.GetBanner -> getBanner()
            is HomeAction.Collect -> collect(action.h,action.s)
        }
    }
    private fun collect(h :Int, isCollect : Boolean){
        launch({
            if(isCollect){
                TaskApi.create(CommonApiService::class.java).unCollect(h)
            }
            else{
                TaskApi.create(CommonApiService::class.java).setCollect(h)
            }
        },{

        })
        viewModelScope
    }

    private fun getBanner() {
        viewModelScope.launch {
            flow {
                emit(TaskApi.create(ApiServices::class.java).getBanner())
            }.map {
                it.data?.apply {
                    this.forEachIndexed { i, b ->
                        b.id = i
                    }
                }
            }.onEach {
                it?.apply {
                    viewStates = viewStates.copy(bannerList = this)
                }
            }.catch {

            }.collect()
        }
    }
}

sealed class HomeAction {
    object GetBanner : HomeAction()
    data class Collect (val h :Int, val s:Boolean): HomeAction()
}

data class HomeState(
    val isRefresh : Boolean = false,
    val bannerList: List<BannerResponse> = listOf(),
    val homeList: Flow<PagingData<ArticleBean>> = Pager(
        PagingConfig(PAGE_SIZE),
        pagingSourceFactory = { HomeSource() }).flow,
)