package com.hermes.mobile.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

sealed class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    data object Chat : BottomTab(Routes.CHAT, "对话", Icons.Filled.Chat)
    data object Skills : BottomTab(Routes.SKILLS, "技能", Icons.Filled.Psychology)
    data object Messaging : BottomTab(Routes.MESSAGING, "消息", Icons.Filled.Hub)
    data object More : BottomTab("more", "更多", Icons.Filled.MoreHoriz)
    data object Settings : BottomTab(Routes.SETTINGS, "设置", Icons.Filled.Settings)
}

val bottomTabs = listOf(BottomTab.Chat, BottomTab.Skills, BottomTab.Messaging, BottomTab.More, BottomTab.Settings)

@Composable
fun HermesBottomBar(
    currentDestination: androidx.navigation.NavDestination?,
    onTabClick: (BottomTab) -> Unit
) {
    NavigationBar {
        bottomTabs.forEach { tab ->
            NavigationBarItem(
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) },
                selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true,
                onClick = { onTabClick(tab) }
            )
        }
    }
}
