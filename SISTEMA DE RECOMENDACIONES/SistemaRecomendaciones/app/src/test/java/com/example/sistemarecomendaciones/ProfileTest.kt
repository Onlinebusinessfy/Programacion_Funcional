package com.example.sistemarecomendaciones

import com.example.sistemarecomendaciones.domain.Interaction
import com.example.sistemarecomendaciones.domain.InteractionType
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.User
import com.example.sistemarecomendaciones.profile.generateUserProfile
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ProfileTest {


    private val user =
        User(
            id = 1,
            name = "Samuel",
            preferredCategories = setOf("Tecnologia"),
            blockedCategories = emptySet()
        )


    private val products =
        listOf(
            Product(
                id = 1,
                name = "Laptop",
                category = "Tecnologia",
                price = 20000.0,
                rating = 5.0,
                stock = 5,
                tags = setOf("computadora", "gaming")
            ),
            Product(
                id = 2,
                name = "Mouse",
                category = "Tecnologia",
                price = 1000.0,
                rating = 4.5,
                stock = 10,
                tags = setOf("accesorio")
            )
        )


    private val interactions =
        listOf(
            Interaction(
                userId = 1,
                productId = 1,
                type = InteractionType.PURCHASE,
                timestamp = 1000L
            ),
            Interaction(
                userId = 1,
                productId = 2,
                type = InteractionType.VIEW,
                timestamp = 2000L
            )
        )


    @Test
    fun profile_generatesPurchasedProducts() {

        val profile =
            generateUserProfile(
                user,
                products,
                interactions
            )

        assertTrue(
            1 in profile.purchasedProductIds
        )
    }


    @Test
    fun profile_generatesViewedProducts() {

        val profile =
            generateUserProfile(
                user,
                products,
                interactions
            )

        assertTrue(
            2 in profile.viewedProductIds
        )
    }


    @Test
    fun profile_calculatesFavoriteCategories() {

        val profile =
            generateUserProfile(
                user,
                products,
                interactions
            )

        assertEquals(
            listOf("Tecnologia"),
            profile.favoriteCategories
        )
    }


    @Test
    fun profile_calculatesAveragePurchasePrice() {

        val profile =
            generateUserProfile(
                user,
                products,
                interactions
            )

        assertEquals(
            20000.0,
            profile.averagePurchasePrice
        )
    }


    @Test
    fun profile_countsInteractions() {

        val profile =
            generateUserProfile(
                user,
                products,
                interactions
            )

        assertEquals(
            1,
            profile.interactionFrequency[InteractionType.PURCHASE]
        )

        assertEquals(
            1,
            profile.interactionFrequency[InteractionType.VIEW]
        )
    }
}