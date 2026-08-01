package com.hermes.mobile.data.local

import javax.inject.Inject
import javax.inject.Singleton

/**
 * 内存配置缓存 — 避免 OkHttp 拦截器中 runBlocking 读 DataStore 导致死锁
 * 登录/测试连接时同步更新；拦截器只读内存
 */
@Singleton
class AppConfig @Inject constructor() {
    @Volatile
    var backendHost: String = ""
        private set

    fun updateHost(host: String) {
        if (host.isNotBlank()) backendHost = host.trim().trimEnd('/')
    }

    fun clear() { backendHost = "" }
}
