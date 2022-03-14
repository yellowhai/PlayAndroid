package com.hh.playandroid.bean

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/7  10:08
 */
data class BannerResponse(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
)