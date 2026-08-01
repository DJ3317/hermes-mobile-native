package com.hermes.mobile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.hermes.mobile.presentation.screens.logs.LogsScreen

@Composable
fun HermesNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Routes.CHAT, modifier = modifier) {
        composable(Routes.CHAT) { ChatScreen(navController = navController) }
        composable(Routes.SKILLS) { SkillsScreen() }
        composable(Routes.MESSAGING) { MessagingScreen() }
        composable(Routes.SETTINGS) { SettingsScreen(navController = navController) }
        composable(Routes.SESSIONS) { SessionsScreen(navController = navController) }
        composable(Routes.ARTIFACTS) { ArtifactsScreen() }
        composable(Routes.CRON) { CronScreen() }
        composable(Routes.AGENTS) { AgentsScreen() }
        composable(Routes.PROFILES) { ProfilesScreen() }
        composable(Routes.MODEL_CONFIG) { ModelConfigScreen() }
        composable(Routes.STARMAP) { StarmapScreen() }
        composable(Routes.PROJECTS) { ProjectsScreen() }
        composable(Routes.FILES) { FilesScreen() }
        composable(Routes.REVIEW) { ReviewScreen() }
        composable(Routes.TERMINAL) { TerminalScreen() }
        composable(Routes.PREVIEW) { PreviewScreen() }
        composable(Routes.LOGS) { LogsScreen() }
    }
}
