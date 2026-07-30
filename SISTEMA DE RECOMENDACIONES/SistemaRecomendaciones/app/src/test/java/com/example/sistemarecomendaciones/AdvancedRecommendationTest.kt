package com.example.sistemarecomendaciones

import com.example.sistemarecomendaciones.domain.Recommendation
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.User
import com.example.sistemarecomendaciones.recommendation.generateRecommendations
import com.example.sistemarecomendaciones.scoring.preferredCategoryRule
import com.example.sistemarecomendaciones.validation.AppResult
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue


class AdvancedRecommendationTest {


    private val user =
        User(
            id = 1,
            name = "Samuel",
            preferredCategories =
                setOf("Tecnologia"),
            blockedCategories =
                setOf("Ropa")
        )


    private val products =
        listOf(

            Product(
                id = 1,
                name = " Laptop Gamer ",
                category = "Tecnologia",
                price = 20000.0,
                rating = 5.0,
                stock = 10,
                tags =
                    setOf(
                        "gaming"
                    )
            ),

            Product(
                id = 2,
                name = "Camisa",
                category = "Ropa",
                price = 500.0,
                rating = 4.0,
                stock = 5,
                tags =
                    emptySet()
            ),

            Product(
                id = 3,
                name = "Mouse",
                category = "Tecnologia",
                price = 800.0,
                rating = 4.5,
                stock = 0,
                tags =
                    emptySet()
            )
        )


    @Test
    fun generateRecommendations_excludesBlockedCategories() {

        val result =
            generateRecommendations(
                user = user,
                products = products,
                interactions = emptyList(),
                rules = listOf(
                    preferredCategoryRule
                ),
                limit = 10
            )


        assertIs<AppResult.Success<List<Recommendation>>>(
            result
        )


        assertTrue(
            result.value.none {
                it.product.category == "Ropa"
            }
        )
    }


    @Test
    fun generateRecommendations_excludesProductsWithoutStock() {

        val result =
            generateRecommendations(
                user,
                products,
                emptyList(),
                listOf(
                    preferredCategoryRule
                ),
                10
            )


        assertIs<AppResult.Success<List<Recommendation>>>(
            result
        )


        assertTrue(
            result.value.none {
                it.product.id == 3
            }
        )
    }


    @Test
    fun generateRecommendations_respectsLimit() {

        val result =
            generateRecommendations(
                user,
                products,
                emptyList(),
                listOf(
                    preferredCategoryRule
                ),
                1
            )


        assertIs<AppResult.Success<List<Recommendation>>>(
            result
        )


        assertEquals(
            1,
            result.value.size
        )
    }


    @Test
    fun generateRecommendations_invalidLimit_returnsFailure() {

        val result =
            generateRecommendations(
                user,
                products,
                emptyList(),
                listOf(
                    preferredCategoryRule
                ),
                0
            )


        assertIs<AppResult.Failure>(
            result
        )
    }


    @Test
    fun generateRecommendations_normalizesProductBeforeReturning() {

        val result =
            generateRecommendations(
                user,
                products,
                emptyList(),
                listOf(
                    preferredCategoryRule
                ),
                10
            )


        assertIs<AppResult.Success<List<Recommendation>>>(
            result
        )


        assertEquals(
            "Laptop gamer",
            result.value.first().product.name
        )
    }
}