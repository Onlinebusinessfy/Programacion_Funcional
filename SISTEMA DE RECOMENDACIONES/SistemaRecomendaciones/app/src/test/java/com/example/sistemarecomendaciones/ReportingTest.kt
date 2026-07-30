package com.example.sistemarecomendaciones

import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.Recommendation
import com.example.sistemarecomendaciones.domain.RejectedProduct
import com.example.sistemarecomendaciones.reporting.accumulateRecommendations
import com.example.sistemarecomendaciones.reporting.calculateRejectionReasons
import com.example.sistemarecomendaciones.reporting.createInitialAccumulator
import com.example.sistemarecomendaciones.reporting.generateRecommendationReport
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ReportingTest {


    private val products =
        listOf(
            Product(
                id = 1,
                name = "Laptop",
                category = "Tecnologia",
                price = 20000.0,
                rating = 5.0,
                stock = 5,
                tags = setOf("gaming")
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


    private val recommendations =
        listOf(
            Recommendation(
                product = products[0],
                score = 50.0,
                reasons =
                    listOf(
                        "Categoría preferida"
                    )
            ),

            Recommendation(
                product = products[1],
                score = 30.0,
                reasons =
                    listOf(
                        "Buen rating"
                    )
            )
        )


    private val rejectedProducts =
        listOf(
            RejectedProduct(
                product = products[0],
                reasons =
                    listOf(
                        "Sin stock",
                        "Categoría bloqueada"
                    )
            )
        )


    @Test
    fun initialAccumulator_hasEmptyValues() {

        val result =
            createInitialAccumulator()


        assertEquals(
            0,
            result.evaluatedProducts
        )

        assertTrue(
            result.accepted.isEmpty()
        )

        assertTrue(
            result.rejected.isEmpty()
        )

        assertEquals(
            0.0,
            result.totalScore
        )
    }


    @Test
    fun accumulateRecommendations_countsAcceptedAndRejected() {

        val result =
            accumulateRecommendations(
                recommendations,
                rejectedProducts
            )


        assertEquals(
            3,
            result.evaluatedProducts
        )


        assertEquals(
            2,
            result.accepted.size
        )


        assertEquals(
            1,
            result.rejected.size
        )


        assertEquals(
            80.0,
            result.totalScore
        )
    }


    @Test
    fun calculateRejectionReasons_countsReasons() {

        val result =
            calculateRejectionReasons(
                rejectedProducts
            )


        assertEquals(
            1,
            result["Sin stock"]
        )


        assertEquals(
            1,
            result["Categoría bloqueada"]
        )
    }


    @Test
    fun generateRecommendationReport_calculatesTotals() {

        val result =
            generateRecommendationReport(
                products = products,
                recommendations = recommendations,
                rejectedProducts = rejectedProducts,
                popularProducts = products
            )


        assertEquals(
            2,
            result.totalProducts
        )


        assertEquals(
            2,
            result.totalRecommendations
        )


        assertEquals(
            40.0,
            result.averageScore
        )
    }


    @Test
    fun report_groupsRecommendationsByCategory() {

        val result =
            generateRecommendationReport(
                products,
                recommendations,
                emptyList(),
                products
            )


        assertEquals(
            2,
            result.recommendationsByCategory["Tecnologia"]
        )
    }


    @Test
    fun report_limitsPopularProductsToTen() {

        val manyProducts =
            List(15) {
                products[0]
            }


        val result =
            generateRecommendationReport(
                products,
                recommendations,
                emptyList(),
                manyProducts
            )


        assertEquals(
            10,
            result.mostPopularProducts.size
        )
    }
}