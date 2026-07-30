import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/** 日志级别 */
enum class LogLevel { DEBUG, INFO, WARN, ERROR }

/** 日志条目 */
data class LogEntry(
    val id: Long = System.currentTimeMillis(),
    val timestamp: Instant = Instant.now(),
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwable: String? = null
)

/** 本地日志系统 — 存储到文件，支持查看 */
@Singleton
class Logger @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val logFile: File = File(context.filesDir, "hermes_logs.txt")
    private val _logs = mutableListOf<LogEntry>()
    val logs: List<LogEntry> get() = _logs.toList()

    init { loadFromFile() }

    fun d(tag: String, msg: String) = add(LogLevel.DEBUG, tag, msg)
    fun i(tag: String, msg: String) = add(LogLevel.INFO, tag, msg)
    fun w(tag: String, msg: String) = add(LogLevel.WARN, tag, msg)
    fun e(tag: String, msg: String, t: Throwable? = null) = add(LogLevel.ERROR, tag, msg, t?.message)

    private fun add(level: LogLevel, tag: String, msg: String, throwable: String? = null) {
        val entry = LogEntry(level = level, tag = tag, message = msg, throwable = throwable)
        _logs.add(entry)
        if (_logs.size > 200) _logs.removeAt(0)
        appendToFile(entry)
    }

    fun clear() { _logs.clear(); logFile.delete() }

    private fun loadFromFile() {
        try {
            if (!logFile.exists()) return
            val lines = logFile.readLines().takeLast(200)
            _logs.clear()
            lines.forEach { line ->
                val parts = line.split("|", limit = 5)
                if (parts.size >= 4) {
                    _logs.add(LogEntry(
                        timestamp = try { Instant.parse(parts[0]) } catch (_: Exception) { Instant.now() },
                        level = try { LogLevel.valueOf(parts[1]) } catch (_: Exception) { LogLevel.INFO },
                        tag = parts[2],
                        message = parts[3],
                        throwable = parts.getOrNull(4)
                    ))
                }
            }
        } catch (_: Exception) { }
    }

    private fun appendToFile(entry: LogEntry) {
        try {
            logFile.appendText(
                "${entry.timestamp}|${entry.level}|${entry.tag}|${entry.message}${if (entry.throwable != null) "|${entry.throwable}" else ""}\n"
            )
        } catch (_: Exception) { }
    }
}
