package com.hh.common.ext

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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