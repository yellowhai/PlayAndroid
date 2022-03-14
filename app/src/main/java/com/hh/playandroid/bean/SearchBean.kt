package com.hh.playandroid.bean

import androidx.compose.ui.graphics.Color

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/10  20:20
 */
data class SearchResponse(var id: Int,
                          var link: String,
                          var name: String,
                          var order: Int,
                          var visible: Int,
                          var color : Color
                          )