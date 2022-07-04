package com.hh.mine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.hh.common.base.YshhApplication
import com.hh.common.bean.MineItemBean
import com.hh.common.bean.ModelPath
import com.hh.common.ext.stringResource
import com.hh.common.theme.HhfTheme
import com.hh.common.util.*
import com.hh.mine.ui.*

class MineActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            HhfTheme(colorTheme = Color(CacheUtils.themeColor)) {
                CpNavigation.navHostController = rememberAnimatedNavController()
                AnimatedNavHost(navController = CpNavigation.navHostController, startDestination = ModelPath.Main.route) {
                    composable(ModelPath.Setting.route){
                        CpSetting()
                    }
                }
            }
        }
    }
}

val mineList = listOf(
    MineItemBean(0,YshhApplication.context.stringResource(R.string.main_mine_integral), Icons.Filled.EmojiEvents),
    MineItemBean(1,YshhApplication.context.stringResource(R.string.main_mine_collect), Icons.Filled.Favorite),
    MineItemBean(2,YshhApplication.context.stringResource(R.string.main_mine_share), Icons.Filled.Share),
    MineItemBean(3,"TODO", Icons.Filled.Today),
    MineItemBean(4,YshhApplication.context.stringResource(R.string.main_mine_start), Icons.Filled.Star),
    MineItemBean(5,YshhApplication.context.stringResource(R.string.main_mine_setting), Icons.Filled.Settings),
)