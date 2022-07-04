package com.hh.playandroid

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.hh.common.base.BaseActivity
import com.hh.common.ext.showToast
import com.hh.common.ext.stringResource
import com.hh.common.theme.HhfTheme
import com.hh.common.util.*
import com.hh.common.view.DialogProgress
import com.hh.common.view.appTheme
import com.hh.common.view.isNight
import com.hh.playandroid.ui.HhfNavigation
import com.hh.playandroid.ui.SplashView

class MainActivity : BaseActivity() {
    var exitTime = 0L

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        setContent {
            HhfTheme(
                theme = if (isSystemInDarkTheme() || isNight) HhfTheme.Theme.Dark else HhfTheme.Theme.Light,
                colorTheme = appTheme
            ) {
                val viewModel : MainViewModel = viewModel()
                if (viewModel.isSplash) {
                    SplashView { viewModel.isSplash = false }
                } else {
                    CpNavigation.navHostController = rememberAnimatedNavController()
                    HhfNavigation()
                    DialogProgress()
                }
                if(isMourningDay()){
                    Canvas(modifier = Modifier.fillMaxSize()){
                        drawRect(color = Color.White,blendMode = BlendMode.Saturation)
                    }
                }
            }
        }
        addCallback()
    }

    private fun addCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = NavController(this@MainActivity)
                if (nav.currentDestination != null && nav.currentDestination!!.id != androidx.appcompat.R.id.content) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        showToast(stringResource(com.hh.common.R.string.press_again_to_exit))
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }
            }
        })
    }
}