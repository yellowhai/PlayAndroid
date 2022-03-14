package com.hh.playandroid.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.VerifiedUser
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
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CpNavigation
import com.hh.common.util.showToast
import com.hh.common.view.CpTopBar
import com.hh.playandroid.R
import kotlinx.coroutines.flow.collect

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/24  8:53
 */

@Composable
fun RegisterView() {
    Column(
        Modifier
            .fillMaxSize()
            .background(HhfTheme.colors.themeColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val viewModel: RegisterViewModel = viewModel()
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
            viewModel.viewEvents.collect {
                when (it) {
                    is RegisterEvent.Success -> {
                        context.showToast(context.getString(R.string.register_success))
                        CpNavigation.navHostController.previousBackStackEntry
                            ?.arguments?.apply {
                                putSerializable(USERNAME, viewModel.viewStates.userName)
                                putSerializable(PASSWORD, viewModel.viewStates.password)
                            }
                        CpNavigation.backAndReturnsIsLastPage()
                    }
                    is RegisterEvent.Error -> context.showToast(it.msg)
                }
            }
        }
        CpTopBar(
            Modifier.fillMaxWidth(),
            title = stringResource(R.string.register)
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
        var isClickRegister by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = viewModel.viewStates.userName,
            onValueChange = {
                viewModel.dispatch(RegisterAction.UpdateUserName(it))
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
                    IconButton(onClick = { viewModel.dispatch(RegisterAction.ClearUserName) }) {
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
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            singleLine = true,
            isError = (viewModel.viewStates.userName == "" && isClickRegister)
        )

        OutlinedTextField(
            value = viewModel.viewStates.password,
            onValueChange = {
                viewModel.dispatch(RegisterAction.UpdatePassWord(it))
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
                        viewModel.dispatch(RegisterAction.ChangeShowPwd)
                    }
                )
            },
            trailingIcon = {
                if (viewModel.viewStates.password != "" && focusPwd) {
                    IconButton(onClick = { viewModel.dispatch(RegisterAction.ClearPassWord) }) {
                        Icon(
                            Icons.Filled.Close, contentDescription = "close PassWord",
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
            visualTransformation = if (viewModel.viewStates.isShowPwd) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            isError = (viewModel.viewStates.password == "" && isClickRegister)
        )

        OutlinedTextField(
            value = viewModel.viewStates.passwordC,
            onValueChange = {
                viewModel.dispatch(RegisterAction.UpdatePassWordC(it))
            },
            Modifier
                .padding(top = 12.dp)
                .onFocusChanged {
                    focusPwd = it.hasFocus
                },
            colors = textFieldColors,
            label = {
                Text(stringResource(R.string.password_confirm))
            },
            placeholder = {
                Text(stringResource(R.string.please_input_password_confirm), fontSize = 15.sp)
            },
            leadingIcon = {
                Icon(
                    if (viewModel.viewStates.isShowPwd) Icons.Filled.LockOpen
                    else Icons.Filled.Lock, contentDescription = "lock",
                    Modifier.clickable {
                        viewModel.dispatch(RegisterAction.ChangeShowPwd)
                    }
                )
            },
            trailingIcon = {
                if (viewModel.viewStates.passwordC != "" && focusPwd) {
                    IconButton(onClick = { viewModel.dispatch(RegisterAction.ClearPassWordC) }) {
                        Icon(
                            Icons.Filled.Close, contentDescription = "close PassWord confirm",
                            tint = Color.White
                        )
                    }
                }
            },
            visualTransformation = if (viewModel.viewStates.isShowPwd) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            isError = (viewModel.viewStates.passwordC == "" && isClickRegister && viewModel.viewStates.passwordC == viewModel.viewStates.password)
        )

        Button(
            onClick = {
                isClickRegister = true
                viewModel.dispatch(RegisterAction.Register)
            },
            Modifier
                .padding(top = 64.dp, bottom = 20.dp)
                .width(280.dp)
                .height(45.dp)
        ) {
            Text(stringResource(id = R.string.register))
        }
    }
}