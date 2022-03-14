package com.hh.playandroid.bean

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/5  11:23
 */
data class BottomBean(val dashboardState: DashboardState, val name : String)

enum class DashboardState(
    val icon: ImageVector,
) {
    Home(Icons.Filled.Home),
    Square(Icons.Filled.SquareFoot),
    PubAccount(Icons.Filled.SupervisorAccount),
    Mine(Icons.Filled.Person),
    Project(Icons.Filled.Source),
}