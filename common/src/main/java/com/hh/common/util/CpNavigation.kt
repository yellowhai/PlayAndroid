package com.hh.common.util

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.navigation.*
import com.hh.common.bean.ModelPath

/**
 * @ProjectName: CBook
 * @Package: com.hh.cbook.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/27  10:06
 */
object CpNavigation {
    var currentScreen: ModelPath? by mutableStateOf(ModelPath.Main)
    val navList = mutableStateListOf(currentScreen)

    @SuppressLint("StaticFieldLeak")
    lateinit var navHostController: NavHostController

    /**
     * 跳转到某个页面
     */
    fun to(screenName: ModelPath) {
        currentScreen = screenName
        navHostController.navigate(screenName.route)
        navList.add(screenName)
    }

    /**
     * 跳转到某个页面带参数
     */
    fun toBundle(screenName: ModelPath, bundle: Bundle) {
        currentScreen = screenName
        navHostController.navigate(screenName.route, bundle)
        navList.add(screenName)
    }

    private fun NavController.navigate(
        route: String,
        args: Bundle,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
    ) {
        val routeLink = NavDeepLinkRequest.Builder.Companion.fromUri(
            NavDestination.Companion.createRoute(route).toUri())
            .build()

        val deepLinkMatch = graph.matchDeepLink(routeLink)
        if (deepLinkMatch != null) {
            val destination = deepLinkMatch.destination
            val id = destination.id
            navigate(id, args, navOptions, navigatorExtras)
        } else {
            navigate(route, navOptions, navigatorExtras)
        }
    }

    //    /**
//     * 返回到上一页
//     */
    fun backAndReturnsIsLastPage(): Boolean {
        return if (navList.size == 1) {
            //当前是最后一页了，返回true
            true
        } else {
            navList.removeLast()
            currentScreen = navList.last()
            navHostController.navigateUp()
            false
        }
    }
}
