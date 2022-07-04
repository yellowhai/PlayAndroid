package com.hh.mine.ui.integral.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hh.common.api.TaskApi
import com.hh.common.bean.IntegralHistory
import com.hh.mine.api.ApiService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @ProjectName: CBook
 * @Package: com.hh.cbook.ui.pgaing
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/26  15:15
 */
class IntegralSource() : PagingSource<Int, IntegralHistory>() {
    override fun getRefreshKey(state: PagingState<Int, IntegralHistory>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IntegralHistory> {
        return try {
            withContext(IO){
                val page = params.key ?: 1 // set page 1 as default
                val repoResponse = TaskApi.create(ApiService::class.java).getIntegralHistory(page)
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (!repoResponse.data?.isEmpty()!!) page + 1 else null
                LoadResult.Page(repoResponse.data?.datas?: listOf(), prevKey, nextKey)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}