package com.hh.mine.ui.share.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.base.launch
import com.hh.common.util.CpNavigation
import com.hh.common.ext.showToast
import com.hh.mine.R
import com.hh.mine.api.ApiService

/**
 * @Description: Share
 * @Author: yshh
 * @CreateDate: 2022/3/4  21:15
 */
class ShareAddViewModel : BaseViewModel() {

    var viewStates by mutableStateOf(ShareAddState())
        private set

    fun dispatch(action: ShareAddAction) {
        when (action) {
            ShareAddAction.Add -> add()
            ShareAddAction.ClearTitle -> viewStates = viewStates.copy(title = "")
            ShareAddAction.ClearLink -> viewStates = viewStates.copy(link = "")
            is ShareAddAction.UpdateTitle -> viewStates = viewStates.copy(title = action.s)
            is ShareAddAction.UpdateLink -> viewStates = viewStates.copy(link = action.s)
        }
    }

    private fun add() {
        if(viewStates.title.isEmpty()){
            context.showToast(context.getString(R.string.please_input_title))
            return
        }
        if(viewStates.link.isEmpty()){
            context.showToast(context.getString(R.string.please_input_address))
            return
        }
        launch({
                TaskApi.create(ApiService::class.java).addAriticle(viewStates.title, viewStates.link)
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

sealed class ShareAddAction {
    object Add : ShareAddAction()
    data class UpdateTitle(val s: String) : ShareAddAction()
    data class UpdateLink(val s: String) : ShareAddAction()
    object ClearTitle : ShareAddAction()
    object ClearLink : ShareAddAction()
}

data class ShareAddState(
    val title: String = "",
    val link: String = "",
)