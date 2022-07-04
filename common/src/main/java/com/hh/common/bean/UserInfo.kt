package com.hh.common.bean


/**
 * wanAndroid api
 */
data class UserInfo(
    var id: Int,
    var admin: Boolean,
    var chapterTops: MutableList<Int>,
    var coinCount: Int,
    var collectIds: MutableList<Int>,
    var email: String,
    var icon: String,
    var nickname: String,
    var password: String,
    var token: String,
    var type: Int,
    var username: String,
)

data class Integral(
    var coinCount: Int,//当前积分
    var rank: Int,
    var userId: Int,
    var username: String)

data class IntegralHistory(
    var coinCount: Int,
    var date: Long,
    var desc: String,
    var id: Int,
    var type: Int,
    var reason: String,
    var userId: Int,
    var userName: String)