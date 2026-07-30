package com.hermes.mobile.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.hermes.mobile.data.local.datastore.AuthDataStore
import com.hermes.mobile.data.local.datastore.SettingsDataStore
import com.hermes.mobile.domain.usecases.settings.LoginUseCase
import com.hermes.mobile.domain.usecases.settings.LogoutUseCase
import com.hermes.mobile.domain.usecases.settings.GetModelsUseCase
import com.hermes.mobile.domain.usecases.settings.SetModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val host: String = "http://192.168.31.250:9191",
    val token: String? = null,
    val username: String = "",
    val password: String = "",
    val isConnected: Boolean = false,
    val isLoggingIn: Boolean = false,
    val themeMode: String = "system",
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getModelsUseCase: GetModelsUseCase,
    private val setModelUseCase: SetModelUseCase,
    private val settingsDataStore: SettingsDataStore,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(token = authDataStore.getToken()) }
    }

    fun setHost(host: String) { _uiState.update { it.copy(host = host) } }
    fun setUsername(username: String) { _uiState.update { it.copy(username = username) } }
    fun setPassword(password: String) { _uiState.update { it.copy(password = password) } }

    fun testConnection() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isConnected = true, error = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "连接失败: ${e.message}") }
            }
        }
    }

    fun login() {
        val state = _uiState.value
        if (state.username.isBlank()) { _uiState.update { it.copy(error = "请输入用户名") }; return }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingIn = true) }
            try {
                val token = loginUseCase(state.username, state.password)
                _uiState.update { it.copy(token = token, isLoggingIn = false, error = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoggingIn = false, error = "登录失败: ${e.message}") }
            }
        }
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase(); _uiState.update { it.copy(token = null) } }
    }

    fun clearError() { _uiState.update { it.copy(error = null) } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: androidx.navigation.NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("设置") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(androidx.compose.foundation.rememberScrollState())
        ) {
            // Connection
            Text("后端连接", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp))
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = uiState.host,
                        onValueChange = { viewModel.setHost(it) },
                        label = { Text("服务器地址") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.username,
                        onValueChange = { viewModel.setUsername(it) },
                        label = { Text("用户名") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { viewModel.setPassword(it) },
                        label = { Text("密码") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.testConnection() }, modifier = Modifier.weight(1f)) { Text("测试连接") }
                        if (uiState.token != null) {
                            OutlinedButton(onClick = { viewModel.logout() }, modifier = Modifier.weight(1f)) { Text("退出登录") }
                        } else {
                            Button(onClick = { viewModel.login() }, enabled = !uiState.isLoggingIn, modifier = Modifier.weight(1f)) {
                                if (uiState.isLoggingIn) CircularProgressIndicator(modifier = Modifier.size(20.dp))
                                else Text("登录")
                            }
                        }
                    }

                    uiState.token?.let {
                        Text("已认证", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Navigation items
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Column {
                    SettingsNavItem("模型配置", "🤖") { navController.navigate("model_config") }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsNavItem("Profile 管理", "👤") { navController.navigate("profiles") }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsNavItem("对话记录", "📋") { navController.navigate("sessions") }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Error
            uiState.error?.let {
                Snackbar(modifier = Modifier.padding(16.dp), action = { TextButton(onClick = { viewModel.clearError() }) { Text("关闭") } }) { Text(it) }
            }
        }
    }
}

@Composable
fun SettingsNavItem(label: String, icon: String, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text("$icon  ", style = MaterialTheme.typography.titleMedium)
            Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            Text("›", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.titleLarge)
        }
    }
}
