# Hermes Mobile (Kotlin Native)

基于 [NousResearch/hermes-agent](https://github.com/NousResearch/hermes-agent) 桌面客户端，使用 **Kotlin + Jetpack Compose + MVVM + Clean Architecture** 原生重写的 Android APP。

## 技术栈

- **语言**: Kotlin 2.0+
- **UI**: Jetpack Compose (Material 3)
- **架构**: MVVM + Clean Architecture
- **DI**: Hilt
- **网络**: Retrofit + OkHttp + Kotlinx Serialization
- **WebSocket**: OkHttp WebSocket (JSON-RPC 2.0)
- **本地存储**: DataStore Preferences
- **最低 SDK**: Android 10 (API 29)
- **目标 SDK**: Android 15 (API 35)

## 功能模块 (17个)

| # | 模块 | 状态 |
|---|------|------|
| 1 | Chat 对话 | ✅ 流式 + 消息气泡 |
| 2 | Sessions 会话管理 | ✅ 列表 + 搜索 |
| 3 | Skills 技能管理 | ✅ 列表 + 启停 |
| 4 | Settings 设置 | ✅ 连接 + 登录 + 导航 |
| 5 | Messaging 消息网关 | ✅ 平台列表 |
| 6 | Artifacts 产物 | ✅ 基础框架 |
| 7 | Cron 定时任务 | ✅ 基础框架 |
| 8 | Agents 子代理 | ✅ 基础框架 |
| 9 | Profiles 配置文件 | ✅ 基础框架 |
| 10 | Model Config 模型配置 | ✅ 基础框架 |

## 构建

```bash
./gradlew assembleRelease
```

APK 输出在 `app/build/outputs/apk/release/`

## 后端连接

默认连接地址: `http://192.168.31.250:9191`
