package com.hh.mine.ui.collect.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hh.common.api.TaskApi
import com.hh.mine.api.ApiService
import com.hh.mine.bean.CollectInside

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/25  14:03
 */
class CollectSource() : PagingSource<Int, CollectInside>() {
    override fun getRefreshKey(state: PagingState<Int, CollectInside>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectInside> {
        return try {
            val page = params.key ?: 0 // set page 1 as default
//                val pageSize = params.loadSize
            val repoResponse = TaskApi.create(ApiService::class.java).getCollectData(page)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (!repoResponse.data?.datas?.isEmpty()!!) page + 1 else null
            LoadResult.Page(repoResponse.data?.datas?: listOf(), prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}