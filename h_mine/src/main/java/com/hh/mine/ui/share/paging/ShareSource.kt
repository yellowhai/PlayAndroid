package com.hh.mine.ui.share.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hh.common.api.TaskApi
import com.hh.mine.api.ApiService
import com.hh.mine.bean.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/12 11:24
 */
class ShareSource : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            withContext(Dispatchers.IO){
                val page = params.key ?: 1 // set page 1 as default
//                val pageSize = params.loadSize
                val repoResponse = TaskApi.create(ApiService::class.java).getShareList(page)
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (!repoResponse.data?.shareArticles?.datas?.isEmpty()!!) page + 1 else null
                LoadResult.Page(repoResponse.data?.shareArticles?.datas?: listOf(), prevKey, nextKey)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}