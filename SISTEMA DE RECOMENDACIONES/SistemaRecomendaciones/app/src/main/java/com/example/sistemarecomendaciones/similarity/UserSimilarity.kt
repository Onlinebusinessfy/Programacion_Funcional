package com.example.sistemarecomendaciones.similarity

import com.example.sistemarecomendaciones.domain.User
import com.example.sistemarecomendaciones.domain.UserProfile

fun calculateSimilarity(
    firstProfile: UserProfile,
    secondProfile: UserProfile
): Double {

    val firstElements =
        firstProfile.favoriteCategories.toSet() +
                firstProfile.favoriteTags.toSet()

    val secondElements =
        secondProfile.favoriteCategories.toSet() +
                secondProfile.favoriteTags.toSet()

    val union =
        firstElements union secondElements

    val intersection =
        firstElements intersect secondElements

    return if (union.isEmpty()) {
        0.0
    } else {
        intersection.size.toDouble() /
                union.size.toDouble()
    }
}

fun findSimilarUsers(
    targetUser: User,
    users: List<User>,
    profiles: Map<Int, UserProfile>,
    limit: Int
): List<Pair<User, Double>> {

    val targetProfile =
        profiles[targetUser.id]
            ?: return emptyList()

    return users
        .asSequence()
        .filter { user ->
            user.id != targetUser.id
        }
        .mapNotNull { user ->
            profiles[user.id]?.let { profile ->
                user to calculateSimilarity(
                    firstProfile = targetProfile,
                    secondProfile = profile
                )
            }
        }
        .filter { pair ->
            pair.second > 0.0
        }
        .sortedByDescending { pair ->
            pair.second
        }
        .take(limit)
        .toList()
}