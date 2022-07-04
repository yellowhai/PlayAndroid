package com.hh.mine.bean

import java.io.Serializable

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/3  11:19
 */

data class TodoBean(
    var completeDate: Long,
    var completeDateStr: String,
    var content: String,
    var date: Long,
    var dateStr: String,
    var id: Int,
    var priority: Int,
    var status: Int,
    var title: String,
    var type: Int,
    var userId: Int
) : Serializable