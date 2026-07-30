package com.example.sistemarecomendaciones.domain

data class User(
    val id: Int,
    val name: String,
    val preferredCategories: Set<String>,
    val blockedCategories: Set<String>
)

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val stock: Int,
    val tags: Set<String>
)

data class Interaction(
    val userId: Int,
    val productId: Int,
    val type: InteractionType,
    val timestamp: Long
)

enum class InteractionType {
    VIEW,
    FAVORITE,
    PURCHASE,
    REMOVE_FROM_CART
}

data class ProductScore(
    val product: Product,
    val score: Double,
    val ruleScores: Map<String, Double>
)

data class Recommendation(
    val product: Product,
    val score: Double,
    val reasons: List<String>
)

data class RejectedProduct(
    val product: Product,
    val reasons: List<String>
)

data class RecommendationReport(
    val totalProducts: Int,
    val totalRecommendations: Int,
    val averageScore: Double,
    val recommendationsByCategory: Map<String, Int>,
    val rejectionReasons: Map<String, Int>,
    val mostPopularProducts: List<Product>
)

data class ProcessingError(
    val code: String,
    val message: String
)

data class RuleEvaluation(
    val score: Double,
    val reason: String?
)

data class UserProfile(
    val favoriteCategories: List<String>,
    val favoriteTags: List<String>,
    val averagePurchasePrice: Double,
    val viewedProductIds: Set<Int>,
    val purchasedProductIds: Set<Int>,
    val interactionFrequency: Map<InteractionType, Int>
)

data class RecommendationAccumulator(
    val evaluatedProducts: Int,
    val accepted: List<Recommendation>,
    val rejected: List<RejectedProduct>,
    val totalScore: Double
)

data class Category(
    val name: String,
    val children: List<Category>
)