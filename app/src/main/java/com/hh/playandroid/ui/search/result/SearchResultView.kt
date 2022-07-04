package com.hh.playandroid.ui.search.result

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CacheUtils
import com.hh.common.util.CpNavigation
import com.hh.common.view.ColumnTopBar
import com.hh.common.view.PagingItem
import com.hh.playandroid.ui.home.HomeListItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/10  21:32
 */
@Composable
fun SearchResultView(modifier: Modifier = Modifier, title: String) {
    val viewModel: SearchResultViewModel = viewModel()
    LaunchedEffect(viewModel) {
        viewModel.dispatch(SearchResultAction.GetSearchList(title))
    }
    ColumnTopBar(
        modifier
            .fillMaxSize()
            .background(HhfTheme.colors.background), title = title
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val listState = rememberLazyListState()
            SearchResultContent(viewModel, listState)
            SearchActionButton(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp), viewModel = viewModel
            ) {
                listState.animateScrollToItem(0)
            }
        }
    }
}

@Composable
fun SearchActionButton(
    modifier: Modifier = Modifier,
    viewModel: SearchResultViewModel,
    block: suspend () -> Unit
) {
    val scope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = viewModel.viewStates.isShowBtn,
        modifier, enter = slideInVertically() + fadeIn(), exit = slideOutVertically() + fadeOut()
    ) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    block()
                }
            },
            backgroundColor = HhfTheme.colors.themeColor,
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.ArrowUpward, contentDescription = "up")
        }
    }
}

@Composable
fun SearchResultContent(viewModel: SearchResultViewModel, listState: LazyListState) {
    val list = viewModel.viewStates.searchList?.collectAsLazyPagingItems()
    if (listState.isScrollInProgress) {
        val preItemIndex by remember { mutableStateOf(listState.firstVisibleItemIndex) }
        LaunchedEffect(Unit) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .collect {
                    when {
                        it > preItemIndex -> viewModel.dispatch(
                            SearchResultAction.ChangeShowBtn(false)
                        )
                        it < preItemIndex -> viewModel.dispatch(
                            SearchResultAction.ChangeShowBtn(true)
                        )
                    }
                }
        }
    }
    else{
        if(listState.firstVisibleItemIndex == 0){
            viewModel.dispatch(SearchResultAction.ChangeShowBtn(false))
        }
    }
    list?.apply {
        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.viewStates.isRefresh),
            onRefresh = {
                refresh()
            }) {
            PagingItem(list = this, errorBlock = {
                viewModel.dispatch(SearchResultAction.SetRefresh(false))
            }, successBlock = {
                viewModel.dispatch(SearchResultAction.SetRefresh(false))
            }, errorAndSuccessClick = {
                refresh()
            }) {
                LazyColumn(state = listState) {
                    items(it) { homeBean ->
                        homeBean?.apply {
                            HomeListItem(homeBean = this) {
                                if (CacheUtils.isLogin) {
                                    viewModel.dispatch(SearchResultAction.Collect(homeBean, it))
                                } else {
                                    CpNavigation.to(ModelPath.Login)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}