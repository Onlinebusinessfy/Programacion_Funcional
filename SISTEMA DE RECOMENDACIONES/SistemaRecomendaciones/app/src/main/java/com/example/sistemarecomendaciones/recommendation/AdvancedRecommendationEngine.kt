package com.example.sistemarecomendaciones.recommendation

import com.example.sistemarecomendaciones.domain.Interaction
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.Recommendation
import com.example.sistemarecomendaciones.domain.RuleEvaluation
import com.example.sistemarecomendaciones.domain.User
import com.example.sistemarecomendaciones.domain.UserProfile
import com.example.sistemarecomendaciones.normalization.normalizeProduct
import com.example.sistemarecomendaciones.scoring.ProfileScoringRule
import com.example.sistemarecomendaciones.scoring.ScoringRule
import com.example.sistemarecomendaciones.similarity.calculateSimilarUsersProductScore
import com.example.sistemarecomendaciones.validation.AppResult
import com.example.sistemarecomendaciones.validation.map
import com.example.sistemarecomendaciones.validation.validateUser

private data class AdvancedEvaluatedProduct(
    val product: Product,
    val evaluations: List<RuleEvaluation>
) {
    val score: Double
        get() = evaluations.fold(0.0) { total, evaluation ->
            total + evaluation.score
        }

    val reasons: List<String>
        get() = evaluations.mapNotNull { evaluation ->
            evaluation.reason
        }
}

fun generateAdvancedRecommendations(
    user: User,
    profile: UserProfile,
    products: List<Product>,
    interactions: List<Interaction>,
    scoringRules: List<ScoringRule>,
    profileRules: List<ProfileScoringRule>,
    similarProfiles: List<Pair<UserProfile, Double>>,
    limit: Int
): AppResult<List<Recommendation>> {

    if (limit <= 0) {
        return AppResult.Failure(
            listOf(
                "El límite de recomendaciones debe ser mayor que cero."
            )
        )
    }

    return validateUser(user).map { validUser ->

        products
            .asSequence()
            .map(normalizeProduct)
            .filter { product ->
                product.stock > 0
            }
            .filterNot { product ->
                product.category in validUser.blockedCategories
            }
            .filterNot { product ->
                product.id in profile.purchasedProductIds
            }
            .map { product ->

                val normalEvaluations =
                    scoringRules.map { rule ->
                        rule(
                            validUser,
                            product,
                            interactions
                        )
                    }

                val profileEvaluations =
                    profileRules.map { rule ->
                        rule(
                            profile,
                            product
                        )
                    }

                val similarUserEvaluation =
                    calculateSimilarUsersProductScore(
                        product = product,
                        similarProfiles = similarProfiles
                    )

                AdvancedEvaluatedProduct(
                    product = product,
                    evaluations =
                        normalEvaluations +
                                profileEvaluations +
                                similarUserEvaluation
                )
            }
            .filter { evaluatedProduct ->
                evaluatedProduct.score >= 0.0
            }
            .sortedWith(
                compareByDescending<AdvancedEvaluatedProduct> {
                    it.score
                }
                    .thenByDescending {
                        it.product.rating
                    }
                    .thenBy {
                        it.product.price
                    }
            )
            .take(limit)
            .map { evaluatedProduct ->
                Recommendation(
                    product = evaluatedProduct.product,
                    score = evaluatedProduct.score,
                    reasons = evaluatedProduct.reasons
                )
            }
            .toList()
    }
}