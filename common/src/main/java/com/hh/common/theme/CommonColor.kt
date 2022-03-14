package com.hh.common.theme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
/**
 * @ProjectName: playandroid
 * @Package: com.hh.common.theme
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/12/30  13:34
 */


val CommonPurple200 = Color(0xFFBB86FC)
val CommonPurple500 = Color(0xFF6200EE)
val CommonPurple700 = Color(0xFF3700B3)
val CommonTeal200 = Color(0xFF03DAC5)

//自定义颜色，过滤掉与白色相近的颜色
var ACCENT_COLORS = listOf(
    Color(0xFFEF5350),
    Color(0xFFEC407A),
    Color(0xFFAB47BC),
    Color(0xFF7E57C2),
    Color(0xFF5C6BC0),
    Color(0xFF42A5F5),
    Color(0xFF29B6F6),
    Color(0xFF26C6DA),
    Color(0xFF26A69A),
    Color(0xFF66BB6A),
    Color(0xFF9CCC65),
    Color(0xFFD4E157),
    Color(0xFFFFEE58),
    Color(0xFFFFCA28),
    Color(0xFFFFA726),
    Color(0xFFFF7043),
    Color(0xFF8D6E63),
    Color(0xFFBDBDBD),
    Color(0xFF78909C),
    CommonPurple200,
    CommonPurple500,
    CommonPurple700,
    CommonTeal200
)

val PRIMARY_COLORS_SUB = listOf(
    listOf(
        Color(0xFFEF5350),
        Color(0xFFF44336),
        Color(0xFFE53935),
        Color(0xFFD32F2F),
        Color(0xFFC62828),
        Color(0xFFB71C1C)
    ),
    listOf(
        Color(0xFFEC407A),
        Color(0xFFE91E63),
        Color(0xFFD81B60),
        Color(0xFFC2185B),
        Color(0xFFAD1457),
        Color(0xFF880E4F)
    ),
    listOf(
        Color(0xFFAB47BC),
        Color(0xFF9C27B0),
        Color(0xFF8E24AA),
        Color(0xFF7B1FA2),
        Color(0xFF6A1B9A),
        Color(0xFF4A148C)
    ),
    listOf(
        Color(0xFF7E57C2),
        Color(0xFF673AB7),
        Color(0xFF5E35B1),
        Color(0xFF512DA8),
        Color(0xFF4527A0),
        Color(0xFF311B92)
    ),
    listOf(
        Color(0xFF5C6BC0),
        Color(0xFF3F51B5),
        Color(0xFF3949AB),
        Color(0xFF303F9F),
        Color(0xFF283593),
        Color(0xFF1A237E)
    ),
    listOf(
        Color(0xFF42A5F5),
        Color(0xFF2196F3),
        Color(0xFF1E88E5),
        Color(0xFF1976D2),
        Color(0xFF1565C0),
        Color(0xFF0D47A1)
    ),
    listOf(
        Color(0xFF29B6F6),
        Color(0xFF03A9F4),
        Color(0xFF039BE5),
        Color(0xFF0288D1),
        Color(0xFF0277BD),
        Color(0xFF01579B)
    ),
    listOf(
        Color(0xFF26C6DA),
        Color(0xFF00BCD4),
        Color(0xFF00ACC1),
        Color(0xFF0097A7),
        Color(0xFF00838F),
        Color(0xFF006064)
    ),
    listOf(
        Color(0xFF26A69A),
        Color(0xFF009688),
        Color(0xFF00897B),
        Color(0xFF00796B),
        Color(0xFF00695C),
        Color(0xFF004D40)
    ),
    listOf(
        Color(0xFF66BB6A),
        Color(0xFF4CAF50),
        Color(0xFF43A047),
        Color(0xFF388E3C),
        Color(0xFF2E7D32),
        Color(0xFF1B5E20)
    ),
    listOf(
        Color(0xFF9CCC65),
        Color(0xFF8BC34A),
        Color(0xFF7CB342),
        Color(0xFF689F38),
        Color(0xFF558B2F),
        Color(0xFF33691E)
    ),
    listOf(
        Color(0xFFD4E157),
        Color(0xFFCDDC39),
        Color(0xFFC0CA33),
        Color(0xFFAFB42B),
        Color(0xFF9E9D24),
        Color(0xFF827717)
    ),
    listOf(
        Color(0xFFFFEE58),
        Color(0xFFFFEB3B),
        Color(0xFFFDD835),
        Color(0xFFFBC02D),
        Color(0xFFF9A825),
        Color(0xFFF57F17)
    ),
    listOf(
        Color(0xFFFFCA28),
        Color(0xFFFFC107),
        Color(0xFFFFB300),
        Color(0xFFFFA000),
        Color(0xFFFF8F00),
        Color(0xFFFF6F00)
    ),
    listOf(
        Color(0xFFFFA726),
        Color(0xFFFF9800),
        Color(0xFFFB8C00),
        Color(0xFFF57C00),
        Color(0xFFEF6C00),
        Color(0xFFE65100)
    ),
    listOf(
        Color(0xFFFF7043),
        Color(0xFFFF5722),
        Color(0xFFF4511E),
        Color(0xFFE64A19),
        Color(0xFFD84315),
        Color(0xFFBF360C)
    ),
    listOf(
        Color(0xFF8D6E63),
        Color(0xFF795548),
        Color(0xFF6D4C41),
        Color(0xFF5D4037),
        Color(0xFF4E342E),
        Color(0xFF3E2723)
    ),
    listOf(
        Color(0xFFBDBDBD),
        Color(0xFF9E9E9E),
        Color(0xFF757575),
        Color(0xFF616161),
        Color(0xFF424242),
        Color(0xFF212121)
    ),
    listOf(
        Color(0xFF78909C),
        Color(0xFF607D8B),
        Color(0xFF546E7A),
        Color(0xFF455A64),
        Color(0xFF37474F),
        Color(0xFF263238)
    ),
    listOf(
        CommonPurple200
    ),
    listOf(
        CommonPurple500
    ),
    listOf(
        CommonPurple700
    ),
    listOf(
        CommonTeal200
    )
)


val white = Color(0xFFFFFFFF)
val white1 = Color(0xFFF7F7F7)
val white2 = Color(0xFFEDEDED)
val white3 = Color(0xFFE5E5E5)
val white4 = Color(0xFFD5D5D5)
val white5 = Color(0xFFCCCCCC)
val black = Color(0xFF000000)
val black1 = Color(0xFF1E1E1E)
val black2 = Color(0xFF111111)
val black3 = Color(0xFF191919)
val black4 = Color(0xFF252525)
val black5 = Color(0xFF2C2C2C)
val black6 = Color(0xFF07130A)
val black7 = Color(0xFF292929)
val grey1 = Color(0xFF888888)
val grey2 = Color(0xFFCCC7BF)
val grey3 = Color(0xFF767676)
val grey4 = Color(0xFFB2B2B2)
val grey5 = Color(0xFF5E5E5E)
val green1 = Color(0xFFB0EB6E)
val green2 = Color(0xFF6DB476)
val green3 = Color(0xFF67BF63)
val red1 = Color(0xFFDF5554)
val red2 = Color(0xFFDD302E)
val red3 = Color(0xFFF77B7A)
val red4 = Color(0xFFD42220)
val red5 = Color(0xFFC51614)
val red6 = Color(0xFFF74D4B)
val red7 = Color(0xFFDC514E)
val red8 = Color(0xFFCBC7BF)
val yellow1 = Color(0xFFF6CA23)