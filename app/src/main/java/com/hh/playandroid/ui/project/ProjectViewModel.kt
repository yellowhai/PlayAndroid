package com.hh.playandroid.ui.project

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
import com.hh.playandroid.ui.project.paging.ProjectSource
import kotlinx.coroutines.flow.Flow

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/8  15:04
 */
class ProjectViewModel : BaseViewModel() {
    var viewStates by mutableStateOf(ProjectState())
        private set
    fun dispatch(action: ProjectAction){
        when(action){
            is ProjectAction.Collect -> collect(action.h,action.s)
            is ProjectAction.GetList -> getList(action.s)
            ProjectAction.GetTabList -> getTabList()
            is ProjectAction.SetRefresh -> viewStates = viewStates.copy(isRefresh = action.s)
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
            TaskApi.create(ApiServices::class.java).getProjecTitle()
        }, {
            it.data?.apply {
                add(0,Tabs(name = "最新项目",pageId = 0))
                forEachIndexed { index, ProjectTab ->
                    ProjectTab.pageId = index
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
            pagingSourceFactory = { ProjectSource(id) }).flow
    }
}

sealed class ProjectAction {
    object GetTabList : ProjectAction()
    data class GetList(val s : Int) : ProjectAction()
    data class Collect(val h: ArticleBean, val s: Boolean) : ProjectAction()
    data class SetRefresh(val s : Boolean) : ProjectAction()
}
data class ProjectState(
    val isRefresh: Boolean = false,
    val tabList: List<Tabs> = listOf(),
    val tabTextWidthList: MutableList<Dp> = arrayListOf(),
    val isShowError: Boolean = false,
)