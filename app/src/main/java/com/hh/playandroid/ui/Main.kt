package com.hh.playandroid.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hh.common.base.YshhApplication
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CacheUtils.isLogin
import com.hh.common.util.CpNavigation
import com.hh.common.util.stringResource
import com.hh.common.view.ErrorBox
import com.hh.playandroid.util.reenableScrolling
import com.hh.mine.ui.Mine
import com.hh.playandroid.R
import com.hh.playandroid.bean.BottomBean
import com.hh.playandroid.bean.DashboardState
import com.hh.playandroid.ui.account.AccountView
import com.hh.playandroid.ui.home.HomeView
import com.hh.playandroid.ui.project.ProjectView
import com.hh.playandroid.ui.square.SquareView

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/22  10:35
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    var bottomSwitch by remember { mutableStateOf(true) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // called when you scroll the content
                bottomSwitch = available.y > 0
                return super.onPreScroll(available, source)
            }
        }
    }
    Scaffold(bottomBar = {
        AnimatedVisibility(
            visible = bottomSwitch,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            MainBottomBar(Modifier.fillMaxWidth().height(IntrinsicSize.Max),pagerState = pagerState) {
                pagerState.reenableScrolling(coroutineScope, it)
            }
        }
    }) {
        Column(
            modifier
                .fillMaxSize()
                .navigationBarsPadding(bottomSwitch)
//                .nestedScroll(nestedScrollConnection)
        ) {
            HorizontalPager(
                count = bottomList.size,
                modifier.weight(1f), pagerState, userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> HomeView(
                        Modifier
                            .fillMaxSize()
                            .background(HhfTheme.colors.background)
                            .align(Alignment.CenterHorizontally)
                    )
                    1 -> ProjectView(
                        Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally)
                    )
                    2 -> AccountView(
                        Modifier
                            .fillMaxSize()
                            .background(HhfTheme.colors.background)
                            .align(Alignment.CenterHorizontally)
                    )
                    3 -> {
                        if (isLogin) {
                            Mine(
                                Modifier
                                    .fillMaxSize()
                                    .background(HhfTheme.colors.background)
                                    .align(Alignment.CenterHorizontally)
                            )
                        } else {
                            ErrorBox(
                                Modifier.fillMaxSize(),
                                title = stringResource(id = R.string.sign_in)
                            )
                            { CpNavigation.to(ModelPath.Login) }
                        }
                    }
                }
            }
        }
    }
}

val bottomList = listOf(
    BottomBean(
        DashboardState.Home,
        YshhApplication.context.stringResource(R.string.main_title_home)
    ),
    BottomBean(
        DashboardState.Project,
        YshhApplication.context.stringResource(R.string.main_title_project)
    ),
    BottomBean(
        DashboardState.PubAccount,
        YshhApplication.context.stringResource(R.string.main_title_account)
    ),
    BottomBean(
        DashboardState.Mine,
        YshhApplication.context.stringResource(R.string.main_title_mine)
    )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MainBottomBar(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    currentChanged: (Int) -> Unit
) {
    BottomAppBar(
        modifier.navigationBarsPadding(),
        cutoutShape = CircleShape,
        backgroundColor = HhfTheme.colors.bottomBar,
        elevation = 5.dp
    ) {
        bottomList.forEachIndexed { index, item ->
            BottomNavigationItem(
                selected = pagerState.currentPage == index, onClick = {
                    currentChanged(index)
                }, icon = {
                    Icon(
                        item.dashboardState.icon,
                        contentDescription = item.name,
                        tint = if (pagerState.currentPage == index) {
                            HhfTheme.colors.themeColor
                        } else {
                            LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                        }, modifier = Modifier.size(24.dp)
                    )
                }, label = {
                    Text(
                        text = item.name,
                        color = if (pagerState.currentPage == index) {
                            HhfTheme.colors.themeColor
                        } else {
                            LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                        },
                        fontSize = 12.sp
                    )
                },
                unselectedContentColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                selectedContentColor = HhfTheme.colors.themeColor,
                alwaysShowLabel = false

            )
        }
    }
}