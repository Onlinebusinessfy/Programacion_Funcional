package com.example.sistemarecomendaciones.profile

import com.example.sistemarecomendaciones.domain.Interaction
import com.example.sistemarecomendaciones.domain.InteractionType
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.User
import com.example.sistemarecomendaciones.domain.UserProfile

fun generateUserProfile(
    user: User,
    products: List<Product>,
    interactions: List<Interaction>
): UserProfile {

    val userInteractions =
        interactions.filter { interaction ->
            interaction.userId == user.id
        }

    val productsById =
        products.associateBy { product ->
            product.id
        }

    val purchasedProductIds =
        userInteractions
            .filter { interaction ->
                interaction.type == InteractionType.PURCHASE
            }
            .map { interaction ->
                interaction.productId
            }
            .toSet()

    val viewedProductIds =
        userInteractions
            .filter { interaction ->
                interaction.type == InteractionType.VIEW
            }
            .map { interaction ->
                interaction.productId
            }
            .toSet()

    val purchasedProducts =
        purchasedProductIds
            .mapNotNull { productId ->
                productsById[productId]
            }

    val favoriteCategories =
        purchasedProducts
            .groupingBy { product ->
                product.category
            }
            .eachCount()
            .toList()
            .sortedByDescending { entry ->
                entry.second
            }
            .map { entry ->
                entry.first
            }
            .take(5)

    val favoriteTags =
        purchasedProducts
            .flatMap { product ->
                product.tags
            }
            .groupingBy { tag ->
                tag
            }
            .eachCount()
            .toList()
            .sortedByDescending { entry ->
                entry.second
            }
            .map { entry ->
                entry.first
            }
            .take(10)

    val averagePurchasePrice =
        if (purchasedProducts.isEmpty()) {
            0.0
        } else {
            purchasedProducts
                .map { product ->
                    product.price
                }
                .average()
        }

    val interactionFrequency =
        userInteractions
            .groupingBy { interaction ->
                interaction.type
            }
            .eachCount()

    return UserProfile(
        favoriteCategories = favoriteCategories,
        favoriteTags = favoriteTags,
        averagePurchasePrice = averagePurchasePrice,
        viewedProductIds = viewedProductIds,
        purchasedProductIds = purchasedProductIds,
        interactionFrequency = interactionFrequency
    )
}

fun generateProfiles(
    users: List<User>,
    products: List<Product>,
    interactions: List<Interaction>
): Map<Int, UserProfile> =
    users.associate { user ->
        user.id to generateUserProfile(
            user = user,
            products = products,
            interactions = interactions
        )
    }