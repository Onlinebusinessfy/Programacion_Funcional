package com.example.sistemarecomendaciones

import com.example.sistemarecomendaciones.infrastructure.DataGeneratorConfig
import com.example.sistemarecomendaciones.infrastructure.generateData
import com.example.sistemarecomendaciones.recommendation.generateRecommendations
import com.example.sistemarecomendaciones.scoring.defaultScoringRules
import com.example.sistemarecomendaciones.validation.AppResult
import org.junit.Test
import java.io.File

class RecommendationRunnerTest {

    @Test
    fun generateRecommendationFile() {

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


        val content =
            when(result) {

                is AppResult.Success -> {

                    buildString {

                        appendLine(
                            "RESULTADOS DEL SISTEMA DE RECOMENDACIONES"
                        )

                        appendLine()

                        result.value.forEachIndexed {
                                index,
                                recommendation ->

                            appendLine(
                                "Recomendación ${index + 1}"
                            )

                            appendLine(
                                "Producto: ${recommendation.product.name}"
                            )

                            appendLine(
                                "Categoría: ${recommendation.product.category}"
                            )

                            appendLine(
                                "Score: ${recommendation.score}"
                            )

                            appendLine(
                                "Razones: ${recommendation.reasons.joinToString()}"
                            )

                            appendLine()
                        }
                    }
                }

                is AppResult.Failure -> {
                    result.errors.joinToString("\n")
                }
            }


        File(
            "ResultadosRecomendaciones.txt"
        ).writeText(content)
    }
}