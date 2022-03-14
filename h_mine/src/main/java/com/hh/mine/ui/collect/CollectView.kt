package com.hh.mine.ui.collect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hh.common.network.Resource
import com.hh.common.theme.HhfTheme
import com.hh.common.view.*
import com.hh.mine.R
import com.hh.mine.bean.CollectUrl
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/25  13:52
 */

@ExperimentalPagerApi
@Composable
fun CollectView() {
    val viewModel: CollectViewModel = viewModel()
    ColumnTopBar(Modifier
        .fillMaxSize()
        .background(HhfTheme.colors.background),title = stringResource(R.string.main_mine_collect)) {
        val pagerState = rememberPagerState()
        Spacer(modifier = Modifier.height(5.dp))
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = HhfTheme.colors.themeColor,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clip(RoundedCornerShape(50))
                .background(HhfTheme.colors.themeColor)
                .padding(1.dp),
            indicator = {
//                TabRowDefaults.Indicator(
//                    Modifier.ownTabIndicatorOffset(
//                        it[pagerState.currentPage],
//                        viewModel.viewStates.TabTextWidthList[pagerState.currentPage]
//                    ),
//                    2.dp,
//                    HhfTheme.colors.themeColor
//                )
                Box {}
            }
        ) {
            CollectTab(collectTabList, pagerState)
        }

        CollectPager(Modifier, viewModel, collectTabList, pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CollectTab(
    tabList: List<String>,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()
    tabList.forEachIndexed { index, title ->
        Tab(modifier = if (pagerState.currentPage == index) Modifier
            .clip(RoundedCornerShape(50))
            .background(
                Color.White
            )
        else Modifier
            .clip(RoundedCornerShape(50))
            .background(
                HhfTheme.colors.themeColor
            ),
            text = {
                Text(title)
            },
            selected = pagerState.currentPage == index,
            onClick = {
                coroutineScope.launch { pagerState.animateScrollToPage(index) }
            },
            selectedContentColor = HhfTheme.colors.themeColor,
            unselectedContentColor = Color.White.copy(0.5f),
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CollectPager(
    modifier: Modifier = Modifier,
    viewModel: CollectViewModel,
    tabList: List<String>,
    pagerState: PagerState
) {
    HorizontalPager(tabList.size, modifier, pagerState) { page ->
        when (page) {
            0 -> InSideCollect(Modifier.fillMaxSize(), viewModel)
            1 -> UrlCollect(Modifier.fillMaxSize(), viewModel)
        }
    }
}

@Composable
fun InSideCollect(modifier: Modifier = Modifier, viewModel: CollectViewModel) {
    val inSideList = viewModel.viewStates.insideList.collectAsLazyPagingItems()
    SwipeRefresh(state = rememberSwipeRefreshState(viewModel.viewStates.isInsideRefresh), onRefresh = {
        inSideList.refresh()
    }, modifier) {
        PagingItem(Modifier, inSideList, {
            viewModel.dispatch(CollectAction.SetInsideRefresh(false))
        }, {
            viewModel.dispatch(CollectAction.SetInsideRefresh(false))
        }, {
            inSideList.refresh()
        }) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(it) {
                    it?.apply {
                        InsideView(collectInside = this){
                            viewModel.dispatch(CollectAction.UnCollect(it.originId))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UrlCollect(modifier: Modifier = Modifier, viewModel: CollectViewModel) {
    LaunchedEffect(Unit) {
        viewModel.dispatch(CollectAction.GetUrlList)
    }
    SwipeRefresh(state = rememberSwipeRefreshState(viewModel.viewStates.isUrlRefresh), onRefresh = {
        viewModel.dispatch(CollectAction.GetUrlList)
    }) {
        BoxWithConstraints(modifier) {
            when (viewModel.viewStates.urlList) {
                is Resource.Loading -> {
                    BoxProgress()
                }
                is Resource.DataError -> {
                    ErrorBox(
                        modifier
                            .height(maxHeight)
                            .width(maxWidth),
                        (viewModel.viewStates.urlList as Resource.DataError<List<CollectUrl>>).errorMsg?.run {
                            errorMsg
                        } ?: stringResource(id = R.string.no_network)
                    )
                }
                is Resource.NoData -> {
                    ErrorBox(
                        modifier
                            .height(maxHeight)
                            .width(maxWidth),
                        stringResource(com.hh.common.R.string.no_data)
                    ){
                        viewModel.dispatch(CollectAction.GetUrlList)
                    }
                }
                is Resource.Success -> {
                    val list = (viewModel.viewStates.urlList as? Resource.Success<List<CollectUrl>>)?.data
                    list?.takeIf { it.isNotEmpty() }?.apply {
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(this@apply) {
                                CollectUrlView(collectUrl = it, viewModel = viewModel)
                            }
                        }
                    }?: run{
                        ErrorBox(
                            modifier
                                .height(maxHeight)
                                .width(maxWidth),
                            stringResource(com.hh.common.R.string.no_data)
                        ){
                            viewModel.dispatch(CollectAction.GetUrlList)
                        }
                    }
                }
            }
        }

    }
}