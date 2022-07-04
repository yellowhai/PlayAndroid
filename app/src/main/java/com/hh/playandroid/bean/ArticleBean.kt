package com.hh.playandroid.bean

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/7  13:48
 */
data class ArticleBean (
    var apkLink: String,
    var author: String,
    var chapterId: Int,
    var chapterName: String,
    var collect: Boolean,
    var courseId: Int,
    var desc: String,
    var envelopePic: String,
    var fresh: Boolean,
    var id: Int,
    var link: String,
    var niceDate: String,
    var origin: String,
    var prefix: String,
    var projectLink: String,
    var publishTime: Long,
    var superChapterId: Int,
    var superChapterName: String,
    var shareUser: String,
    var tags: List<Tag>,
    var title: String,
    var type: Int,
    var userId: Int,
    var visible: Int,
    var zan: Int
        )

data class Tag(
    val name: String,
    val url: String
)