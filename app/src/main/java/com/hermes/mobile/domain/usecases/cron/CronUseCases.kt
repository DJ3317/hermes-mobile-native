package com.hermes.mobile.domain.usecases.cron
import com.hermes.mobile.domain.models.CronTask
import com.hermes.mobile.domain.models.CronRunHistory
import com.hermes.mobile.domain.repositories.CronRepository
import javax.inject.Inject

class GetCronTasksUseCase @Inject constructor(private val repo: CronRepository) {
    suspend operator fun invoke(): List<CronTask> = repo.getTasks()
}
class CreateCronTaskUseCase @Inject constructor(private val repo: CronRepository) {
    suspend operator fun invoke(name: String, schedule: String, prompt: String): CronTask = repo.createTask(name, schedule, prompt)
}
class ToggleCronTaskUseCase @Inject constructor(private val repo: CronRepository) {
    suspend operator fun invoke(id: String, enabled: Boolean) = repo.toggleTask(id, enabled)
}
class GetCronHistoryUseCase @Inject constructor(private val repo: CronRepository) {
    suspend operator fun invoke(id: String): List<CronRunHistory> = repo.getTaskHistory(id)
}
