package com.padabajka.dating.feature.reaction.screen.domain

import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction

sealed interface ReactionsToMe {
    data class Like(
        val personId: PersonId,
        val profile: Profile
    ) : ReactionsToMe

    data class SuperLike(
        val personId: PersonId,
        val profile: Profile,
        val message: String
    ) : ReactionsToMe
}

fun Collection<ReactionsToMe>.sortByPriorityType() = sortedWith(comparator)

private val comparator: Comparator<ReactionsToMe>
    get() = Comparator { a, b ->
        fun priority(r: ReactionsToMe): Int =
            when (r) {
                is ReactionsToMe.SuperLike ->
                    if (r.message.isNotBlank()) 0 else 1
                is ReactionsToMe.Like -> 2
            }

        val p1 = priority(a)
        val p2 = priority(b)

        if (p1 != p2) return@Comparator p1 - p2

        return@Comparator 0
    }

fun PersonReaction.toReactionsToMe(profile: Profile): ReactionsToMe {
    return when (this) {
        is PersonReaction.Dislike -> TODO()
        is PersonReaction.Like -> toMe(profile)
        is PersonReaction.SuperLike -> toMe(profile)
    }
}

private fun PersonReaction.Like.toMe(profile: Profile): ReactionsToMe.Like {
    return ReactionsToMe.Like(
        personId = id,
        profile = profile,
    )
}

private fun PersonReaction.SuperLike.toMe(profile: Profile): ReactionsToMe.SuperLike {
    return ReactionsToMe.SuperLike(
        personId = id,
        profile = profile,
        message = message
    )
}
