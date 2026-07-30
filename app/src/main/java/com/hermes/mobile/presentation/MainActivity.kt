package com.hermes.mobile.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hermes.mobile.presentation.screens.chat.ChatScreen
import com.hermes.mobile.presentation.screens.sessions.SessionsScreen
import com.hermes.mobile.presentation.screens.skills.SkillsScreen
import com.hermes.mobile.presentation.screens.messaging.MessagingScreen
import com.hermes.mobile.presentation.screens.settings.SettingsScreen
import com.hermes.mobile.presentation.screens.artifacts.ArtifactsScreen
import com.hermes.mobile.presentation.screens.cron.CronScreen
import com.hermes.mobile.presentation.screens.agents.AgentsScreen
import com.hermes.mobile.presentation.screens.profiles.ProfilesScreen
import com.hermes.mobile.presentation.screens.model.ModelConfigScreen
import com.hermes.mobile.presentation.screens.starmap.StarmapScreen
import com.hermes.mobile.presentation.screens.projects.ProjectsScreen
import com.hermes.mobile.presentation.screens.files.FilesScreen
import com.hermes.mobile.presentation.screens.review.ReviewScreen
import com.hermes.mobile.presentation.screens.terminal.TerminalScreen
import com.hermes.mobile.presentation.screens.preview.PreviewScreen
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
                val showBottomBar = bottomTabs.any { tab ->
                    currentDestination?.hierarchy?.any { it.route == tab.route } == true
                } || currentDestination?.route in bottomTabs.map { it.route }

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
                                            navController.navigate(tab.route) {
                                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomTab.Chat.route,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable(BottomTab.Chat.route) { ChatScreen(navController = navController) }
                        composable(BottomTab.Skills.route) { SkillsScreen() }
                        composable(BottomTab.Messaging.route) { MessagingScreen() }
                        composable(BottomTab.Settings.route) { SettingsScreen(navController = navController) }
                        composable("sessions") { SessionsScreen(navController = navController) }
                        composable("artifacts") { ArtifactsScreen() }
                        composable("cron") { CronScreen() }
                        composable("agents") { AgentsScreen() }
                        composable("profiles") { ProfilesScreen() }
                        composable("model_config") { ModelConfigScreen() }
                        composable("starmap") { StarmapScreen() }
                        composable("projects") { ProjectsScreen() }
                        composable("files") { FilesScreen() }
                        composable("review") { ReviewScreen() }
                        composable("terminal") { TerminalScreen() }
                        composable("preview") { PreviewScreen() }
                    }
                }
            }
        }
    }
}
