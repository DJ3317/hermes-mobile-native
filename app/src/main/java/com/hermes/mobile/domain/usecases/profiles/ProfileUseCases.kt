package com.hermes.mobile.domain.usecases.profiles
import com.hermes.mobile.domain.models.Profile
import com.hermes.mobile.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetProfilesUseCase @Inject constructor(private val repo: ProfileRepository) { suspend operator fun invoke(): List<Profile> = repo.getProfiles() }
class CreateProfileUseCase @Inject constructor(private val repo: ProfileRepository) { suspend operator fun invoke(name: String, label: String): Profile = repo.createProfile(name, label) }
class DeleteProfileUseCase @Inject constructor(private val repo: ProfileRepository) { suspend operator fun invoke(name: String) = repo.deleteProfile(name) }
