package com.example.sistemarecomendaciones.reporting

import android.content.Context
import com.example.sistemarecomendaciones.domain.Recommendation
import com.example.sistemarecomendaciones.domain.RecommendationReport

fun saveRecommendationsToFile(
    context: Context,
    recommendations: List<Recommendation>
): String {

    val content =
        buildString {

            appendLine(
                "RESULTADOS DEL SISTEMA DE RECOMENDACIONES"
            )

            appendLine(
                "========================================"
            )

            appendLine()

            if (recommendations.isEmpty()) {

                appendLine(
                    "No se encontraron recomendaciones."
                )

            } else {

                recommendations.forEachIndexed {
                        index,
                        recommendation ->

                    appendLine(
                        "RECOMENDACIÓN ${index + 1}"
                    )

                    appendLine(
                        "Producto: " +
                                recommendation.product.name
                    )

                    appendLine(
                        "ID: " +
                                recommendation.product.id
                    )

                    appendLine(
                        "Categoría: " +
                                recommendation.product.category
                    )

                    appendLine(
                        "Precio: $" +
                                recommendation.product.price
                    )

                    appendLine(
                        "Calificación: " +
                                recommendation.product.rating
                    )

                    appendLine(
                        "Puntuación: " +
                                recommendation.score
                    )

                    appendLine(
                        "Razones: " +
                                recommendation.reasons
                                    .joinToString(
                                        separator = " | "
                                    )
                    )

                    appendLine(
                        "----------------------------------------"
                    )
                }
            }
        }

    val file =
        context.openFileOutput(
            "resultados_recomendaciones.txt",
            Context.MODE_PRIVATE
        )

    file.bufferedWriter()
        .use { writer ->

            writer.write(
                content
            )
        }

    return context
        .getFileStreamPath(
            "resultados_recomendaciones.txt"
        )
        .absolutePath
}

fun saveReportToFile(
    context: Context,
    report: RecommendationReport
): String {

    val content =
        buildString {

            appendLine(
                "REPORTE DEL SISTEMA DE RECOMENDACIONES"
            )

            appendLine(
                "========================================"
            )

            appendLine()

            appendLine(
                "Total de productos: " +
                        report.totalProducts
            )

            appendLine(
                "Total de recomendaciones: " +
                        report.totalRecommendations
            )

            appendLine(
                "Puntuación promedio: " +
                        report.averageScore
            )

            appendLine()

            appendLine(
                "RECOMENDACIONES POR CATEGORÍA"
            )

            report.recommendationsByCategory
                .forEach {
                        entry ->

                    appendLine(
                        "${entry.key}: " +
                                entry.value
                    )
                }

            appendLine()

            appendLine(
                "RAZONES DE RECHAZO"
            )

            if (
                report.rejectionReasons
                    .isEmpty()
            ) {

                appendLine(
                    "No se registraron rechazos."
                )

            } else {

                report.rejectionReasons
                    .forEach {
                            entry ->

                        appendLine(
                            "${entry.key}: " +
                                    entry.value
                        )
                    }
            }

            appendLine()

            appendLine(
                "PRODUCTOS POPULARES"
            )

            report.mostPopularProducts
                .forEachIndexed {
                        index,
                        product ->

                    appendLine(
                        "${index + 1}. " +
                                product.name +
                                " - " +
                                product.category
                    )
                }
        }

    val file =
        context.openFileOutput(
            "reporte_recomendaciones.txt",
            Context.MODE_PRIVATE
        )

    file.bufferedWriter()
        .use { writer ->

            writer.write(
                content
            )
        }

    return context
        .getFileStreamPath(
            "reporte_recomendaciones.txt"
        )
        .absolutePath
}