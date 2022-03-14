package com.hh.mine.ui.share

import android.os.Bundle
import android.text.TextUtils
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CpNavigation
import com.hh.common.view.*
import com.hh.mine.R
import com.hh.mine.bean.Article
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

/**
 * @Description: share
 * @Author: yshh
 * @CreateDate: 2022/3/11  21:51
 */

@Composable
fun ShareView() {
    val viewModel: ShareViewModel = viewModel()
    ColumnTopBar(
        Modifier
            .fillMaxSize()
            .background(HhfTheme.colors.background),
        title = stringResource(id = R.string.main_mine_share), actions = {
            IconButton(onClick = {
                CpNavigation.to(ModelPath.ShareAdd)
            }) {
                Icon(
                    Icons.Filled.Add, contentDescription = "add",
                    tint = Color.White
                )
            }
        }
    ) {
        ShareList(viewModel = viewModel)
    }
}

@Composable
fun ShareList(modifier: Modifier = Modifier, viewModel: ShareViewModel) {
    val shareList = viewModel.getShareList().collectAsLazyPagingItems()
    var isRefresh by remember { mutableStateOf(false) }
    SwipeRefresh(state = rememberSwipeRefreshState(isRefresh), onRefresh = {
        shareList.refresh()
    }, modifier) {
        PagingItem(Modifier, shareList,
            { isRefresh = false },
            { isRefresh = false },
            { shareList.refresh() }
        ) {
            LazyColumn {
                items(it) {
                    it?.run {
                        ShareListItem(
                            article = it,
                            viewModel = viewModel,
                            refresh = { shareList.refresh() }) {
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShareListItem(
    modifier: Modifier = Modifier,
    article: Article,
    isShowLabel: Boolean = true,
    viewModel: ShareViewModel,
    refresh: () -> Unit,
    favoriteAction: (isCollect: Boolean) -> Unit
) {
    val deleteMaterialDialogState = rememberMaterialDialogState()
    var isCollect by remember { mutableStateOf(article.collect) }
    MaterialDialog(dialogState = deleteMaterialDialogState, buttons = {
        positiveButton(stringResource(R.string.confirm)) {
            viewModel.delete(article.id, refresh)
        }
        negativeButton(stringResource(R.string.cancel))
    }) {
        title(stringResource(id = R.string.tips))
        message(res = R.string.sure_delete_it)
    }
    Card(
        modifier
            .padding(8.dp)
            .clickable {
                CpNavigation.toBundle(ModelPath.WebView, Bundle().apply {
                    putString(webTitle, article.title)
                    putString(webUrl, article.link)
                    putBoolean(webIsCollect, isCollect)
                    putInt(webCollectId, article.id)
                    putInt(webCollectType, 0)
                })
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        deleteMaterialDialogState.show()
                    }
                )
            },
        backgroundColor = HhfTheme.colors.listItem, elevation = 5.dp
    ) {
        article.apply {
            Column(Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (author.isNotEmpty()) author else shareUser,
                        fontSize = 13.sp,
                        color = HhfTheme.colors.textColor
                    )
                    if (isShowLabel) {
                        if (fresh) {
                            Box(
                                Modifier
                                    .padding(start = 6.dp)
                                    .border(1.dp, Color.Red, RoundedCornerShape(5.dp))
                                    .padding(4.dp)
                            ) {
                                Text(stringResource(id = R.string.newl), Modifier, Color.Red, 10.sp)
                            }
                        }
                        if (tags.isNotEmpty()) {
                            Box(
                                Modifier
                                    .padding(start = 6.dp)
                                    .border(1.dp, Color(0xFF66BB6A), RoundedCornerShape(5.dp))
                                    .padding(4.dp)
                            ) {
                                Text(tags[0].name, Modifier, Color(0xFF66BB6A), 10.sp)
                            }
                        }
                    }
                    Text(
                        niceDate,
                        Modifier
                            .weight(1f)
                            .wrapContentWidth(
                                Alignment.End
                            )
                            .align(Alignment.CenterVertically),
                        HhfTheme.colors.textColor.copy(0.6f),
                        fontSize = 13.sp,
                    )
                }
                if (TextUtils.isEmpty(envelopePic)) {
                    Text(
                        title,
                        Modifier.padding(top = 12.dp),
                        HhfTheme.colors.textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Row(Modifier.padding(top = 12.dp)) {
                        NetworkImage(
                            envelopePic,
                            Modifier.size(100.dp),
                            contentScale = ContentScale.Crop,
                            defaultImg = R.mipmap.ic_default_round
                        )
                        Column(Modifier.padding(start = 8.dp)) {
                            Text(
                                title,
                                color = HhfTheme.colors.textColor,
                                fontSize = 14.sp,
                                maxLines = 3,
                                style = TextStyle(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                desc,
                                color = HhfTheme.colors.textInactiveColor,
                                fontSize = 13.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Row {
                    Text(
                        "$superChapterName / $chapterName",
                        Modifier.padding(top = 12.dp),
                        HhfTheme.colors.textColor,
                        fontSize = 13.sp
                    )
                    Icon(
                        if (isCollect) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "",
                        Modifier
                            .wrapContentWidth(
                                Alignment.End
                            )
                            .weight(1f)
                            .clickable {
                                favoriteAction(isCollect)
                                isCollect = !isCollect
                            },
                        tint = HhfTheme.colors.themeColor
                    )
                }
            }
        }
    }
}