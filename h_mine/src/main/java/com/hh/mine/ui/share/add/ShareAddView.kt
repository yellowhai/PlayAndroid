package com.hh.mine.ui.share.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hh.common.theme.HhfTheme
import com.hh.common.view.ColumnTopBar
import com.hh.mine.R

/**
 * @Description: Share
 * @Author: yshh
 * @CreateDate: 2022/3/11  13:38
 */
@Composable
fun ShareAddView() {
    val viewModel:ShareAddViewModel = viewModel()
    ColumnTopBar(
        Modifier
            .fillMaxSize()
            .background(HhfTheme.colors.background),title = stringResource(id = R.string.share_article),actions = {
                Text(stringResource(id = R.string.webview_share),Modifier.clickable {
                    viewModel.dispatch(ShareAddAction.Add)
                }, Color.White,14.sp)
        }) {
        Row (Modifier.padding(16.dp)){
            Text(
                "${stringResource(R.string.title)}:",
                Modifier,
                HhfTheme.colors.textColor,
                14.sp
            )
            BasicTextField(
                viewModel.viewStates.title,
                { viewModel.dispatch(ShareAddAction.UpdateTitle(it)) },
                Modifier
                    .weight(1f)
                    .padding(start = 13.dp),
                textStyle = TextStyle(HhfTheme.colors.textColor,13.sp),
                singleLine = true,
                maxLines = 20,decorationBox = {
                    if (viewModel.viewStates.title.isEmpty()) {
                        Text(
                            stringResource(id = R.string.please_input_title),
                            Modifier,
                            color = HhfTheme.colors.textInactiveColor
                        )
                    }
                    it()
                }
            )
        }
        Divider()
        Row (Modifier.padding(16.dp)){
            Text(
                "${stringResource(R.string.link)}:",
                Modifier,
                HhfTheme.colors.textColor,
                14.sp
            )
            BasicTextField(
                viewModel.viewStates.link,
                { viewModel.dispatch(ShareAddAction.UpdateLink(it)) },
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .height(224.dp)
                    .background(HhfTheme.colors.listItem)
                    .padding(5.dp),
                textStyle = TextStyle(HhfTheme.colors.textColor,13.sp,),
                decorationBox = {
                    if (viewModel.viewStates.link.isEmpty()) {
                        Text(
                            "eg:https://github.com/yellowhai/PlayAndroid",
                            Modifier,
                            color = HhfTheme.colors.textInactiveColor
                        )
                    }
                    it()
                }
            )
        }
        Text("1. 只要是任何好文都可以分享哈，并不一定要是原创！投递的文章会进入广场 tab;\\n" +
                "2. CSDN，掘金，简书等官方博客站点会直接通过，不需要审核;\\n" +
                "3. 其他个人站点会进入审核阶段，不要投递任何无效链接，测试的请尽快删除，否则可能会对你的账号产生一定影响;\\n" +
                "4. 目前处于测试阶段，如果你发现500等错误，可以向我提交日志，让我们一起使网站变得更好。\\n" +
                "5. 由于本站只有我一个人开发与维护，会尽力保证24小时内审核，当然有可能哪天太累，会延期，请保持佛系...\n", Modifier
                .fillMaxWidth()
            .weight(1f)
            .wrapContentHeight(Alignment.Bottom)
            .padding(start = 16.dp,end = 16.dp,bottom = 24.dp),color = HhfTheme.colors.textInactiveColor,12.sp)
    }
}