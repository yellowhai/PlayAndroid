package com.hh.playandroid.ui.search.result.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hh.common.api.TaskApi
import com.hh.playandroid.api.ApiServices
import com.hh.playandroid.bean.ArticleBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/10  13:49
 */
class SearchResultSource(private val searchName : String) : PagingSource<Int,ArticleBean>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleBean>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleBean> {
        return try {
            withContext(Dispatchers.IO){
                val page = params.key ?: 0 // set page 1 as default
                val repoResponse = TaskApi.create(ApiServices::class.java).getSearchDataByKey(page,searchName)
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (!repoResponse.data?.isEmpty()!!) page + 1 else null
                LoadResult.Page(repoResponse.data?.datas?: listOf(), prevKey, nextKey)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}