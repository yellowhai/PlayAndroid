package com.hh.playandroid.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hh.common.base.BaseViewModel
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.ext.toJson
import com.hh.common.util.CacheUtils
import com.hh.common.view.ldDismiss
import com.hh.common.view.showLd
import com.hh.playandroid.R
import com.hh.common.api.TaskApi
import com.hh.playandroid.api.ApiServices
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/23  10:39
 */
class LoginViewModel : BaseViewModel() {
    var viewStates by mutableStateOf(LoginState())
        private set

    private val _viewEvents = Channel<LoginEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()


    fun dispatch(action: LoginAction) {
        when (action) {
            LoginAction.ClearUserName -> viewStates = viewStates.copy(userName = "")
            LoginAction.ClearPassWord -> viewStates = viewStates.copy(password = "")
            LoginAction.ChangeShowPwd -> viewStates =
                viewStates.copy(isShowPwd = !viewStates.isShowPwd)
            is LoginAction.UpdateUserName -> viewStates =
                viewStates.copy(userName = action.userName)
            is LoginAction.UpdatePassWord -> viewStates =
                viewStates.copy(password = action.password)
             LoginAction.Login -> login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            if(viewStates.userName.isEmpty()){
                _viewEvents.send(LoginEvent.Error(context.getString(R.string.please_input_username)))
                return@launch
            }
            if(viewStates.password.isEmpty()){
                _viewEvents.send(LoginEvent.Error(context.getString(R.string.please_input_password)))
                return@launch
            }
            flow {
                emit(
                    TaskApi.create(ApiServices::class.java)
                        .login(viewStates.userName.trim(), viewStates.password.trim())
                )
            }.onStart {
                showLd()
            }.onCompletion {
                ldDismiss()
            }.map {
                if (it.errorCode == 0) {
                    it.data
                        ?: throw Exception("data null")
                } else {
                    throw Exception(it.errorMsg)
                }
            }.onEach {
                CacheUtils.userInfo = it.toJson()
                CacheUtils.isLogin = true
                _viewEvents.send(LoginEvent.Success)
            }.catch {
                _viewEvents.send(LoginEvent.Error(it.message ?: ""))
            }.collect()
        }
    }

}

sealed class LoginEvent {
    object Success : LoginEvent()
    data class Error(val msg: String) : LoginEvent()
}

sealed class LoginAction {
    object ClearUserName : LoginAction()
    object ClearPassWord : LoginAction()
    object ChangeShowPwd : LoginAction()
    object Login : LoginAction()
    data class UpdateUserName(val userName: String) : LoginAction()
    data class UpdatePassWord(val password: String) : LoginAction()
}

data class LoginState(
    val userName: String = "",
    val password: String = "",
    val isShowPwd: Boolean = false
)