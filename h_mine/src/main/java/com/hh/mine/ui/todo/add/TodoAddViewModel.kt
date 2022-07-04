package com.hh.mine.ui.todo.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.base.launch
import com.hh.common.util.CpNavigation
import com.hh.common.util.formatDate
import com.hh.common.ext.showToast
import com.hh.mine.R
import com.hh.mine.api.ApiService

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/4  21:15
 */
class TodoAddViewModel : BaseViewModel() {

    var viewStates by mutableStateOf(TodoAddState())
        private set

    fun dispatch(action: TodoAddAction) {
        when (action) {
            TodoAddAction.Change -> viewStates = viewStates.copy(isChange = true)
            TodoAddAction.Add -> add()
            TodoAddAction.ClearDetail -> viewStates = viewStates.copy(detail = "")
            TodoAddAction.ClearTitle -> viewStates = viewStates.copy(title = "")
            is TodoAddAction.UpdateDetail -> viewStates = viewStates.copy(detail = action.s)
            is TodoAddAction.UpdateLevel -> viewStates = viewStates.copy(level = action.s)
            is TodoAddAction.UpdateTime -> viewStates = viewStates.copy(time = action.s)
            is TodoAddAction.UpdateTitle -> viewStates = viewStates.copy(title = action.s)
            is TodoAddAction.InitState -> viewStates = viewStates.copy(
                title = action.title,
                detail = action.detail,
                level = action.level,
                time = action.time,
                changeId = action.changeId
            )
        }
    }

    private fun add() {
        if(viewStates.title.isEmpty()){
            context.showToast(context.getString(R.string.please_input_title))
            return
        }
        launch({
            if (!viewStates.isChange) {
                TaskApi.create(ApiService::class.java).addTodo(
                    viewStates.title,
                    viewStates.detail,
                    viewStates.time,
                    0,
                    viewStates.level
                )
            } else {
                TaskApi.create(ApiService::class.java).updateTodo(
                    viewStates.title,
                    viewStates.detail,
                    viewStates.time,
                    0,
                    viewStates.level,
                    viewStates.changeId
                )
            }
        }, {
            context.showToast(context.getString(R.string.save_success))
            CpNavigation.backAndReturnsIsLastPage()
        }, {
            it.localizedMessage?.apply {
                context.showToast(this)
            }
        })
    }
}

sealed class TodoAddAction {
    object Add : TodoAddAction()
    data class UpdateTitle(val s: String) : TodoAddAction()
    data class UpdateDetail(val s: String) : TodoAddAction()
    data class UpdateLevel(val s: Int) : TodoAddAction()
    data class UpdateTime(val s: String) : TodoAddAction()
    data class InitState(
        val title: String,
        val detail: String,
        val level: Int,
        val time: String,
        val changeId: Int
    ) : TodoAddAction()

    object ClearTitle : TodoAddAction()
    object ClearDetail : TodoAddAction()
    object Change : TodoAddAction()
}

data class TodoAddState(
    val title: String = "",
    val detail: String = "",
    val level: Int = 1,
    val time: String = formatDate("yyyy-MM-dd", isHour = false),
    val isChange: Boolean = false,
    val changeId: Int = 0
)