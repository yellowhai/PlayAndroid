package com.hh.common.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
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
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.hh.common.R
import com.hh.common.bean.ModelPath
import com.hh.common.ext.filterHtml
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CacheUtils
import com.hh.common.util.CpNavigation
import com.hh.common.util.logE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        HhTopAppBar(
            {
                MarqueeText(
                    title.filterHtml(),
                    Modifier,
                    color = Color.White,
                    softWrap = false
                )
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = HhfTheme.colors.themeColor,
            contentPadding = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top).asPaddingValues(),
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
        var progress by remember{ mutableStateOf(1f)}
        var complete by remember{ mutableStateOf(false)}
        val scope = rememberCoroutineScope()
        val webChromeClient = object : AccompanistWebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progress = newProgress/100.toFloat()
                scope.launch {
                    delay(500L)
                    if(newProgress == 100){
                        complete = true
                    }
                }
            }
        }
        val webClient = object : AccompanistWebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.apply {
                    return if(request.url.toString().startsWith("http:") || request.url.toString().startsWith("https:")){
                        view!!.loadUrl(request.url.toString())
                        false
                    } else{
                        true
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        BoxWithConstraints(Modifier.weight(1f)) {
            url.logE()
            WebView(
                state = state,
                modifier = Modifier.size(maxWidth,maxHeight),
                onCreated = {
                    it.settings.javaScriptEnabled = true
                    // h5
                    it.settings.domStorageEnabled = true
                    //http img
                    it.settings.blockNetworkImage = false
                    it.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                    it.settings.defaultTextEncodingName = "UTF-8"
                    it.settings.javaScriptCanOpenWindowsAutomatically = true
                    it.setBackgroundColor(0)
                },
                chromeClient = webChromeClient,
                client = webClient,
                onDispose = {
                }
            )
            if(!complete){
                LinearProgressIndicator(progress = progress,Modifier.fillMaxWidth(),
                    HhfTheme.colors.themeColor,HhfTheme.colors.background)
            }
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