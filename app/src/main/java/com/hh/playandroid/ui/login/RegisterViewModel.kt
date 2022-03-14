package com.hh.playandroid.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hh.common.base.BaseViewModel
import com.hh.common.base.YshhApplication.Companion.context
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
 * @CreateDate: 2022/2/24  10:23
 */
class RegisterViewModel : BaseViewModel() {

    var viewStates by mutableStateOf(RegisterState())
        private set

    private val _viewEvents = Channel<RegisterEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: RegisterAction) {
        when (action) {
            RegisterAction.ClearUserName -> viewStates = viewStates.copy(userName = "")
            RegisterAction.ClearPassWord -> viewStates = viewStates.copy(password = "")
            RegisterAction.ClearPassWordC -> viewStates = viewStates.copy(passwordC = "")
            RegisterAction.ChangeShowPwd -> viewStates =
                viewStates.copy(isShowPwd = !viewStates.isShowPwd)
            is RegisterAction.UpdateUserName -> viewStates =
                viewStates.copy(userName = action.userName)
            is RegisterAction.UpdatePassWord -> viewStates =
                viewStates.copy(password = action.password)
            is RegisterAction.UpdatePassWordC -> viewStates =
                viewStates.copy(passwordC = action.password)
            RegisterAction.Register -> register()
        }
    }

    private fun register() {
        viewModelScope.launch {
            if(viewStates.userName.isEmpty()){
                _viewEvents.send(RegisterEvent.Error(context.getString(R.string.please_input_username)))
                return@launch
            }
            if(viewStates.password.isEmpty()){
                _viewEvents.send(RegisterEvent.Error(context.getString(R.string.please_input_password)))
                return@launch
            }
            if(viewStates.passwordC.isEmpty()){
                _viewEvents.send(RegisterEvent.Error(context.getString(R.string.please_input_password_confirm)))
                return@launch
            }
            if(viewStates.password != viewStates.passwordC){
                _viewEvents.send(RegisterEvent.Error(context.getString(R.string.Inconsistent_password)))
                return@launch
            }
            flow {
                emit(
                    TaskApi.create(ApiServices::class.java)
                        .register(
                            viewStates.userName.trim(),
                            viewStates.password.trim(),
                            viewStates.passwordC.trim()
                        )
                )
            }.onStart {
                showLd()
            }.onCompletion {
                ldDismiss()
            }.
            map {
                if (it.errorCode == 0) {
                    it.data
                        ?: throw Exception("data null")
                } else {
                    throw Exception(it.errorMsg)
                }
            }.onEach {
                _viewEvents.send(RegisterEvent.Success)
            }.catch {
                _viewEvents.send(RegisterEvent.Error(it.message ?: ""))
            }.collect()
        }
    }
}

sealed class RegisterEvent {
    object Success : RegisterEvent()
    data class Error(val msg: String) : RegisterEvent()
}

sealed class RegisterAction {
    object ClearUserName : RegisterAction()
    object ClearPassWord : RegisterAction()
    object ClearPassWordC : RegisterAction()
    object ChangeShowPwd : RegisterAction()
    object Register : RegisterAction()
    data class UpdateUserName(val userName: String) : RegisterAction()
    data class UpdatePassWord(val password: String) : RegisterAction()
    data class UpdatePassWordC(val password: String) : RegisterAction()
}

data class RegisterState(
    val userName: String = "",
    val password: String = "",
    val passwordC: String = "",
    val isShowPwd: Boolean = false
)