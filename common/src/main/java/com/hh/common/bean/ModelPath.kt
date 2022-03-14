package com.hh.common.bean

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/5  9:42
 */
sealed class ModelPath(val route: String) {
    object Main : ModelPath("main")
    object Setting : ModelPath("setting")
    object Login : ModelPath("login")
    object Register : ModelPath("register")
    object Integral : ModelPath("integral")
    object Collect : ModelPath("collect")
    object WebView : ModelPath("webView")
    object Todo : ModelPath("todo")
    object TodoAdd : ModelPath("todoAdd")
    object Search : ModelPath("search")
    object SearchResult : ModelPath("searchResult")
    object Share : ModelPath("share")
    object ShareAdd : ModelPath("shareAdd")
}