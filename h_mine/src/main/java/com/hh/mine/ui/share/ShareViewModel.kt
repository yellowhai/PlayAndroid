package com.hh.mine.ui.share

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.launch
import com.hh.mine.api.ApiService
import com.hh.mine.bean.Article
import com.hh.mine.ui.integral.PAGE_SIZE
import com.hh.mine.ui.share.paging.ShareSource
import kotlinx.coroutines.flow.Flow

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/11  13:11
 */
class ShareViewModel : BaseViewModel() {
    fun getShareList() : Flow<PagingData<Article>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { ShareSource() }).flow
    }

    fun delete(id : Int,block : ()-> Unit){
        launch({
            TaskApi.create(ApiService::class.java).deleteShareArticle(id)
        },{
            block()
        })
    }
}