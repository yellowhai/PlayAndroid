package com.hh.common.view

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
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
import kotlin.math.absoluteValue

/**
 * @ProjectName: playandroid
 * @Package: com.hh.common.view
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/12/31  13:59
 */


var appTheme by mutableStateOf(Color(CacheUtils.themeColor))

var isNight by mutableStateOf(CacheUtils.isNight)

@Composable
fun CpTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    title: String,
    back: (() -> Unit)? = null
) {
    TopAppBar(
        {
            Text(title, color = Color.White)
        },
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars),
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
    TopAppBar(
        {
            Text(title, color = Color.White)
        },
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars),
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
                    colorFilter = ColorFilter.tint(HhfTheme.colors.themeColor)
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