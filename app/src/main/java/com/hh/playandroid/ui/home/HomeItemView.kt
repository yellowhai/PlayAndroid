package com.hh.playandroid.ui.home

import android.os.Bundle
import android.text.TextUtils
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hh.common.bean.ModelPath
import com.hh.common.ext.filterHtml
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CpNavigation
import com.hh.common.view.*
import com.hh.playandroid.R
import com.hh.playandroid.bean.ArticleBean

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/7  22:23
 */
@Composable
fun HomeListItem(modifier: Modifier = Modifier, homeBean: ArticleBean, isShowLabel : Boolean = true, favoriteAction: (isCollect : Boolean) -> Unit) {
    var isCollect by remember{ mutableStateOf(homeBean.collect)}
    Card(
        modifier
            .padding(8.dp)
            .clickable {
                CpNavigation.toBundle(ModelPath.WebView, Bundle().apply {
                    putString(webTitle, homeBean.title)
                    putString(webUrl, homeBean.link)
                    putBoolean(webIsCollect, isCollect)
                    putInt(webCollectId, homeBean.id)
                    putInt(webCollectType, 0)
                })
            },
        backgroundColor = HhfTheme.colors.listItem,elevation = 5.dp
    ) {
        homeBean.apply {
            Column(Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (author.isNotEmpty()) author else shareUser,
                        fontSize = 13.sp,
                        color = HhfTheme.colors.textColor
                    )
                    if(isShowLabel){
                        if (type == 1) {
                            Box(
                                Modifier
                                    .padding(start = 6.dp)
                                    .border(1.dp, Color.Red, RoundedCornerShape(5.dp))
                                    .padding(4.dp)
                            ) {
                                Text(stringResource(id = R.string.istop), Modifier, Color.Red, 10.sp)
                            }
                        }
                        if (fresh) {
                            Box(
                                Modifier
                                    .padding(start = 6.dp)
                                    .border(1.dp, Color.Red, RoundedCornerShape(5.dp))
                                    .padding(4.dp)
                            ) {
                                Text(stringResource(id = R.string.neww), Modifier, Color.Red, 10.sp)
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
                        title.filterHtml(),
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
                                title.filterHtml(),
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
                        if(isCollect)Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = "",
                        Modifier
                            .wrapContentWidth(
                                Alignment.End
                            )
                            .weight(1f)
                            .clickable {
                                favoriteAction(isCollect)
                                isCollect = !isCollect
                            }, tint = HhfTheme.colors.themeColor
                    )
                }
            }
        }
    }
}