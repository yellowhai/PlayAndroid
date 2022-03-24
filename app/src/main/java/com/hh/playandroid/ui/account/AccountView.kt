package com.hh.playandroid.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
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
import com.hh.common.bean.ModelPath
import com.hh.common.ext.ownTabIndicatorOffset
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CacheUtils
import com.hh.common.util.CpNavigation
import com.hh.common.util.logE
import com.hh.common.view.*
import com.hh.playandroid.R
import com.hh.playandroid.ui.home.HomeListItem
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/22  19:41
 */

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AccountView(modifier: Modifier = Modifier) {
    "AccountView".logE()
    val viewModel: AccountViewModel = viewModel()
    ColumnTopBarMain(
        modifier.background(HhfTheme.colors.background),
        stringResource(id = R.string.main_title_account)
    ) {
        val pagerState = rememberPagerState()
        AccountTabLayout(viewModel, pagerState)
        AccountContent(viewModel, pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AccountTabLayout(viewModel: AccountViewModel, pagerState: PagerState) {
    "AccountTabLayout".logE()
    LaunchedEffect(viewModel) {
        viewModel.dispatch(AccountAction.GetTabList)
    }
    if (viewModel.viewStates.tabList.isNotEmpty()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = HhfTheme.colors.bottomBar,
            indicator = {
                TabRowDefaults.Indicator(
                    Modifier.ownTabIndicatorOffset(
                        it[pagerState.currentPage],
                        viewModel.viewStates.tabTextWidthList[pagerState.currentPage]
                    ),
                    2.dp,
                    HhfTheme.colors.themeColor
                )
            },
            edgePadding = 1.dp
        ) {
            AccountTabs(viewModel, pagerState)
        }
    } else {
        if (viewModel.viewStates.isShowError) {
            ErrorBox(title = stringResource(id = R.string.no_network)) {
                viewModel.dispatch(AccountAction.GetTabList)
            }
        } else {
            BoxProgress()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AccountContent(viewModel: AccountViewModel, pagerState: PagerState) {
    "AccountContent".logE()
    if (viewModel.viewStates.tabList.isNotEmpty()) {
        HorizontalPager(count = viewModel.viewStates.tabList.size, state = pagerState) { page ->
            when (page) {
                viewModel.viewStates.tabList[page].pageId -> {
                    AccountList(viewModel, viewModel.viewStates.tabList[page].id)
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AccountTabs(viewModel: AccountViewModel, pagerState: PagerState) {
    "AccountTabs".logE()
    val coroutineScope = rememberCoroutineScope()
    viewModel.viewStates.tabList.forEachIndexed { index, accountTab ->
        Tab(
            text = {
                Text(accountTab.name,
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            viewModel.viewStates.tabTextWidthList.add(placeable.width.toDp())
                            placeable.placeRelative(0, 0)
                        }
                    }
                )
            },
            selected = pagerState.currentPage == index,
            onClick = {
                coroutineScope.launch { pagerState.animateScrollToPage(index) }
            },
            selectedContentColor = HhfTheme.colors.themeColor,
            unselectedContentColor = HhfTheme.colors.textColor,
        )
    }
}

@Composable
fun AccountList(viewModel: AccountViewModel, page: Int) {
    "AccountList".logE()
    val list = viewModel.getList(page).collectAsLazyPagingItems()
    SwipeRefresh(state = rememberSwipeRefreshState(viewModel.viewStates.isRefresh), onRefresh = {
        list.refresh()
    }) {
        PagingItem(list = list, errorBlock = {
            viewModel.dispatch(AccountAction.SetRefresh(false))
        }, successBlock = {
            viewModel.dispatch(AccountAction.SetRefresh(false))
        }, errorAndSuccessClick = {
            list.refresh()
        }) {
            viewModel.dispatch(AccountAction.SetRefresh(false))
            LazyColumn {
                items(it) { homeBean ->
                    homeBean?.apply {
                        HomeListItem(homeBean = this, isShowLabel = false) {
                            if (CacheUtils.isLogin) {
                                viewModel.dispatch(AccountAction.Collect(homeBean, it))
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