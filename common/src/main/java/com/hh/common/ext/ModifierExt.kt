package com.hh.common.ext

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.TabPosition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/5  14:50
 */

/**
 * 增加消息未读小红点
 */
fun Modifier.rightRead(read: Boolean, badgeColor: Color) = this
    .drawWithContent {
        drawContent()
        if (!read) {
            drawCircle(
                color = badgeColor,
                radius = 5.dp.toPx(),
                center = Offset(size.width - 1.dp.toPx(), 1.dp.toPx()),
            )
        }
}

/**
 * 增加消息未读小红点
 */
fun Modifier.rightReadCircle(read: Boolean, badgeColor: Color) = this
    .drawWithContent {
        drawContent()
        if (!read) {
            drawCircle(
                color = badgeColor,
                radius = 5.dp.toPx(),
                center = Offset(size.width - 7.dp.toPx(), 7.dp.toPx()),
            )
        }
    }


@ExperimentalPagerApi
fun Modifier.pagerTabIndicatorOffsetH(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    width: Dp = 1.dp
): Modifier = composed {
    // If there are no pages, nothing to show
    if (pagerState.pageCount == 0) return@composed this

    val targetIndicatorOffset: Dp

    val currentTab = tabPositions[pagerState.currentPage]
    val targetPage = pagerState.targetPage
    val targetTab = targetPage.let { tabPositions.getOrNull(it) }

    if (targetTab != null) {
        // The distance between the target and current page. If the pager is animating over many
        // items this could be > 1
        val targetDistance = (targetPage - pagerState.currentPage).absoluteValue
        // Our normalized fraction over the target distance
        val fraction =
            (pagerState.currentPageOffset / kotlin.math.max(targetDistance, 1)).absoluteValue

        targetIndicatorOffset =
            androidx.compose.ui.unit.lerp(currentTab.left, targetTab.left, fraction)
    } else {
        // Otherwise we just use the current tab/page
        targetIndicatorOffset = currentTab.left
    }

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = targetIndicatorOffset + ((currentTab.width - width) / 2))
        .width(width)
}

fun Modifier.ownTabIndicatorOffset(
    currentTabPosition: TabPosition,
    currentTabWidth: Dp = currentTabPosition.width
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset + ((currentTabPosition.width - currentTabWidth) / 2))
        .width(currentTabWidth)
}

fun Modifier.onClick(
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
        properties["indication"] = indication
        properties["interactionSource"] = interactionSource
    }
) {
    val scope = rememberCoroutineScope()
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = {
            if(!isClick){
                isClick = true
                onClick.invoke()
                scope.launch {
                    delay(500L)
                    isClick = false
                }
            }
        },
        role = role,
        indication = indication,
        interactionSource = interactionSource
    )
}

private var isClick = false

fun Modifier.onClick(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    val scope = rememberCoroutineScope()
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = {
            if(!isClick){
                isClick = true
                onClick.invoke()
                scope.launch {
                    delay(500L)
                    isClick = false
                }
            }
        },
        role = role,
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() }
    )
}