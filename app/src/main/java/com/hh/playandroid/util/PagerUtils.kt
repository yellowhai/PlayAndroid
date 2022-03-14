package com.hh.playandroid.util

import androidx.compose.foundation.MutatePriority
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.hh.common.util.logW
import kotlinx.coroutines.*

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/6  15:27
 */

/**
 * 设置pager可滚动并滑动目标位置
 */
@OptIn(ExperimentalPagerApi::class)
fun PagerState.reenableScrolling(scope: CoroutineScope, page: Int) {
    scope.launch {
        withContext(Dispatchers.IO) {
            scroll(scrollPriority = MutatePriority.PreventUserInput) {
                // Do nothing, just cancel the previous indefinite "scroll"
                "reenableScrolling".logW("HHLog")
            }
        }
        scrollToPage(page)
    }
}

/**
 * 设置pager可滚动
 */
@OptIn(ExperimentalPagerApi::class)
fun PagerState.reenableScrolling(scope: CoroutineScope) {
    scope.launch {
        scroll(scrollPriority = MutatePriority.PreventUserInput) {
            // Do nothing, just cancel the previous indefinite "scroll"
            "reenableScrolling".logW("HHLog")
        }
    }
}

/**
 * 设置pager不可滚动
 */
@OptIn(ExperimentalPagerApi::class)
fun PagerState.disableScrolling(scope: CoroutineScope) {
    scope.launch {
        scroll(scrollPriority = MutatePriority.PreventUserInput) {
            // Await indefinitely, blocking scrolls
            "disableScrolling".logW("HHLog")
            awaitCancellation()
        }
    }
}