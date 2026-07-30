package com.hermes.mobile.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hermes.mobile.presentation.navigation.HermesNavGraph
import com.hermes.mobile.presentation.navigation.Routes
import com.hermes.mobile.presentation.theme.HermesTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    data object Chat : BottomTab("chat", "对话", Icons.Filled.Chat)
    data object Skills : BottomTab("skills", "技能", Icons.Filled.Psychology)
    data object Messaging : BottomTab("messaging", "消息", Icons.Filled.Hub)
    data object More : BottomTab("more", "更多", Icons.Filled.MoreHoriz)
    data object Settings : BottomTab("settings", "设置", Icons.Filled.Settings)
}

val bottomTabs = listOf(BottomTab.Chat, BottomTab.Skills, BottomTab.Messaging, BottomTab.More, BottomTab.Settings)

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HermesTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                var showMoreSheet by remember { mutableStateOf(false) }
                val showBottomBar = bottomTabs.any { tab ->
                    currentDestination?.hierarchy?.any { it.route == tab.route } == true
                } || currentDestination?.route in bottomTabs.map { it.route }

                val moreItems = listOf(
                    "sessions" to "会话记录" to Icons.Filled.List,
                    "artifacts" to "产物" to Icons.Filled.Inventory2,
                    "cron" to "定时任务" to Icons.Filled.Schedule,
                    "agents" to "子代理" to Icons.Filled.SmartToy,
                    "profiles" to "Profile" to Icons.Filled.AccountCircle,
                    "model_config" to "模型配置" to Icons.Filled.ModelTraining,
                    "starmap" to "星图" to Icons.Filled.Explore,
                    "projects" to "项目" to Icons.Filled.Folder,
                    "files" to "文件" to Icons.Filled.Description,
                    "review" to "审查" to Icons.Filled.RateReview,
                    "terminal" to "终端" to Icons.Filled.Terminal,
                    "preview" to "预览" to Icons.Filled.Visibility,
                )

                if (showMoreSheet) {
                    ModalBottomSheet(onDismissRequest = { showMoreSheet = false }) {
                        LazyColumn(modifier = Modifier.padding(bottom = 32.dp)) {
                            items(moreItems) { item ->
                                val (route, label) = item.first
                                val icon = item.second
                                Surface(
                                    onClick = { showMoreSheet = false; navController.navigate(route) { launchSingleTop = true } },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(Modifier.width(16.dp))
                                        Text(label, style = MaterialTheme.typography.bodyLarge)
                                    }
                                }
                            }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                bottomTabs.forEach { tab ->
                                    NavigationBarItem(
                                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                                        label = { Text(tab.label) },
                                        selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true,
                                        onClick = {
                                            if (tab.route == "more") {
                                                showMoreSheet = true
                                            } else {
                                                navController.navigate(tab.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { padding ->
                    HermesNavGraph(navController = navController, modifier = Modifier.padding(padding))
                }
            }
        }
    }
}
