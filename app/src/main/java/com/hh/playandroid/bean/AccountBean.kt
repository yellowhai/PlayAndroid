package com.hh.playandroid.bean

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/9  8:25
 */
data class Tabs(var children: List<String> = listOf(),
                var courseId: Int = 0,
                var id: Int = 0,
                var name: String = "",
                var order: Int = 0,
                var parentChapterId: Int = 0,
                var userControlSetTop: Boolean = false,
                var visible: Int = 0,
                var pageId : Int)