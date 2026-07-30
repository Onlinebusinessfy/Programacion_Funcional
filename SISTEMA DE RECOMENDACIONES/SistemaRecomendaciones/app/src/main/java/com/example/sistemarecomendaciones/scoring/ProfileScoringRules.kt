package com.example.sistemarecomendaciones.scoring

import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.RuleEvaluation
import com.example.sistemarecomendaciones.domain.UserProfile
import kotlin.math.abs

typealias ProfileScoringRule =
            (UserProfile, Product) -> RuleEvaluation

val relatedProfileTagsRule: ProfileScoringRule =
    { profile, product ->

        val matchingTags =
            product.tags.intersect(
                profile.favoriteTags.toSet()
            )

        if (matchingTags.isNotEmpty()) {
            RuleEvaluation(
                score = matchingTags.size * 8.0,
                reason = "El producto tiene etiquetas relacionadas con compras anteriores."
            )
        } else {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        }
    }

val profileAveragePriceRule: ProfileScoringRule =
    { profile, product ->

        if (profile.averagePurchasePrice <= 0.0) {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        } else {

            val difference =
                abs(
                    product.price -
                            profile.averagePurchasePrice
                )

            val allowedDifference =
                profile.averagePurchasePrice * 0.30

            if (difference <= allowedDifference) {
                RuleEvaluation(
                    score = 12.0,
                    reason = "El precio es cercano al promedio de compras del usuario."
                )
            } else {
                RuleEvaluation(
                    score = -5.0,
                    reason = "El precio se aleja del promedio de compras del usuario."
                )
            }
        }
    }

val profileFavoriteCategoryRule: ProfileScoringRule =
    { profile, product ->

        if (product.category in profile.favoriteCategories) {
            RuleEvaluation(
                score = 15.0,
                reason = "El producto pertenece a una categoría frecuente en el historial."
            )
        } else {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        }
    }

val defaultProfileScoringRules:
        List<ProfileScoringRule> =
    listOf(
        relatedProfileTagsRule,
        profileAveragePriceRule,
        profileFavoriteCategoryRule
    )

fun evaluateProfileRules(
    profile: UserProfile,
    product: Product,
    rules: List<ProfileScoringRule> =
        defaultProfileScoringRules
): List<RuleEvaluation> =
    rules.map { rule ->
        rule(
            profile,
            product
        )
    }