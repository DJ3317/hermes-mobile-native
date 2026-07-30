package com.hermes.mobile.domain.usecases.projects
import com.hermes.mobile.domain.models.Project
import com.hermes.mobile.domain.repositories.ProjectRepository
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(private val repo: ProjectRepository) { suspend operator fun invoke(): List<Project> = repo.getProjects() }
