package com.hh.playandroid.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CpNavigation
import com.hh.common.util.CpNavigation.navHostController
import com.hh.common.view.*
import com.hh.mine.bean.TodoBean
import com.hh.mine.ui.CpSetting
import com.hh.mine.ui.collect.CollectView
import com.hh.mine.ui.integral.IntegralView
import com.hh.mine.ui.share.ShareView
import com.hh.mine.ui.share.add.ShareAddView
import com.hh.mine.ui.todo.TodoView
import com.hh.mine.ui.todo.add.TodoAddView
import com.hh.mine.ui.todo.add.todoAddBean
import com.hh.playandroid.ui.login.LoginView
import com.hh.playandroid.ui.login.PASSWORD
import com.hh.playandroid.ui.login.RegisterView
import com.hh.playandroid.ui.login.USERNAME
import com.hh.playandroid.ui.search.SearchView
import com.hh.playandroid.ui.search.result.SearchResultView

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/22  19:31
 */

@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HhfNavigation() {
    AnimatedNavHost(navController = CpNavigation.navHostController,
        startDestination = ModelPath.Main.route,
        enterTransition = { fadeIn(animationSpec = tween(1000), initialAlpha = 0f) },
        exitTransition = { fadeOut(animationSpec = tween(1000), targetAlpha = 0f) }) {
        composable(ModelPath.Main.route) {
            MainContent()
        }
        composable(ModelPath.Setting.route) {
            CpSetting(Modifier.fillMaxSize())
        }
        composable(ModelPath.Login.route){
            val userName = navHostController.currentBackStackEntry?.arguments?.getString(USERNAME,"")
            val password = navHostController.currentBackStackEntry?.arguments?.getString(PASSWORD,"")
            LoginView(userName,password)
        }
        composable(ModelPath.Register.route){
            RegisterView()
        }
        composable(ModelPath.Integral.route){
            IntegralView()
        }
        composable(ModelPath.Collect.route){
            CollectView()
        }
        composable(ModelPath.WebView.route){
            val title =it.arguments?.getString(webTitle)
            val webUrl = it.arguments?.getString(webUrl)
            val isCollect =it.arguments?.getBoolean(webIsCollect)
            val collectId =it.arguments?.getInt(webCollectId)
            val collectType =it.arguments?.getInt(webCollectType)
            HhfWebView(Modifier.fillMaxSize().navigationBarsPadding(),
                title = title!!,
                url = webUrl!!,
                isCollect = isCollect!!,
                collectId = collectId!!,
                collectType = collectType!!
                )
        }
        composable(ModelPath.Todo.route){
            TodoView()
        }
        composable(ModelPath.TodoAdd.route){
            val todoBean = it.arguments?.getSerializable(todoAddBean) as? TodoBean
            TodoAddView(todoBean = todoBean)
        }
        composable(ModelPath.Search.route){
            SearchView(Modifier.fillMaxSize()
                .background(HhfTheme.colors.background)
                .navigationBarsPadding()
            )
        }
        composable(ModelPath.SearchResult.route){
            val searchName = it.arguments?.getString("searchName", "")
            SearchResultView(Modifier.fillMaxSize()
                .background(HhfTheme.colors.background),searchName!!
            )
        }
        composable(ModelPath.Share.route){
            ShareView()
        }
        composable(ModelPath.ShareAdd.route){
            ShareAddView()
        }
    }
}