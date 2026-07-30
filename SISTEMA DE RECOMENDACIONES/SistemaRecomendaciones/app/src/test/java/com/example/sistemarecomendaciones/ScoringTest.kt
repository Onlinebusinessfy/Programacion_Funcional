package com.example.sistemarecomendaciones

import com.example.sistemarecomendaciones.domain.Interaction
import com.example.sistemarecomendaciones.domain.InteractionType
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.User
import com.example.sistemarecomendaciones.scoring.blockedCategoryRule
import com.example.sistemarecomendaciones.scoring.calculateReasons
import com.example.sistemarecomendaciones.scoring.calculateScore
import com.example.sistemarecomendaciones.scoring.popularityRule
import com.example.sistemarecomendaciones.scoring.preferredCategoryRule
import com.example.sistemarecomendaciones.scoring.ratingRule
import com.example.sistemarecomendaciones.scoring.removedFromCartRule
import com.example.sistemarecomendaciones.scoring.recentlyViewedRule
import com.example.sistemarecomendaciones.scoring.stockRule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ScoringTest {


    private val user =
        User(
            id = 1,
            name = "Samuel",
            preferredCategories =
                setOf("Tecnologia"),
            blockedCategories =
                setOf("Ropa")
        )


    private val product =
        Product(
            id = 10,
            name = "Laptop",
            category = "Tecnologia",
            price = 20000.0,
            rating = 4.8,
            stock = 5,
            tags =
                setOf("computadora")
        )


    @Test
    fun preferredCategory_addsScore() {

        val result =
            preferredCategoryRule(
                user,
                product,
                emptyList()
            )

        assertEquals(
            30.0,
            result.score
        )

        assertEquals(
            "Coincide con una categoría preferida.",
            result.reason
        )
    }


    @Test
    fun blockedCategory_penalizesProduct() {

        val blockedProduct =
            product.copy(
                category = "Ropa"
            )

        val result =
            blockedCategoryRule(
                user,
                blockedProduct,
                emptyList()
            )

        assertEquals(
            -100.0,
            result.score
        )
    }


    @Test
    fun highRating_generatesBonus() {

        val result =
            ratingRule(
                user,
                product,
                emptyList()
            )

        assertEquals(
            38.4,
            result.score
        )

        assertTrue(
            result.reason != null
        )
    }


    @Test
    fun viewedProduct_generatesScore() {

        val interaction =
            Interaction(
                userId = 1,
                productId = 10,
                type = InteractionType.VIEW,
                timestamp = 1000L
            )

        val result =
            recentlyViewedRule(
                user,
                product,
                listOf(interaction)
            )

        assertEquals(
            12.0,
            result.score
        )
    }


    @Test
    fun removedProduct_getsNegativeScore() {

        val interaction =
            Interaction(
                userId = 1,
                productId = 10,
                type = InteractionType.REMOVE_FROM_CART,
                timestamp = 1000L
            )

        val result =
            removedFromCartRule(
                user,
                product,
                listOf(interaction)
            )

        assertEquals(
            -15.0,
            result.score
        )
    }


    @Test
    fun noStockProduct_getsPenalty() {

        val emptyProduct =
            product.copy(
                stock = 0
            )

        val result =
            stockRule(
                user,
                emptyProduct,
                emptyList()
            )

        assertEquals(
            -100.0,
            result.score
        )
    }


    @Test
    fun calculateScore_combinesMultipleRules() {

        val rules =
            listOf(
                preferredCategoryRule,
                ratingRule,
                stockRule
            )

        val score =
            calculateScore(
                user,
                product,
                emptyList(),
                rules
            )

        assertEquals(
            73.4,
            score
        )
    }
}