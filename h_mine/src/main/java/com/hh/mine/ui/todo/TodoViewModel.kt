package com.hh.mine.ui.todo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.launch
import com.hh.mine.api.ApiService
import com.hh.mine.bean.TodoBean
import com.hh.mine.ui.integral.PAGE_SIZE
import com.hh.mine.ui.todo.paging.TodoSource
import kotlinx.coroutines.flow.Flow

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/3  11:22
 */
class TodoViewModel : BaseViewModel() {
    fun getTodoList() : Flow<PagingData<TodoBean>>{
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { TodoSource() }).flow
    }

    fun done(id : Int,block : ()-> Unit){
        launch({
            TaskApi.create(ApiService::class.java).doneTodo(id,1)
        },{
            block()
        })
    }

    fun delete(id : Int,block : ()-> Unit){
        launch({
            TaskApi.create(ApiService::class.java).deleteTodo(id)
        },{
            block()
        })
    }
}