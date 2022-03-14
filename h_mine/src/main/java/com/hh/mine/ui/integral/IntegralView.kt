package com.hh.mine.ui.integral

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hh.common.theme.HhfTheme
import com.hh.common.util.getTimeAgo
import com.hh.common.view.ColumnTopBar
import com.hh.common.view.PagingItem
import com.hh.mine.R

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/24  16:26
 */

@Composable
fun IntegralView() {
    val viewModel: IntegralViewModel = viewModel()
    ColumnTopBar(Modifier
        .fillMaxSize()
        .background(HhfTheme.colors.background),title = stringResource(R.string.main_mine_integral),actions = {
        if (viewModel.viewStates.isShowError) {
            IconButton(onClick = {
                viewModel.dispatch(IntegralAction.GetList)
            }) {
                Icon(
                    Icons.Filled.Refresh, contentDescription = "refresh",
                    tint = Color.White
                )
            }
        }
    }) {
        IntegralContent(viewModel = viewModel)
    }
}

@Composable
fun IntegralContent(modifier: Modifier = Modifier, viewModel: IntegralViewModel) {
    val integralList = viewModel.viewStates.list.collectAsLazyPagingItems()
    SwipeRefresh(state = rememberSwipeRefreshState(viewModel.viewStates.isRefresh), onRefresh = {
        integralList.refresh()
    },modifier) {
        PagingItem(Modifier,integralList,{
            viewModel.dispatch(IntegralAction.SetShowError(true))
            viewModel.dispatch(IntegralAction.SetRefresh(false))
        },{
            viewModel.dispatch(IntegralAction.SetShowError(false))
            viewModel.dispatch(IntegralAction.SetRefresh(false))
        },{
            integralList.refresh()
        }){
            LazyColumn {
                items(it) {
                    it?.run {
                        Card(Modifier.padding(8.dp),backgroundColor = HhfTheme.colors.listItem) {
                            Row(Modifier.padding(16.dp)) {
                                Column {
                                    Text(
                                        "$reason${
                                            desc.subSequence(
                                                desc.indexOf("积分"),
                                                desc.length
                                            )
                                        }",color = HhfTheme.colors.textColor, fontSize = 15.sp
                                    )
                                    Text(
                                        getTimeAgo(date),
                                        Modifier.padding(top = 8.dp),
                                        color = HhfTheme.colors.textInactiveColor,
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    "+ $coinCount",
                                    Modifier
                                        .weight(1f)
                                        .wrapContentWidth(
                                            Alignment.End
                                        )
                                        .align(Alignment.CenterVertically),
                                    HhfTheme.colors.themeColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}