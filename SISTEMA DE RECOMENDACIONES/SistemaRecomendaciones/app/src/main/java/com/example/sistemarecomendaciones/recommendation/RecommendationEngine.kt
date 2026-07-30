package com.example.sistemarecomendaciones.recommendation

import com.example.sistemarecomendaciones.domain.Interaction
import com.example.sistemarecomendaciones.domain.InteractionType
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.Recommendation
import com.example.sistemarecomendaciones.domain.RuleEvaluation
import com.example.sistemarecomendaciones.domain.User
import com.example.sistemarecomendaciones.normalization.normalizeProduct
import com.example.sistemarecomendaciones.scoring.ScoringRule
import com.example.sistemarecomendaciones.validation.AppResult
import com.example.sistemarecomendaciones.validation.map
import com.example.sistemarecomendaciones.validation.validateUser

private data class EvaluatedProduct(
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

fun generateRecommendations(
    user: User,
    products: List<Product>,
    interactions: List<Interaction>,
    rules: List<ScoringRule>,
    limit: Int
): AppResult<List<Recommendation>> {

    if (limit <= 0) {
        return AppResult.Failure(
            listOf("El límite de recomendaciones debe ser mayor que cero.")
        )
    }

    return validateUser(user).map { validUser ->

        val purchasedProductIds =
            interactions
                .filter { interaction ->
                    interaction.userId == validUser.id &&
                            interaction.type == InteractionType.PURCHASE
                }
                .map { interaction ->
                    interaction.productId
                }
                .toSet()

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
                product.id in purchasedProductIds
            }
            .map { product ->
                EvaluatedProduct(
                    product = product,
                    evaluations = rules.map { rule ->
                        rule(
                            validUser,
                            product,
                            interactions
                        )
                    }
                )
            }
            .filter { evaluatedProduct ->
                evaluatedProduct.score >= 0.0
            }
            .sortedWith(
                compareByDescending<EvaluatedProduct> {
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