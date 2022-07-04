package com.hh.mine.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.android.renderscript.Toolkit
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.bean.Integral
import com.hh.common.bean.ModelPath
import com.hh.common.bean.UserInfo
import com.hh.common.ext.toBitmap
import com.hh.common.util.CacheUtils.avatar
import com.hh.common.util.CacheUtils.isLogin
import com.hh.common.util.CpNavigation
import com.hh.common.view.*
import com.hh.mine.api.ApiService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/5  17:09
 */
class MineViewModel : BaseViewModel() {

    var viewStates by mutableStateOf(MineViewState())
        private set

    fun dispatch(action: MineViewEvent) {
        when (action) {
            is MineViewEvent.Blur -> bitmapBlur(action.s)
            is MineViewEvent.ToComposable -> toComposable(action.type)
            is MineViewEvent.ChangePopupState -> viewStates =
                viewStates.copy(isShowPopup = action.flag)
            is MineViewEvent.SetUserInfo -> viewStates = viewStates.copy(userInfo = action.userInfo)
            is MineViewEvent.GetIntegral -> getIntegral()
        }
    }

    private fun toComposable(type: Int) {
        if(isLogin){
            when (type) {
                0 -> CpNavigation.to(ModelPath.Integral)
                1 -> CpNavigation.to(ModelPath.Collect)
                2 -> CpNavigation.to(ModelPath.Share)
                3 -> CpNavigation.to(ModelPath.Todo)
                4 -> CpNavigation.toBundle(ModelPath.WebView, Bundle().apply {
                    putString(webTitle, "PlayAndroid")
                    putString(webUrl, "https://github.com/yellowhai/PlayAndroid")
                    putBoolean(webIsCollect, false)
                    putInt(webCollectId, 998)
                    putInt(webCollectType, 1)
                })
                5 -> CpNavigation.to(ModelPath.Setting)
            }
        }
        else{
            when (type) {
                4 -> CpNavigation.toBundle(ModelPath.WebView, Bundle().apply {
                    putString(webTitle, "PlayAndroid")
                    putString(webUrl, "https://github.com/yellowhai/PlayAndroid")
                    putBoolean(webIsCollect, false)
                    putInt(webCollectId, 998)
                    putInt(webCollectType, 1)
                })
                5 -> CpNavigation.to(ModelPath.Setting)
                else -> CpNavigation.to(ModelPath.Login)
            }
        }
    }

    private fun getIntegral() {
        viewModelScope.launch {
            flow {
                emit(
                    TaskApi.create(ApiService::class.java).getIntegral()
                )
            }.map {
                if (it.errorCode == 0) {
                    it.data
                        ?: throw Exception("data null")
                } else {
                    throw Exception(it.errorMsg)
                }
            }.onEach {
                viewStates = viewStates.copy(integral = it)
            }.catch {
                viewStates = viewStates.copy(integral = null)
            }.collect()
        }
    }

    private fun bitmapBlur(s: String) {
        viewModelScope.launch {
            viewStates = if (s.startsWith("http") || s == "") {
                viewStates.copy(
                    avatarBitmap = s,
                    backgroundBitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        Toolkit.blur(s.toBitmap(), 25)
                    } else s.toBitmap()
                )
            } else {
                viewStates.copy(
                    avatarBitmap = s,
                    backgroundBitmap = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        Toolkit.blur(BitmapFactory.decodeFile(s), 25)
                    } else BitmapFactory.decodeFile(s)
                )
            }
        }
    }
}

sealed class MineViewEvent {
    data class Blur(val s: String) : MineViewEvent()
    data class ToComposable(val type: Int) : MineViewEvent()
    data class ChangePopupState(val flag: Boolean) : MineViewEvent()
    data class SetUserInfo(val userInfo: UserInfo?) : MineViewEvent()
    object GetIntegral : MineViewEvent()
}

data class MineViewState(
    val avatarBitmap: String = avatar,
    val backgroundBitmap: Bitmap? = null,
    val isShowPopup: Boolean = false,
    val userInfo: UserInfo? = null,
    val integral: Integral? = null
)