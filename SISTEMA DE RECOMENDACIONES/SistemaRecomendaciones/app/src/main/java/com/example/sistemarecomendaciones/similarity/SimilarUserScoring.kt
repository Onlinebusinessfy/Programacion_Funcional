package com.example.sistemarecomendaciones.similarity

import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.RuleEvaluation
import com.example.sistemarecomendaciones.domain.UserProfile

fun calculateSimilarUsersProductScore(
    product: Product,
    similarProfiles: List<Pair<UserProfile, Double>>
): RuleEvaluation {

    val weightedPurchaseScore =
        similarProfiles.fold(0.0) { total, pair ->

            val profile = pair.first
            val similarity = pair.second

            if (product.id in profile.purchasedProductIds) {
                total + (similarity * 20.0)
            } else {
                total
            }
        }

    return if (weightedPurchaseScore > 0.0) {
        RuleEvaluation(
            score = weightedPurchaseScore,
            reason = "Usuarios con intereses similares han comprado este producto."
        )
    } else {
        RuleEvaluation(
            score = 0.0,
            reason = null
        )
    }
}