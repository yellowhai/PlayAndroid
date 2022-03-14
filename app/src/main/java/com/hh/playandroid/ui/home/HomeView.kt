package com.hh.playandroid.ui.home

import android.os.Bundle
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.hh.common.R
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CacheUtils
import com.hh.common.util.CpNavigation
import com.hh.common.util.logE
import com.hh.common.view.*
import com.hh.playandroid.bean.BannerResponse
import kotlinx.coroutines.delay

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/22  19:35
 */
@Composable
fun HomeView(modifier: Modifier = Modifier) {
    "HomeView".logE()
    val viewModel: HomeViewModel = viewModel()
    ColumnTopBarMain(modifier
        .background(HhfTheme.colors.background),
        stringResource(R.string.app_name)) {
        HomeContent(viewModel = viewModel)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerPager(modifier: Modifier = Modifier, viewModel: HomeViewModel) {
    "BannerPager".logE()
    val pagerState = rememberPagerState()
    LaunchedEffect(viewModel){
        viewModel.dispatch(HomeAction.GetBanner)
    }
    BoxWithConstraints(modifier) {
        HorizontalPager(count = viewModel.viewStates.bannerList.size, state = pagerState) { page ->
            when (page) {
                viewModel.viewStates.bannerList[page].id -> {
                    BannerItem(
                        Modifier
                            .height(maxHeight)
                            .fillMaxWidth(), viewModel.viewStates.bannerList[page]
                    )
                }
            }
        }
        if (viewModel.viewStates.bannerList.isNotEmpty()) {
            BannerIndicator(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(0.2f))
                    .padding(8.dp),viewModel,pagerState )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerIndicator(modifier: Modifier = Modifier,viewModel: HomeViewModel,pagerState : PagerState) {
    "BannerIndicator".logE()
    LaunchedEffect(viewModel){
        while (true) {
            delay(3000L)
            if (pagerState.currentPage < viewModel.viewStates.bannerList.size -1) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            } else {
                pagerState.scrollToPage(0)
            }
        }
    }
    Row(modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            viewModel.viewStates.bannerList[pagerState.currentPage].title,
            Modifier,
            Color.White.copy(0.8f),
            14.sp
        )
        Row(
            Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.End)
        ) {
            repeat(viewModel.viewStates.bannerList.size) {
                Canvas(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .size(8.dp)
                ) {
                    if (it == pagerState.currentPage) {
                        drawCircle(appTheme)
                    } else {
                        drawCircle(Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun BannerItem(modifier: Modifier = Modifier, data: BannerResponse) {
    data.apply {
        Box(modifier.clickable {
            CpNavigation.toBundle(ModelPath.WebView, Bundle().apply {
                putString(webTitle, data.title)
                putString(webUrl, data.url)
                putBoolean(webIsCollect, false)
                putInt(webCollectId, data.id)
                putInt(webCollectType, 1)
            })
        }) {
            NetworkImage(
                imagePath,
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillBounds,
                defaultImg = R.mipmap.ic_default_round
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(modifier: Modifier = Modifier, viewModel: HomeViewModel) {
    val list = viewModel.viewStates.homeList.collectAsLazyPagingItems()
    "HomeContent".logE()
    SwipeRefresh(
        state = rememberSwipeRefreshState(viewModel.viewStates.isRefresh),
        onRefresh = {
            viewModel.dispatch(HomeAction.GetBanner)
            list.refresh()
        }) {
        PagingItem(modifier, list = list,
            errorBlock = {},
            successBlock = {},
            errorAndSuccessClick = {
                list.refresh()
            }) {
            LazyColumn{
                item {
                    BannerPager(Modifier.height(160.dp), viewModel = viewModel)
                }
                items(it) { homeBean ->
                    homeBean?.apply {
                        HomeListItem(homeBean = this) {
                            if(CacheUtils.isLogin){
                                viewModel.dispatch(HomeAction.Collect(homeBean.id, it))
                            }
                            else{
                                CpNavigation.to(ModelPath.Login)
                            }
                        }
                    }
                }
            }
        }
    }
}
