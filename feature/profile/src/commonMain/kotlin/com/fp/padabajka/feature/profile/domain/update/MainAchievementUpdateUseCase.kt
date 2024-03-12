package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.repository.api.EditProfileRepository
import com.fp.padabajka.core.repository.api.model.profile.Achievement

class MainAchievementUpdateUseCase(private val repository: EditProfileRepository) {
    suspend operator fun invoke(achievement: Achievement) {
        repository.update { profile ->
            val mainAchievement = achievement.copy(hidden = false)
            profile.copy(
                mainAchievement = mainAchievement,
                achievements = profile.achievements.map {
                    if (it == achievement) mainAchievement else it
                }
            )
        }
    }
}
