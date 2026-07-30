package com.hermes.mobile.domain.usecases.files
import com.hermes.mobile.domain.models.FileEntry
import com.hermes.mobile.domain.repositories.FileRepository
import javax.inject.Inject

class ListFilesUseCase @Inject constructor(private val repo: FileRepository) { suspend operator fun invoke(path: String): List<FileEntry> = repo.listFiles(path) }
class GetFileContentUseCase @Inject constructor(private val repo: FileRepository) { suspend operator fun invoke(path: String): String = repo.getFileContent(path) }
