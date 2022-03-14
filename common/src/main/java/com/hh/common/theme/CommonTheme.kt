package com.hh.common.theme

/**
 * @ProjectName: playandroid
 * @Package: com.hh.common.theme
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/12/31  15:25
 */
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val LightColorPalette = HhfColors(
    bottomBar = white1,
    themeColor =  CommonPurple500,
    textColor = black3,
    background = white2,
    listItem = white,
    divider = white3,
    textFieldBackground = white,
)

private val DarkColorPalette = HhfColors(
    bottomBar = black1,
    themeColor =  black2,
    textColor = white3,
    background = black4,
    listItem = black5,
    divider = black4,
    textFieldBackground = black7,
)

private val LocalHhfColors = compositionLocalOf {
    LightColorPalette
}


@Stable
class HhfColors(themeColor: Color,textColor : Color = black4,
                textInactiveColor:Color = grey2,
                background: Color = white2,
                listItem : Color = white,
                bottomBar : Color = white1,
                divider : Color = white3,
                textFieldBackground: Color = white,
                ){
    var background: Color by mutableStateOf(background)
        private set
    var themeColor: Color by mutableStateOf(themeColor)
    var textColor: Color by mutableStateOf(textColor)
        private set
    var textInactiveColor : Color by mutableStateOf(textInactiveColor)
        private set
    var listItem : Color by mutableStateOf(listItem)
        private set
    var bottomBar : Color by mutableStateOf(bottomBar)
        private set
    var divider : Color by mutableStateOf(divider)
        private set
    var textFieldBackground : Color by mutableStateOf(textFieldBackground)
        private set
}


@Stable
object HhfTheme {
    val colors: HhfColors
        @Composable
        get() = LocalHhfColors.current

    enum class Theme {
        Light, Dark
    }
}



@Composable
fun HhfTheme(
    theme: HhfTheme.Theme = HhfTheme.Theme.Light,
    colorTheme: Color? = null,
    content: @Composable () -> Unit
) {
    val targetColors = if (theme == HhfTheme.Theme.Dark) {
        colorTheme?.let {
            rememberSystemUiController().setNavigationBarColor(colorTheme)
            DarkColorPalette.apply {
                themeColor = it
            }
        }?:DarkColorPalette
    } else {
        colorTheme?.let {
            rememberSystemUiController().setNavigationBarColor(colorTheme)
            LightColorPalette.apply {
                themeColor = it
            }
        }?:LightColorPalette
    }
    val themeColor = animateColorAsState(targetColors.themeColor, TweenSpec(600))
    val textColor = animateColorAsState(targetColors.textColor, TweenSpec(600))
    val background = animateColorAsState(targetColors.background,TweenSpec(600))
    val listItem =  animateColorAsState(targetColors.listItem,TweenSpec(600))
    val bottomBar =  animateColorAsState(targetColors.bottomBar,TweenSpec(600))
    val divider =  animateColorAsState(targetColors.divider,TweenSpec(600))
    val textFieldBackground =  animateColorAsState(targetColors.textFieldBackground,TweenSpec(600))
    val colors = HhfColors(themeColor.value,
        textColor.value,
        background = background.value,
        listItem = listItem.value,
        bottomBar = bottomBar.value,
        divider = divider.value,
        textFieldBackground = textFieldBackground.value,
        )
    CompositionLocalProvider(LocalHhfColors provides colors) {
        MaterialTheme(
            shapes = Shapes,
            typography = typography
        ) {
            ProvideWindowInsets(content = content)
        }
    }
}