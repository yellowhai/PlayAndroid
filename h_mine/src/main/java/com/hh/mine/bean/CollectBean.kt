package com.hh.mine.bean

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/25  14:01
 */

data class CollectUrl(var icon: String,
                              var id: Int,
                              var link: String,
                              var name: String,
                              var order: Int,
                              var userId: Int,
                              var visible: Int)

data class CollectInside(var chapterId: Int,
                           var author: String,
                           var chapterName: String,
                           var courseId: Int,
                           var desc: String,
                           var envelopePic: String,
                           var id: Int,
                           var link: String,
                           var niceDate: String,
                           var origin: String,
                           var originId: Int,
                           var publishTime: Long,
                           var title: String,
                           var userId: Int,
                           var visible: Int,
                           var zan: Int)