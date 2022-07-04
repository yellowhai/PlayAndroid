package com.hh.playandroid.ui.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hh.common.api.CommonApiService
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.launch
import com.hh.mine.ui.integral.PAGE_SIZE
import com.hh.playandroid.api.ApiServices
import com.hh.playandroid.bean.Tabs
import com.hh.playandroid.bean.ArticleBean
import com.hh.playandroid.ui.account.paging.AccountSource
import kotlinx.coroutines.flow.Flow

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/8  15:04
 */
class AccountViewModel : BaseViewModel() {
    var viewStates by mutableStateOf(AccountState())
        private set
    fun dispatch(action: AccountAction){
        when(action){
            is AccountAction.Collect -> collect(action.h,action.s)
            is AccountAction.GetList -> getList(action.s)
            AccountAction.GetTabList -> getTabList()
            is AccountAction.SetRefresh -> viewStates = viewStates.copy(isRefresh = action.s)
        }
    }

    private fun collect(h: ArticleBean, isCollect: Boolean) {
        launch({
            if (isCollect) {
                TaskApi.create(CommonApiService::class.java).unCollect(h.id)
            } else {
                TaskApi.create(CommonApiService::class.java).setCollect(h.id)
            }
        }, {

        })
    }

    private fun getTabList() {
        launch({
            TaskApi.create(ApiServices::class.java).getAccountTab()
        }, {
            it.data?.apply {
                forEachIndexed { index, accountTab ->
                    accountTab.pageId = index
                }
                viewStates = viewStates.copy(tabList = this)
            } ?: run {
                viewStates = viewStates.copy(isShowError = true)
            }
        })
    }

    fun getList(id : Int) : Flow<PagingData<ArticleBean>> {
        return Pager(
            PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { AccountSource(id) }).flow
    }
}

sealed class AccountAction {
    object GetTabList : AccountAction()
    data class GetList(val s : Int) : AccountAction()
    data class Collect(val h: ArticleBean, val s: Boolean) : AccountAction()
    data class SetRefresh(val s : Boolean) : AccountAction()
}
data class AccountState(
    val isRefresh: Boolean = false,
    val tabList: List<Tabs> = listOf(),
    val tabTextWidthList: MutableList<Dp> = arrayListOf(),
    val isShowError: Boolean = false,
)