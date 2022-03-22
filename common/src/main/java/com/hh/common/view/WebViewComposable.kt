package com.hh.common.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.google.accompanist.insets.ui.TopAppBar
import com.hh.common.R
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CacheUtils
import com.hh.common.util.CpNavigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/2  13:33
 */

const val webTitle = "webTitle"
const val webUrl = "webUrl"
const val webIsCollect = "webIsCollect"
const val webCollectId = "webCollectId"
const val webCollectType = "webCollectType"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HhfWebView(
    modifier: Modifier = Modifier,
    title: String,
    url: String,
    isCollect: Boolean = false,
    collectId: Int = 0,
    collectType: Int = 0
) {
    val state = rememberWebViewState(url)
    val viewModel: WebViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.dispatch(
            WebAction.WebBean(
                title = title,
                webUrl = url,
                isCollect = isCollect,
                collectId = collectId,
                collectType = collectType
            )
        )
    }
    Column(modifier.background(HhfTheme.colors.background)) {
        TopAppBar(
            {
                MarqueeText(
                    title.replace("<em class='highlight'>","").replace("</em>",""),
                    Modifier,
                    color = Color.White,
                    softWrap = false
                )
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = HhfTheme.colors.themeColor,
            contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars),
            navigationIcon =
            {
                IconButton(
                    onClick = {
                        CpNavigation.backAndReturnsIsLastPage()
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                }
            },
            actions = {
                Column {
                    IconButton(onClick = {
                        viewModel.dispatch(WebAction.ChangePopup)
                    }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "", tint = Color.White)
                    }
                    WebPopup(viewModel = viewModel)
                }
            },
            elevation = 2.dp,
        )
//        val progress: Int by progressFlow.collectAsState(initial = 0)
//        LinearProgressIndicator(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(8.dp),
//            progress = progress / 100f,
//            backgroundColor = Color(0xff2196F3)
//        )
        BoxWithConstraints(Modifier.weight(1f)) {
            WebView(
                state,
                Modifier.size(maxWidth, maxHeight),
                onCreated = {
                    it.settings.javaScriptEnabled = true
                    it.setBackgroundColor(0)
                }
            )
            if (state.isLoading) {
                BoxProgress()
            }
        }

    }
}
private val progressFlow by lazy {
    flow {
        repeat(100) {
            emit(it + 1)
            delay(50)
        }
    }
}

@Composable
fun WebPopup(modifier: Modifier = Modifier, viewModel: WebViewModel) {
    val context = LocalContext.current
    DropdownMenu(viewModel.viewStates.isShowPopup, {
        viewModel.dispatch(WebAction.ChangePopup)
    }, modifier) {
        DropdownMenuItem({
            viewModel.dispatch(WebAction.ChangePopup)
            Intent().run {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT, context.getString(
                        R.string.share_article_url,
                        context.getString(R.string.app_name),
                        viewModel.viewStates.title,
                        viewModel.viewStates.webUrl
                    )
                )
                type = "text/plain"
                context.startActivity(
                    Intent.createChooser(
                        this,
                        context.getString(R.string.webview_share)
                    )
                )
            }
        }) {
            Text(stringResource(R.string.webview_share))
        }
        DropdownMenuItem({
            viewModel.dispatch(WebAction.ChangePopup)
            if (CacheUtils.isLogin) {
                viewModel.dispatch(WebAction.WebCollect)
            } else {
                CpNavigation.to(ModelPath.Login)
            }
        }) {
            Text(
                if (viewModel.viewStates.isCollect) stringResource(R.string.webview_collect_cancel)
                else stringResource(R.string.webview_collect)
            )
        }
        DropdownMenuItem({
            viewModel.dispatch(WebAction.ChangePopup)
            Intent().run {
                action = "android.intent.action.VIEW"
                data = Uri.parse(viewModel.viewStates.webUrl)
                context.startActivity(this)
            }
        }) {
            Text(stringResource(R.string.webview_browser))
        }
    }
}