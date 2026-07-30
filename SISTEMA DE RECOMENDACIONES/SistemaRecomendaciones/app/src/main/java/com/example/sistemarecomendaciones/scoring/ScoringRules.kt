package com.example.sistemarecomendaciones.scoring

import com.example.sistemarecomendaciones.domain.Interaction
import com.example.sistemarecomendaciones.domain.InteractionType
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.RuleEvaluation
import com.example.sistemarecomendaciones.domain.User

typealias ScoringRule =
            (User, Product, List<Interaction>) -> RuleEvaluation

val preferredCategoryRule: ScoringRule =
    { user, product, _ ->
        if (product.category in user.preferredCategories) {
            RuleEvaluation(
                score = 30.0,
                reason = "Coincide con una categoría preferida."
            )
        } else {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        }
    }

val blockedCategoryRule: ScoringRule =
    { user, product, _ ->
        if (product.category in user.blockedCategories) {
            RuleEvaluation(
                score = -100.0,
                reason = "El producto pertenece a una categoría bloqueada."
            )
        } else {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        }
    }

val ratingRule: ScoringRule =
    { _, product, _ ->
        val score = product.rating * 8.0

        if (product.rating >= 4.0) {
            RuleEvaluation(
                score = score,
                reason = "El producto tiene una calificación alta."
            )
        } else {
            RuleEvaluation(
                score = score,
                reason = null
            )
        }
    }

val purchaseHistoryRule: ScoringRule =
    { user, product, interactions ->
        val purchases =
            interactions.filter {
                it.userId == user.id &&
                        it.type == InteractionType.PURCHASE
            }

        val purchasedProductIds =
            purchases.map {
                it.productId
            }.toSet()

        if (product.id in purchasedProductIds) {
            RuleEvaluation(
                score = -20.0,
                reason = "El producto ya fue comprado por el usuario."
            )
        } else {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        }
    }

val recentlyViewedRule: ScoringRule =
    { user, product, interactions ->
        val viewed =
            interactions.any {
                it.userId == user.id &&
                        it.productId == product.id &&
                        it.type == InteractionType.VIEW
            }

        if (viewed) {
            RuleEvaluation(
                score = 12.0,
                reason = "El usuario consultó este producto recientemente."
            )
        } else {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        }
    }

val removedFromCartRule: ScoringRule =
    { user, product, interactions ->
        val removed =
            interactions.any {
                it.userId == user.id &&
                        it.productId == product.id &&
                        it.type == InteractionType.REMOVE_FROM_CART
            }

        if (removed) {
            RuleEvaluation(
                score = -15.0,
                reason = "El usuario eliminó este producto del carrito."
            )
        } else {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        }
    }

val relatedTagsRule: ScoringRule =
    { user, product, interactions ->
        val purchasedProductIds =
            interactions
                .filter {
                    it.userId == user.id &&
                            it.type == InteractionType.PURCHASE
                }
                .map {
                    it.productId
                }
                .toSet()

        val hasPurchaseHistory =
            purchasedProductIds.isNotEmpty()

        if (hasPurchaseHistory && product.tags.isNotEmpty()) {
            RuleEvaluation(
                score = 10.0,
                reason = "El usuario ha comprado productos con etiquetas relacionadas."
            )
        } else {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        }
    }

val stockRule: ScoringRule =
    { _, product, _ ->
        if (product.stock <= 0) {
            RuleEvaluation(
                score = -100.0,
                reason = "El producto no tiene existencias."
            )
        } else {
            RuleEvaluation(
                score = 5.0,
                reason = "El producto se encuentra disponible."
            )
        }
    }

val averagePurchasePriceRule: ScoringRule =
    { user, product, interactions ->
        val purchases =
            interactions.filter {
                it.userId == user.id &&
                        it.type == InteractionType.PURCHASE
            }

        if (purchases.isEmpty()) {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        } else {
            val averageProductId =
                purchases
                    .map {
                        it.productId.toDouble()
                    }
                    .average()

            val difference =
                kotlin.math.abs(
                    product.price - averageProductId
                )

            if (difference <= product.price * 0.30) {
                RuleEvaluation(
                    score = 10.0,
                    reason = "El precio es cercano al promedio de compras."
                )
            } else {
                RuleEvaluation(
                    score = -5.0,
                    reason = "El precio se aleja del promedio de compras."
                )
            }
        }
    }

val popularityRule: ScoringRule =
    { _, product, interactions ->
        val totalViews =
            interactions.count {
                it.productId == product.id &&
                        it.type == InteractionType.VIEW
            }

        val totalFavorites =
            interactions.count {
                it.productId == product.id &&
                        it.type == InteractionType.FAVORITE
            }

        val score =
            (totalViews * 0.5) +
                    (totalFavorites * 2.0)

        if (score > 0.0) {
            RuleEvaluation(
                score = score,
                reason = "El producto es popular entre otros usuarios."
            )
        } else {
            RuleEvaluation(
                score = 0.0,
                reason = null
            )
        }
    }

val defaultScoringRules: List<ScoringRule> =
    listOf(
        preferredCategoryRule,
        blockedCategoryRule,
        ratingRule,
        purchaseHistoryRule,
        recentlyViewedRule,
        removedFromCartRule,
        relatedTagsRule,
        stockRule,
        averagePurchasePriceRule,
        popularityRule
    )

fun evaluateProduct(
    user: User,
    product: Product,
    interactions: List<Interaction>,
    rules: List<ScoringRule>
): List<RuleEvaluation> =
    rules.map { rule ->
        rule(
            user,
            product,
            interactions
        )
    }

fun calculateScore(
    user: User,
    product: Product,
    interactions: List<Interaction>,
    rules: List<ScoringRule>
): Double =
    evaluateProduct(
        user,
        product,
        interactions,
        rules
    ).fold(0.0) { total, evaluation ->
        total + evaluation.score
    }

fun calculateReasons(
    user: User,
    product: Product,
    interactions: List<Interaction>,
    rules: List<ScoringRule>
): List<String> =
    evaluateProduct(
        user,
        product,
        interactions,
        rules
    ).mapNotNull {
        it.reason
    }