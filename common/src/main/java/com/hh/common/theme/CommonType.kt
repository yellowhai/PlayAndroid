package com.hh.common.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.hh.common.R

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/10  16:37
 */
private val dmSans = FontFamily(
    Font(R.font.dmsansregular),
    Font(R.font.dmsansmedium, FontWeight.W500),
    Font(R.font.dmsansbold, FontWeight.Bold)
)


val typography = Typography(defaultFontFamily = dmSans)