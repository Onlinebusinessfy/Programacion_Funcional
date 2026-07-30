package com.example.sistemarecomendaciones

import com.example.sistemarecomendaciones.domain.RejectedProduct
import com.example.sistemarecomendaciones.infrastructure.DataGeneratorConfig
import com.example.sistemarecomendaciones.infrastructure.generateData
import com.example.sistemarecomendaciones.recommendation.generateRecommendations
import com.example.sistemarecomendaciones.reporting.generateRecommendationReport
import com.example.sistemarecomendaciones.scoring.defaultScoringRules
import com.example.sistemarecomendaciones.validation.AppResult
import java.io.File
import org.junit.Test

class ReportGeneratorTest {

    @Test
    fun generateReportFile() {

        val data =
            generateData(
                config = DataGeneratorConfig(),
                clock = {
                    System.currentTimeMillis()
                }
            )


        val user =
            data.users.first()


        val result =
            generateRecommendations(
                user = user,
                products = data.products,
                interactions = data.interactions,
                rules = defaultScoringRules,
                limit = 10
            )


        val recommendations =
            when(result) {

                is AppResult.Success ->
                    result.value

                is AppResult.Failure ->
                    emptyList()
            }


        val report =
            generateRecommendationReport(
                products = data.products,
                recommendations = recommendations,
                rejectedProducts = emptyList<RejectedProduct>(),
                popularProducts = data.products.take(10)
            )


        val text =
            """
            REPORTE DEL SISTEMA DE RECOMENDACIONES
            ======================================

            Total de productos:
            ${report.totalProducts}

            Total de recomendaciones:
            ${report.totalRecommendations}

            Puntuación promedio:
            ${report.averageScore}


            Categorías recomendadas:
            ${report.recommendationsByCategory}


            Razones de rechazo:
            ${report.rejectionReasons}


            Productos populares:
            ${report.mostPopularProducts.map { it.name }}

            """.trimIndent()


        File(
            "ReporteRecomendaciones.txt"
        )
            .writeText(text)
    }
}