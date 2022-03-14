package com.hh.playandroid.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import com.hh.common.theme.*
import com.hh.common.util.CpNavigation
import com.hh.common.util.logE
import com.hh.playandroid.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/10  9:03
 */
@Composable
fun SearchView(modifier: Modifier = Modifier) {
    val viewModel : SearchViewModel = viewModel()
    Column(modifier) {
        SearchTopBar(viewModel = viewModel)
        SearchHot(viewModel = viewModel)
        SearchHistory(viewModel = viewModel)
    }
}

@Composable
private fun SearchTopBar(modifier: Modifier = Modifier, viewModel: SearchViewModel) {
    val paddingValues = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars)
    TopAppBar(
        {
            TextField(
                value = viewModel.viewStates.shareName, onValueChange = {
                    viewModel.dispatch(SearchAction.ChangeShareName(it))
                }, textStyle = TextStyle(fontSize = 15.sp),
                placeholder = {
                    Text(stringResource(id = R.string.please_key_search), fontSize = 15.sp)
                }, colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    placeholderColor = Color.Gray,
                    textColor = Color.White,
                    cursorColor = Color.Red
                ), modifier = modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .height(IntrinsicSize.Min),
                trailingIcon = {
                    if (viewModel.viewStates.shareName.isNotEmpty()) {
                        IconButton(onClick = { viewModel.dispatch(SearchAction.ClearSearchName) }) {
                            Icon(
                                Icons.Filled.Close, contentDescription = "close searchName",
                                tint = Color.White
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // 将键盘的回车键定义为搜索
                // 给回车键定义点击搜索事件，弹出搜索内容
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.dispatch(SearchAction.Share)
                }),
                singleLine = true
            )
        },
        backgroundColor = HhfTheme.colors.themeColor,
        contentPadding = paddingValues,
        navigationIcon = {
            IconButton(onClick = { CpNavigation.backAndReturnsIsLastPage() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = {
                viewModel.dispatch(SearchAction.Share)
            }) {
                Icon(
                    Icons.Filled.Search, contentDescription = "search",
                    tint = Color.White
                )
            }
        },
        elevation = 0.dp
    )
}

@Composable
fun SearchHot(modifier: Modifier = Modifier,viewModel: SearchViewModel) {
    "SearchHot".logE()
    LaunchedEffect(viewModel){
        viewModel.dispatch(SearchAction.GetHotList)
    }
    val list = remember{ derivedStateOf { viewModel.viewStates.hotList.isNotEmpty() }}
    Column(modifier) {
        Text(
            stringResource(R.string.search_hot),
            Modifier.padding(start = 12.dp,top = 16.dp),
            HhfTheme.colors.themeColor,
            16.sp
        )
        if(list.value){
            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)) {
                "FlowRow".logE()
                SearchHotItem(viewModel)
            }
        }
    }
}

@Composable
fun SearchHotItem(viewModel: SearchViewModel) {
    "SearchHotItem".logE()
    val list = remember{ viewModel.viewStates.hotList }
    repeat(list.size) {
        Box(
            modifier = Modifier
                .padding(start = 10.dp, top = 8.dp)
                .background(Color.LightGray.copy(0.6f))
                .padding(8.dp, 10.dp)
                .clickable {
                    viewModel.dispatch(SearchAction.ShareHistory(list[it].name))
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(list[it].name,Modifier,list[it].color,12.sp)
        }
    }
}

@Composable
fun SearchHistory(modifier: Modifier = Modifier, viewModel: SearchViewModel) {
    LaunchedEffect(viewModel){
        viewModel.dispatch(SearchAction.GetHistoryList)
    }
    val clearDialogState = rememberMaterialDialogState()
    MaterialDialog(dialogState = clearDialogState, buttons = {
        positiveButton(stringResource(com.hh.mine.R.string.confirm)) {
            viewModel.dispatch(SearchAction.ClearHistoryList)
        }
        negativeButton(stringResource(com.hh.mine.R.string.cancel))
    }) {
        title(stringResource(id = com.hh.mine.R.string.tips))
        message(res = R.string.clear_history)
    }
    Column(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.search_history),
                Modifier.padding(start = 12.dp),
                HhfTheme.colors.themeColor,
                16.sp
            )
            Text(
                stringResource(id = R.string.close),
                color = HhfTheme.colors.textColor,
                fontSize = 14.sp,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End)
                    .padding(end = 12.dp)
                    .clickable {
                        clearDialogState.show()
                    }
            )
        }
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 10.dp)) {
            items(viewModel.viewStates.historyList) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.dispatch(SearchAction.ShareHistory(it))
                        }
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        it,
                        Modifier.padding(start = 12.dp),
                        HhfTheme.colors.textColor,
                        15.sp
                    )
                    Icon(Icons.Filled.Close, contentDescription = "close history",
                        modifier = Modifier
                            .weight(1f)
                            .size(18.dp, 18.dp)
                            .wrapContentWidth(Alignment.End)
                            .padding(end = 12.dp)
                            .clickable {
                                viewModel.dispatch(SearchAction.RemoveHistory(it))
                            },
                        tint = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

