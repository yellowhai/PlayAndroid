package com.hh.playandroid.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.hh.common.ext.pagerTabIndicatorOffsetH
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CacheUtils
import com.hh.common.util.CpNavigation
import com.hh.common.view.*
import com.hh.playandroid.R
import com.hh.playandroid.ui.home.HomeListItem
import kotlinx.coroutines.launch

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/9  19:50
 */

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProjectView(modifier: Modifier = Modifier) {
    val viewModel : ProjectViewModel = viewModel()
    Box(
        modifier
            .background(HhfTheme.colors.themeColor)
            .navigationBarsPadding()
            .systemBarsPadding()) {
        Column(Modifier.background(HhfTheme.colors.background)) {
            val pagerState = rememberPagerState()
            ProjectTabLayout(viewModel, pagerState)
            ProjectContent(viewModel, pagerState)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProjectTabLayout(viewModel: ProjectViewModel, pagerState: PagerState) {
    LaunchedEffect(viewModel) {
        viewModel.dispatch(ProjectAction.GetTabList)
    }
    if (viewModel.viewStates.tabList.isNotEmpty()) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                Modifier
                    .clickable(onClick = {}, indication = null, interactionSource = remember {
                        MutableInteractionSource()
                    }),
                backgroundColor = HhfTheme.colors.themeColor,
                indicator = {
                    TabRowDefaults.Indicator(
                        Modifier
                            .pagerTabIndicatorOffsetH(
                                pagerState,
                                it,
                                24.dp
                            )
                            .padding(bottom = 5.dp),
                        2.dp,
                        Color.White
                    )
                },
                edgePadding = 1.dp
            ) {
                ProjectTabs(viewModel, pagerState)
            }
    } else {
        if (viewModel.viewStates.isShowError) {
            ErrorBox(title = stringResource(id = R.string.no_network)) {
                viewModel.dispatch(ProjectAction.GetTabList)
            }
        } else {
            BoxProgress()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProjectTabs(viewModel: ProjectViewModel, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember{MutableInteractionSource()}
    viewModel.viewStates.tabList.forEachIndexed { index, ProjectTab ->
        Tab(
            text = {
                Text(ProjectTab.name,fontSize = if(pagerState.currentPage == index)16.sp else 12.sp
                )
            },
            selected = pagerState.currentPage == index,
            onClick = {
                coroutineScope.launch { pagerState.animateScrollToPage(index) }
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.White,
            interactionSource = interactionSource
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProjectContent(viewModel: ProjectViewModel, pagerState: PagerState) {
    if (viewModel.viewStates.tabList.isNotEmpty()) {
        HorizontalPager(count = viewModel.viewStates.tabList.size, state = pagerState) { page ->
            when (page) {
                viewModel.viewStates.tabList[page].pageId -> {
                    ProjectList(viewModel, viewModel.viewStates.tabList[page].id)
                }
            }
        }
    }
}

@Composable
fun ProjectList(viewModel: ProjectViewModel, page: Int) {
    val list = viewModel.getList(page).collectAsLazyPagingItems()
    SwipeRefresh(state = rememberSwipeRefreshState(viewModel.viewStates.isRefresh), onRefresh = {
        list.refresh()
    }) {
        PagingItem(list = list, errorBlock = {
            viewModel.dispatch(ProjectAction.SetRefresh(false))
        }, successBlock = {
            viewModel.dispatch(ProjectAction.SetRefresh(false))
        }, errorAndSuccessClick = {
            list.refresh()
        }) {
            viewModel.dispatch(ProjectAction.SetRefresh(false))
            LazyColumn {
                items(it) { homeBean ->
                    homeBean?.apply {
                        HomeListItem(homeBean = this, isShowLabel = false) {
                            if (CacheUtils.isLogin) {
                                viewModel.dispatch(ProjectAction.Collect(homeBean, it))
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

