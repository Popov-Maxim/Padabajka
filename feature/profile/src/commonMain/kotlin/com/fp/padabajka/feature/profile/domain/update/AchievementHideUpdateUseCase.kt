package com.fp.padabajka.feature.profile.domain.update

import com.fp.padabajka.core.repository.api.EditProfileRepository
import com.fp.padabajka.core.repository.api.model.profile.Achievement

class AchievementHideUpdateUseCase(private val repository: EditProfileRepository) {
    suspend operator fun invoke(achievement: Achievement) {
        repository.update { profile ->
            val hiddenAchievement = achievement.copy(hidden = true)
            profile.copy(
                mainAchievement = profile.mainAchievement?.takeUnless { it == achievement },
                achievements = profile.achievements.map {
                    if (it == achievement) hiddenAchievement else it
                }
            )
        }
    }
}
