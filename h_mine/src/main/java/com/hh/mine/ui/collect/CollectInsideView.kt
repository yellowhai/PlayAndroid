package com.hh.mine.ui.collect

import android.os.Bundle
import android.text.TextUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CpNavigation
import com.hh.common.view.*
import com.hh.mine.R
import com.hh.mine.bean.CollectInside

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/28  9:24
 */

@Composable
fun InsideView(modifier: Modifier = Modifier, collectInside: CollectInside,action : ()-> Unit) {
    Card(modifier.padding(8.dp).clickable {
        CpNavigation.toBundle(ModelPath.WebView, Bundle().apply {
            putString(webTitle,collectInside.title)
            putString(webUrl,collectInside.link)
            putBoolean(webIsCollect,true)
            putInt(webCollectId,collectInside.id)
            putInt(webCollectType,0)
        })
    },backgroundColor = HhfTheme.colors.listItem) {
        collectInside.apply {
            Column(Modifier.padding(8.dp)) {
                Row {
                    Text(
                        text = if (author.isEmpty()) stringResource(id = R.string.anonymous) else author,
                        fontSize = 13.sp,
                        color = HhfTheme.colors.textColor
                    )
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
                                fontWeight = FontWeight.Bold
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
                        chapterName,
                        Modifier.padding(top = 12.dp),
                        HhfTheme.colors.textColor,
                        fontSize = 13.sp
                    )
                    Icon(
                        Icons.Filled.Favorite, contentDescription = "",
                        Modifier
                            .wrapContentWidth(
                                Alignment.End
                            )
                            .weight(1f)
                            .clickable {
                                action.invoke()
                            }
                        , tint = HhfTheme.colors.themeColor
                    )
                }
            }
        }
    }
}