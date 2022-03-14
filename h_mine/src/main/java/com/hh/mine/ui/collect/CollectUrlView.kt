package com.hh.mine.ui.collect

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hh.common.bean.ModelPath
import com.hh.common.theme.HhfTheme
import com.hh.common.util.CpNavigation
import com.hh.common.view.*
import com.hh.mine.bean.CollectUrl

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/28  14:14
 */

@Composable
fun CollectUrlView(modifier: Modifier = Modifier,collectUrl: CollectUrl,viewModel: CollectViewModel) {
    Card(modifier.padding(8.dp).clickable {
        CpNavigation.toBundle(ModelPath.WebView, Bundle().apply {
            putString(webTitle,collectUrl.name)
            putString(webUrl,collectUrl.link)
            putBoolean(webIsCollect,true)
            putInt(webCollectId,collectUrl.id)
            putInt(webCollectType,1)
        })
    },backgroundColor = HhfTheme.colors.listItem) {
        collectUrl.apply {
            Column(Modifier.padding(8.dp)) {
                Text(
                    name,
                    Modifier.padding(top = 12.dp),
                    HhfTheme.colors.textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(Modifier.padding(top = 12.dp)) {
                    Text(
                        link,
                        color = HhfTheme.colors.textColor,
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
                                viewModel.dispatch(CollectAction.UnUrl(id))
                            }
                        , tint = HhfTheme.colors.themeColor
                    )
                }
            }
        }
    }
}