package com.hermes.mobile.domain.usecases.skills
import com.hermes.mobile.domain.models.Skill
import com.hermes.mobile.domain.repositories.SkillRepository
import javax.inject.Inject

class GetSkillsUseCase @Inject constructor(private val repo: SkillRepository) {
    suspend operator fun invoke(): List<Skill> = repo.getSkills()
}
class ToggleSkillUseCase @Inject constructor(private val repo: SkillRepository) {
    suspend operator fun invoke(name: String, enabled: Boolean) = repo.toggleSkill(name, enabled)
}
class CreateSkillUseCase @Inject constructor(private val repo: SkillRepository) {
    suspend operator fun invoke(name: String, description: String, content: String): Skill = repo.createSkill(name, description, content)
}
