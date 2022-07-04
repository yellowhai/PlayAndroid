package com.hh.playandroid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/24  9:38
 */
class MainViewModel : ViewModel() {
    var isSplash by mutableStateOf(true)
}