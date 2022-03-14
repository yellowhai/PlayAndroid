package com.hh.mine.ui.todo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hh.common.api.TaskApi
import com.hh.mine.api.ApiService
import com.hh.mine.bean.TodoBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/3  11:24
 */
class TodoSource : PagingSource<Int, TodoBean>() {
    override fun getRefreshKey(state: PagingState<Int, TodoBean>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TodoBean> {
        return try {
            withContext(Dispatchers.IO){
                val page = params.key ?: 1 // set page 1 as default
//                val pageSize = params.loadSize
                val repoResponse = TaskApi.create(ApiService::class.java).getTodoData(page)
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (!repoResponse.data?.isEmpty()!!) page + 1 else null
                LoadResult.Page(repoResponse.data?.datas?: listOf(), prevKey, nextKey)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}