package com.hh.playandroid.ui.search.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hh.common.api.CommonApiService
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.launch
import com.hh.mine.ui.integral.PAGE_SIZE
import com.hh.playandroid.bean.ArticleBean
import com.hh.playandroid.ui.search.result.paging.SearchResultSource
import kotlinx.coroutines.flow.Flow

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/10  13:42
 */
class SearchResultViewModel : BaseViewModel() {
    var viewStates by mutableStateOf(SearchResultState())
        private set

    fun dispatch(action: SearchResultAction){
        when(action){
            is SearchResultAction.ChangeShowBtn -> viewStates = viewStates.copy(isShowBtn = action.s)
            is SearchResultAction.Collect -> collect(action.h,action.s)
            is SearchResultAction.GetSearchList -> getList(action.s)
            is SearchResultAction.SetRefresh -> viewStates = viewStates.copy(isRefresh = action.s)
        }
    }

    private fun getList(s : String){
       viewStates = viewStates.copy(searchList = Pager(
           PagingConfig(PAGE_SIZE),
           pagingSourceFactory = { SearchResultSource(s) }).flow)
    }

    private fun collect(h :ArticleBean, isCollect : Boolean){
        launch({
            if(isCollect){
                TaskApi.create(CommonApiService::class.java).unCollect(h.id)
            }
            else{
                TaskApi.create(CommonApiService::class.java).setCollect(h.id)
            }
        },{})
    }
}

sealed class SearchResultAction {
    data class Collect (val h : ArticleBean, val s:Boolean): SearchResultAction()
    data class ChangeShowBtn(val s : Boolean) : SearchResultAction()
    data class GetSearchList(val s : String) : SearchResultAction()
    data class SetRefresh(val s : Boolean) : SearchResultAction()
}

data class SearchResultState(
    val isRefresh : Boolean = false,
    val isShowBtn : Boolean = false,
    val searchList: Flow<PagingData<ArticleBean>>? = null
)