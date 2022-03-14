package com.hh.common.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hh.common.R
import com.hh.common.api.CommonApiService
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.base.launch
import com.hh.common.util.showToast

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/2  15:12
 */
class WebViewModel : BaseViewModel() {
    var viewStates by mutableStateOf(WebState())
        private set

    fun dispatch(action: WebAction) {
        when (action) {
            is WebAction.WebCollect -> webCollect()
            is WebAction.ChangePopup -> viewStates = viewStates.copy(isShowPopup = !viewStates.isShowPopup)
            is WebAction.WebBean -> viewStates = viewStates.copy(
                title = action.title,
                webUrl = action.webUrl,
                isCollect = action.isCollect,
                collectId = action.collectId,
                collectType = action.collectType
            )
        }
    }

    private fun webCollect() {
        launch({
            if (viewStates.isCollect) {
                if (viewStates.collectType == 0) {
                    TaskApi.create(CommonApiService::class.java).unCollect(viewStates.collectId)
                } else {
                    TaskApi.create(CommonApiService::class.java).unCollectUrl(viewStates.collectId)
                }
            } else {
                if (viewStates.collectType == 0) {
                    TaskApi.create(CommonApiService::class.java).setCollect(viewStates.collectId)
                } else {
                    TaskApi.create(CommonApiService::class.java)
                        .collectUrl(viewStates.title, viewStates.webUrl)
                }
            }

        }, {
            if (viewStates.isCollect) {
                context.showToast(context.getString(R.string.uncollect_success))
            } else {
                context.showToast(context.getString(R.string.collect_success))
            }
        }, {
            it.localizedMessage?.apply {
                context.showToast(this)
            }
        })
    }
}

sealed class WebAction {
    object WebCollect : WebAction()
    object ChangePopup : WebAction()
    data class WebBean(
        val title: String,
        val webUrl: String,
        val isCollect: Boolean,
        val collectId: Int,
        val collectType: Int,
        val isShowPopup: Boolean = false
    ) : WebAction()
}

data class WebState(
    val title: String = "",
    val webUrl: String = "",
    val isCollect: Boolean = false,
    val collectId: Int = 0,
    val collectType: Int = 0,//0 Inside  1 Url
    val isShowPopup: Boolean = false
)