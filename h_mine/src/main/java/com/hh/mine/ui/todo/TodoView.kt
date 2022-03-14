package com.hh.mine.ui.todo

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CpNavigation
import com.hh.common.view.ColumnTopBar
import com.hh.common.view.PagingItem
import com.hh.mine.R
import com.hh.mine.bean.TodoBean
import com.hh.mine.ui.todo.add.todoAddBean
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/3  11:12
 */
@Composable
fun TodoView() {
    val viewModel: TodoViewModel = viewModel()
    ColumnTopBar(
        Modifier
            .fillMaxSize()
            .background(HhfTheme.colors.background), title = "TODO", actions = {
        IconButton(onClick = {
            CpNavigation.to(ModelPath.TodoAdd)
        }) {
            Icon(
                Icons.Filled.Add, contentDescription = "add",
                tint = Color.White
            )
        }
    }) {
        TodoList(viewModel = viewModel)
    }
}

@Composable
fun TodoList(modifier: Modifier = Modifier, viewModel: TodoViewModel) {
    val todoList = viewModel.getTodoList().collectAsLazyPagingItems()
    var isRefresh by remember { mutableStateOf(false) }
    SwipeRefresh(state = rememberSwipeRefreshState(isRefresh), onRefresh = {
        todoList.refresh()
    }, modifier) {
        PagingItem(Modifier, todoList,
            { isRefresh = false },
            { isRefresh = false },
            { todoList.refresh() }
        ) {
            LazyColumn {
                items(it) {
                    it?.run {
                        var isShowPopup by remember { mutableStateOf(false) }
                        Box {
                            Card(
                                Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        isShowPopup = false
                                        CpNavigation.toBundle(ModelPath.TodoAdd, Bundle().apply {
                                            putSerializable(todoAddBean, this@run)
                                        })
                                    },
                                backgroundColor = HhfTheme.colors.listItem
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Row(Modifier.padding(top = 10.dp)) {
                                        Text(
                                            title,
                                            color = HhfTheme.colors.textColor,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                        Row(
                                            Modifier
                                                .weight(1f)
                                                .wrapContentWidth(Alignment.End)
                                        ) {
                                            Text(
                                                dateStr,
                                                Modifier.align(Alignment.CenterVertically),
                                                HhfTheme.colors.textInactiveColor,
                                                12.sp
                                            )
                                            Column {
                                                Icon(
                                                    Icons.Default.MoreVert,
                                                    "",
                                                    Modifier.clickable {
                                                        isShowPopup = true
                                                    },
                                                    tint = HhfTheme.colors.textColor
                                                )
                                                TodoPopup(isShowPopup,
                                                    when {
                                                        status == 1 -> false
                                                        System.currentTimeMillis() > date -> false
                                                        else -> true
                                                    },
                                                    viewModel,
                                                    this@run,
                                                    { todoList.refresh() }) {
                                                    isShowPopup = false
                                                }
                                            }
                                        }

                                    }
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 8.dp)
                                    )
                                    Text(
                                        content,
                                        color = HhfTheme.colors.textColor,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                            Box(
                                Modifier
                                    .padding(top = 10.dp, end = 5.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    painter = painterResource(R.mipmap.icon_todo_right),
                                    contentDescription = "type",
                                    Modifier,
                                    if (priority == 0) Color.Red.copy(0.8f) else HhfTheme.colors.themeColor,
                                )
                                Text(
                                    when {
                                        status == 1 -> stringResource(id = R.string.done)
                                        System.currentTimeMillis() > date + (24 * 60 * 60 * 1000) -> stringResource(
                                            id = R.string.expire
                                        )
                                        else -> stringResource(id = R.string.undone)
                                    },
                                    Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(top = 2.dp), Color.White, 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoPopup(
    isShowPopup: Boolean,
    isComplete: Boolean,
    viewModel: TodoViewModel,
    t: TodoBean,
    refresh: () -> Unit,
    block: () -> Unit
) {
    val deleteMaterialDialogState = rememberMaterialDialogState()
    MaterialDialog(dialogState = deleteMaterialDialogState, buttons = {
        positiveButton(stringResource(R.string.confirm)) {
            viewModel.delete(t.id, refresh)
        }
        negativeButton(stringResource(R.string.cancel))
    }) {
        title(stringResource(id = R.string.tips))
        message(res = R.string.sure_delete_it)
    }
    DropdownMenu(isShowPopup, onDismissRequest = {
        block()
    }) {
        DropdownMenuItem(onClick = {
            block()
            deleteMaterialDialogState.show()
        }) {
            Text(stringResource(R.string.delete))
        }
        if (isComplete) {
            DropdownMenuItem(onClick = {
                block()
                CpNavigation.toBundle(ModelPath.TodoAdd, Bundle().apply {
                    putSerializable(todoAddBean, t)
                })
            }) {
                Text(stringResource(R.string.edit))
            }
            DropdownMenuItem(onClick = {
                block()
                viewModel.done(t.id, refresh)
            }) {
                Text(stringResource(R.string.done))
            }
        }
    }
}