package com.hh.common.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CacheUtils
import com.hh.common.util.CpNavigation
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.palette.BitmapPalette
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @ProjectName: playandroid
 * @Author: yshh
 * @CreateDate: 2021/12/31  13:59
 */


var appTheme by mutableStateOf(Color(CacheUtils.themeColor))

var isNight by mutableStateOf(CacheUtils.isNight)

@Composable
fun HhTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
) {
    TopAppBarSurface(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    ) {
        TopAppBarContent(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun TopAppBarSurface(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    content: @Composable () -> Unit,
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        modifier = modifier,
        content = content
    )
}

@Composable
fun TopAppBarContent(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = modifier
    )
}

@Composable
fun CpTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    title: String,
    back: (() -> Unit)? = null
) {
    HhTopAppBar(
        {
            Text(title, color = Color.White)
        },
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentPadding = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top).asPaddingValues(),
        navigationIcon = back?.run {
            {
                IconButton(
                    onClick = {
                        invoke()
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                }
            }
        },
        elevation = 2.dp,
    )
}

@Composable
fun CpTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    back: (() -> Unit)? = null
) {
    HhTopAppBar(
        {
            Text(title, color = Color.White)
        },
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentPadding = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top).asPaddingValues(),
        navigationIcon = back?.run {
            {
                IconButton(
                    onClick = {
                        invoke()
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                }
            }
        },
        actions = actions,
        elevation = 2.dp,
    )
}


@Composable
fun CpBottomBar(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top, content: @Composable RowScope.() -> Unit
) {
    Surface(
        elevation = 14.dp,
        color = MaterialTheme.colors.surface, // color will be adjusted for elevation
    ) {
        Row(
            modifier,
            content = content,
            verticalAlignment = verticalAlignment,
            horizontalArrangement = horizontalArrangement
        )
    }
}

@Composable
fun ErrorBox(modifier: Modifier = Modifier, title: String) {
    Box(
        modifier, Alignment.Center
    ) {
        Text(title, color = HhfTheme.colors.textColor)
    }
}

@Composable
fun ErrorBox(modifier: Modifier = Modifier, title: String, block: (() -> Unit)? = null) {
    Box(
        modifier, Alignment.Center
    ) {
        Column(block?.run { Modifier.clickable { block.invoke() } } ?: Modifier,horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Filled.ErrorOutline, contentDescription = "",Modifier.size(120.dp,95.dp),tint = HhfTheme.colors.themeColor)
            Text(
                title,
                color = HhfTheme.colors.textColor,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun ErrorBoxSelectionContainer(modifier: Modifier = Modifier, title: String) {
    Box(
        modifier, Alignment.Center
    ) {
        SelectionContainer {
            Text(title)
        }
    }
}

@Composable
fun NetworkImage(
    url: Any,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    bitmapPalette: BitmapPalette? = null,
    highlightColor: Color = HhfTheme.colors.themeColor,
    colorFilter : ColorFilter? = ColorFilter.tint(HhfTheme.colors.themeColor),
    @DrawableRes defaultImg: Int? = null
) {
    CoilImage(
        url,
        modifier = modifier,
        contentScale = contentScale,
        circularReveal = CircularReveal(duration = 250),
        bitmapPalette = bitmapPalette,
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colors.background,
            highlightColor = highlightColor,
            dropOff = 0.65f
        ),
        failure = {
            defaultImg?.apply {
                Image(
                    painter = painterResource(this),
                    contentDescription = "movie Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = modifier.fillMaxSize(),
                    colorFilter = colorFilter
                )
            }
        }
    )
}

@Composable
fun <T> Flow<T>.flowWithLifecycleStateInAndCollectAsState(
    scope: CoroutineScope,
    initial: T? = null,
    context: CoroutineContext = EmptyCoroutineContext,
): State<T?> {
    val lifecycleOwner = LocalLifecycleOwner.current
    return remember(this, lifecycleOwner) {
        this
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = initial
            )
    }.collectAsState(context)
}

@Stable
class QureytoImageShapes(var hudu: Float = 100f, var controller: Float = 0f) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, size.height - hudu)
        //默认初始化选择中间作为控制点坐标x的数值
        if (controller == 0f) {
            controller = size.width / 2f
        }
        path.quadraticBezierTo(controller, size.height, size.width, size.height - hudu)
        path.lineTo(size.width, 0f)
        path.close()
        return Outline.Generic(path)
    }
}



@Composable
fun ColumnTopBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalAlignment: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier.navigationBarsPadding(),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalAlignment
    ) {
        CpTopBar(
            Modifier.fillMaxWidth(),
            HhfTheme.colors.themeColor,
            title = title,
            actions = actions,
        ) {
            CpNavigation.backAndReturnsIsLastPage()
        }
        content.invoke(this)
    }
}

@Composable
fun ColumnTopBarMain(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier.navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CpTopBar(
            Modifier.fillMaxWidth(),
            HhfTheme.colors.themeColor,
            title = title,
            actions = {
                IconButton(onClick = {
                    CpNavigation.to(ModelPath.Search)
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "search", tint = Color.White)
                }
            },
        )
        content.invoke(this)
    }
}