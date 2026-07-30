package com.example.sistemarecomendaciones.reporting

import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.Recommendation
import com.example.sistemarecomendaciones.domain.RecommendationAccumulator
import com.example.sistemarecomendaciones.domain.RecommendationReport
import com.example.sistemarecomendaciones.domain.RejectedProduct

fun createInitialAccumulator(): RecommendationAccumulator =
    RecommendationAccumulator(
        evaluatedProducts = 0,
        accepted = emptyList(),
        rejected = emptyList(),
        totalScore = 0.0
    )

fun accumulateRecommendations(
    recommendations: List<Recommendation>,
    rejectedProducts: List<RejectedProduct>
): RecommendationAccumulator {

    val acceptedAccumulator =
        recommendations.fold(
            createInitialAccumulator()
        ) { accumulator, recommendation ->

            accumulator.copy(
                evaluatedProducts =
                    accumulator.evaluatedProducts + 1,

                accepted =
                    accumulator.accepted + recommendation,

                totalScore =
                    accumulator.totalScore + recommendation.score
            )
        }

    return rejectedProducts.fold(
        acceptedAccumulator
    ) { accumulator, rejectedProduct ->

        accumulator.copy(
            evaluatedProducts =
                accumulator.evaluatedProducts + 1,

            rejected =
                accumulator.rejected + rejectedProduct
        )
    }
}

fun calculateRejectionReasons(
    rejectedProducts: List<RejectedProduct>
): Map<String, Int> =
    rejectedProducts
        .flatMap { rejectedProduct ->
            rejectedProduct.reasons
        }
        .groupingBy { reason ->
            reason
        }
        .eachCount()

fun generateRecommendationReport(
    products: List<Product>,
    recommendations: List<Recommendation>,
    rejectedProducts: List<RejectedProduct>,
    popularProducts: List<Product>
): RecommendationReport {

    val averageScore =
        if (recommendations.isEmpty()) {
            0.0
        } else {
            recommendations
                .map { recommendation ->
                    recommendation.score
                }
                .average()
        }

    val recommendationsByCategory =
        recommendations
            .groupingBy { recommendation ->
                recommendation.product.category
            }
            .eachCount()

    val rejectionReasons =
        calculateRejectionReasons(
            rejectedProducts
        )

    val mostPopularProducts =
        popularProducts
            .take(10)

    return RecommendationReport(
        totalProducts = products.size,
        totalRecommendations = recommendations.size,
        averageScore = averageScore,
        recommendationsByCategory =
            recommendationsByCategory,
        rejectionReasons =
            rejectionReasons,
        mostPopularProducts =
            mostPopularProducts
    )
}