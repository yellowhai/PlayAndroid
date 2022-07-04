package com.hh.playandroid.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CpNavigation
import com.hh.common.ext.showToast
import com.hh.common.view.CpTopBar
import com.hh.playandroid.R
import kotlinx.coroutines.flow.collect

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/23  10:31
 */

const val USERNAME = "userName"
const val PASSWORD = "password"
@Composable
fun LoginView( userName : String?, password : String?) {
    val viewModel: LoginViewModel = viewModel()
    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White,
        placeholderColor = Color.White.copy(0.6f),
        leadingIconColor = Color.Yellow,
        textColor = Color.White,
        cursorColor = Color.Yellow
    )
    LaunchedEffect(Unit) {
        viewModel.apply {
            userName?.apply {
                dispatch(LoginAction.UpdateUserName(this))
            }
            password?.apply {
                dispatch(LoginAction.UpdatePassWord(this))
            }
            viewEvents.collect {
                when (it) {
                    is LoginEvent.Success -> {
                        context.showToast(context.getString(R.string.login_success))
                        CpNavigation.backAndReturnsIsLastPage()
                    }
                    is LoginEvent.Error -> context.showToast(it.msg)
                }
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(HhfTheme.colors.themeColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CpTopBar(
            Modifier.fillMaxWidth(),
            title = stringResource(R.string.login)
        ) {
            CpNavigation.backAndReturnsIsLastPage()
        }

        Text(
            stringResource(R.string.app_name),
            Modifier.padding(top = 32.dp),
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
        var focusUserName by remember { mutableStateOf(false) }
        var focusPwd by remember { mutableStateOf(false) }
        var isClickLogin by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = viewModel.viewStates.userName,
            onValueChange = {
                viewModel.dispatch(LoginAction.UpdateUserName(it))
            },
            Modifier
                .padding(top = 32.dp)
                .onFocusChanged {
                    focusUserName = it.hasFocus
                },
            colors = textFieldColors,
            label = {
                Text(stringResource(R.string.account))
            },
            placeholder = {
                Text(stringResource(R.string.please_input_username), fontSize = 15.sp)
            },
            leadingIcon = {
                Icon(Icons.Filled.VerifiedUser, contentDescription = "icon")
            },
            trailingIcon = {
                if (viewModel.viewStates.userName != "" && focusUserName) {
                    IconButton(onClick = { viewModel.dispatch(LoginAction.ClearUserName) }) {
                        Icon(
                            Icons.Filled.Close, contentDescription = "close UserName",
                            tint = Color.White
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext ={
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            singleLine = true,
            isError = (viewModel.viewStates.userName == "" && isClickLogin)
        )

        OutlinedTextField(
            value = viewModel.viewStates.password,
            onValueChange = {
                viewModel.dispatch(LoginAction.UpdatePassWord(it))
            },
            Modifier
                .padding(top = 12.dp)
                .onFocusChanged {
                    focusPwd = it.hasFocus
                },
            colors = textFieldColors,
            label = {
                Text(stringResource(R.string.password))
            },
            placeholder = {
                Text(stringResource(R.string.please_input_password), fontSize = 15.sp)
            },
            leadingIcon = {
                Icon(
                    if (viewModel.viewStates.isShowPwd) Icons.Filled.LockOpen
                    else Icons.Filled.Lock, contentDescription = "lock",
                    Modifier.clickable {
                        viewModel.dispatch(LoginAction.ChangeShowPwd)
                    }
                )
            },
            trailingIcon = {
                if (viewModel.viewStates.password != "" && focusPwd) {
                    IconButton(onClick = { viewModel.dispatch(LoginAction.ClearPassWord) }) {
                        Icon(
                            Icons.Filled.Close, contentDescription = "close PassWord",
                            tint = Color.White
                        )
                    }
                }
            },
            visualTransformation = if (viewModel.viewStates.isShowPwd) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            isError = (viewModel.viewStates.password == "" && isClickLogin)
        )

        Column(
            Modifier
                .padding(top = 64.dp, bottom = 20.dp)
                .width(280.dp)
        ) {
            Button(
                onClick = {
                    isClickLogin = true
                    viewModel.dispatch(LoginAction.Login)
                },
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(stringResource(id = R.string.login))
            }

            Text(
                stringResource(id = R.string.register),
                Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.End)
                    .clickable {
                        CpNavigation.to(ModelPath.Register)
                    }, Color.White, 12.sp
            )
        }
    }
}