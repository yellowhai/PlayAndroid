package com.hh.mine.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hh.common.api.HttpUrl.Base_Url
import com.hh.common.bean.ModelPath
import com.hh.common.theme.ACCENT_COLORS
import com.hh.common.theme.HhfTheme
import com.hh.common.theme.PRIMARY_COLORS_SUB
import com.hh.common.util.*
import com.hh.common.view.*
import com.hh.mine.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.color.ARGBPickerState
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @ProjectName: CBook
 * @Package: com.hh.CBook.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/26  9:50
 */

@SuppressLint("CheckResult")
@Composable
fun CpSetting(modifier: Modifier = Modifier) {
    "CpSetting".logE()
    ColumnTopBar(
        modifier
            .fillMaxSize()
            .background(HhfTheme.colors.background)
            .verticalScroll(rememberScrollState()),
        title = stringResource(R.string.main_mine_setting),
        horizontalAlignment = Alignment.Start
    ) {
        SettingBasicView()
        Divider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(HhfTheme.colors.divider)
        )
        SettingOtherView()
        Divider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(HhfTheme.colors.divider)
        )
        SettingAboutView()
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun SettingBasicView() {
    val context = LocalContext.current
    val viewModel: CpSettingViewModel = viewModel()
    var caCheSize by remember {
        mutableStateOf("0.0MB")
    }
    val caCheDialogState = rememberMaterialDialogState()
    val exitDialogState = rememberMaterialDialogState()
    LaunchedEffect(caCheSize) {
        withContext(IO) {
            caCheSize = CacheDataManager.getTotalCacheSize(context)
        }
    }
    MaterialDialog(dialogState = caCheDialogState, buttons = {
        positiveButton(stringResource(R.string.confirm)) {
            CacheDataManager.clearAllCache(context)
            caCheSize = CacheDataManager.getTotalCacheSize(context)
        }
        negativeButton(stringResource(R.string.cancel))
    }) {
        title(stringResource(id = R.string.tips))
        message(res = R.string.confirm_clear_cache)
    }

    MaterialDialog(dialogState = exitDialogState, buttons = {
        positiveButton(stringResource(R.string.exit)) {
            viewModel.logout {
                CacheUtils.userInfo = ""
                CacheUtils.isLogin = false
                context.showToast(context.getString(R.string.exit_login_success))
            }
        }
        negativeButton(stringResource(R.string.cancel))
    }) {
        title(stringResource(id = R.string.tips))
        message(res = R.string.confirm_exit_app)
    }
    Text(
        stringResource(R.string.setting_basic_text),
        color = HhfTheme.colors.themeColor,
        fontSize = 13.sp, modifier = Modifier.padding(15.dp)
    )
    Column(
        Modifier
            .fillMaxWidth()
            .clickable {
                caCheDialogState.show()
            }
            .padding(15.dp)
    ) {
        Text(
            stringResource(R.string.setting_clear_cache),
            color = HhfTheme.colors.textColor,
            fontSize = 15.sp
        )
        Text(
            caCheSize,
            color = HhfTheme.colors.textInactiveColor,
            fontSize = 14.sp
        )
    }
    Column(
        Modifier
            .fillMaxWidth()
            .clickable {
                exitDialogState.show()
            }
            .padding(15.dp)
    ) {
        Text(
            stringResource(R.string.exit),
            color = HhfTheme.colors.textColor,
            fontSize = 15.sp
        )
        Text(
            stringResource(id = R.string.exit_app),
            color = HhfTheme.colors.textInactiveColor,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SettingOtherView() {
    val lifecycleCoroutineScope = rememberCoroutineScope()
    val colorDialogState = rememberMaterialDialogState()
    MaterialDialog(dialogState = colorDialogState, buttons = { defaultColorDialogButtons() }) {
        title(stringResource(id = R.string.setting_theme_color))
        colorChooser(
            colors = ACCENT_COLORS,
            subColors = PRIMARY_COLORS_SUB,
            argbPickerState = ARGBPickerState.WithAlphaSelector,
        ) {
            lifecycleCoroutineScope.launch {
                CacheUtils.themeColor = it.toArgb()
                appTheme = it
            }
        }
    }
    Text(
        stringResource(R.string.setting_other_text),
        color = HhfTheme.colors.themeColor,
        fontSize = 13.sp, modifier = Modifier.padding(15.dp)
    )
    Row(
        Modifier
            .clickable {
                colorDialogState.show()
            }
            .padding(15.dp)) {
        Column {
            Text(
                stringResource(R.string.setting_theme_color),
                color = HhfTheme.colors.textColor,
                fontSize = 15.sp
            )
            Text(
                stringResource(R.string.setting_theme_color_tips),
                color = HhfTheme.colors.textInactiveColor,
                fontSize = 14.sp
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(end = 15.dp)
        ) {
            Spacer(
                Modifier
                    .size(48.dp)
                    .border(1.dp, Color.Black, CircleShape)
                    .clip(CircleShape)
                    .background(HhfTheme.colors.themeColor)
                    .align(Alignment.End)
            )
        }
    }
    Divider(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(HhfTheme.colors.divider)
    )
    Row(
        Modifier
            .padding(15.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.night_mode), Modifier, HhfTheme.colors.textColor, 15.sp)
        Checkbox(
            checked = isNight, onCheckedChange = {
                isNight = it
            },
            Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.End),
            colors = CheckboxDefaults.colors(HhfTheme.colors.themeColor)
        )
    }
}

@Composable
fun SettingAboutView() {
    Text(
        stringResource(R.string.main_mine_about),
        color = HhfTheme.colors.themeColor,
        fontSize = 13.sp, modifier = Modifier.padding(15.dp)
    )
    AboutItem(textTitle = stringResource(R.string.project_api), textContent = Base_Url)
    AboutItem(textTitle = stringResource(R.string.author_email), textContent = "kouzhifu@163.com")
    AboutItem(
        textTitle = stringResource(R.string.project_address),
        textContent = "https://github.com/yellowhai/PlayAndroid",
        block = {
            CpNavigation.toBundle(ModelPath.WebView, Bundle().apply {
                putString(webTitle, "PlayAndroid")
                putString(webUrl, "https://github.com/yellowhai/PlayAndroid")
                putBoolean(webIsCollect, false)
                putInt(webCollectId, 998)
                putInt(webCollectType, 1)
            })
        })
}

@Composable
fun AboutItem(
    modifier: Modifier = Modifier,
    textTitle: String,
    textContent: String,
    block: () -> Unit = {}
) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable {
                block()
            }
            .padding(15.dp)) {
        Text(
            textTitle,
            color = HhfTheme.colors.textColor,
            fontSize = 15.sp
        )
        Text(
            textContent,
            color = HhfTheme.colors.textInactiveColor,
            fontSize = 14.sp
        )
    }
    Divider(
        modifier = modifier
            .height(1.dp)
            .fillMaxWidth()
    )
}