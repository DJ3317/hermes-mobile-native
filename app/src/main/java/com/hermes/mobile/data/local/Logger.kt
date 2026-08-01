package com.hermes.mobile.data.local

import android.util.Log
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/** 日志级别 */
enum class LogLevel { DEBUG, INFO, WARN, ERROR }

/** 日志条目 */
data class LogEntry(
    val id: Long = 0,
    val timestamp: Instant = Instant.now(),
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwable: String? = null
)

/** 应用日志系统 — 输出到 Logcat + 内存缓存 */
@Singleton
class Logger @Inject constructor() {
    private val _logs = mutableListOf<LogEntry>()
    private val idCounter = java.util.concurrent.atomic.AtomicLong(0)
    val logs: List<LogEntry> get() = _logs.toList()

    fun d(tag: String, msg: String) = add(LogLevel.DEBUG, tag, msg)
    fun i(tag: String, msg: String) = add(LogLevel.INFO, tag, msg)
    fun w(tag: String, msg: String) = add(LogLevel.WARN, tag, msg)
    fun e(tag: String, msg: String, t: Throwable? = null) = add(LogLevel.ERROR, tag, msg, t?.message)

    private fun add(level: LogLevel, tag: String, msg: String, throwable: String? = null) {
        val entry = LogEntry(
            id = idCounter.incrementAndGet(),
            level = level, tag = tag, message = msg, throwable = throwable
        )
        _logs.add(entry)
        if (_logs.size > 500) _logs.removeAt(0)
        when (level) {
            LogLevel.DEBUG -> Log.d(tag, msg)
            LogLevel.INFO -> Log.i(tag, msg)
            LogLevel.WARN -> Log.w(tag, msg)
            LogLevel.ERROR -> Log.e(tag, msg)
        }
    }

    fun clear() { _logs.clear() }
}
