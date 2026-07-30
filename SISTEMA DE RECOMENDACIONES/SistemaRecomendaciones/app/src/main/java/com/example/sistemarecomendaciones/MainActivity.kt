package com.example.sistemarecomendaciones

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sistemarecomendaciones.domain.Category
import com.example.sistemarecomendaciones.domain.Recommendation
import com.example.sistemarecomendaciones.domain.UserProfile
import com.example.sistemarecomendaciones.infrastructure.DataGeneratorConfig
import com.example.sistemarecomendaciones.infrastructure.compareListAndSequence
import com.example.sistemarecomendaciones.infrastructure.generateData
import com.example.sistemarecomendaciones.profile.generateProfiles
import com.example.sistemarecomendaciones.recommendation.flattenCategories
import com.example.sistemarecomendaciones.recommendation.generateAdvancedRecommendations
import com.example.sistemarecomendaciones.reporting.generateRecommendationReport
import com.example.sistemarecomendaciones.reporting.saveRecommendationsToFile
import com.example.sistemarecomendaciones.reporting.saveReportToFile
import com.example.sistemarecomendaciones.scoring.defaultProfileScoringRules
import com.example.sistemarecomendaciones.scoring.defaultScoringRules
import com.example.sistemarecomendaciones.similarity.findSimilarUsers
import com.example.sistemarecomendaciones.ui.theme.SistemaRecomendacionesTheme
import com.example.sistemarecomendaciones.validation.fold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {
            SistemaRecomendacionesTheme {
                MaterialTheme {
                    RecommendationApp(
                        context = applicationContext
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendationApp(
    context: Context
) {

    var result by remember {
        mutableStateOf<ScreenResult?>(null)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {

        try {

            result =
                withContext(
                    Dispatchers.Default
                ) {
                    executeRecommendationSystem(
                        context = context
                    )
                }

        } catch (error: Exception) {

            errorMessage =
                error.message
                    ?: "Ocurrió un error al ejecutar el sistema."
        }
    }

    when {

        errorMessage != null -> {

            ErrorScreen(
                message =
                    errorMessage
                        ?: "Error desconocido."
            )
        }

        result == null -> {

            LoadingScreen()
        }

        else -> {

            RecommendationContent(
                result =
                    result!!
            )
        }
    }
}

@Composable
fun LoadingScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text =
                "Sistema Funcional de Recomendaciones",
            style =
                MaterialTheme.typography.headlineSmall
        )

        Text(
            text =
                "Generando datos y creando archivos...",
            modifier =
                Modifier.padding(
                    top = 16.dp
                )
        )

        Text(
            text =
                "Espere un momento.",
            modifier =
                Modifier.padding(
                    top = 8.dp
                )
        )
    }
}

@Composable
fun ErrorScreen(
    message: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text =
                "Ocurrió un error",
            style =
                MaterialTheme.typography.headlineSmall
        )

        Text(
            text =
                message,
            modifier =
                Modifier.padding(
                    top = 16.dp
                )
        )
    }
}

@Composable
fun RecommendationContent(
    result: ScreenResult
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                PaddingValues(20.dp)
            ),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text =
                "Sistema Funcional de Recomendaciones",
            style =
                MaterialTheme.typography.headlineSmall
        )

        Text(
            text =
                "Usuario: ${result.userName}"
        )

        Text(
            text =
                "Productos generados: ${result.productCount}"
        )

        Text(
            text =
                "Interacciones generadas: ${result.interactionCount}"
        )

        Text(
            text =
                "Usuarios similares encontrados: " +
                        result.similarUserCount
        )

        Text(
            text =
                "Tiempo List: ${result.listTime} ms"
        )

        Text(
            text =
                "Tiempo Sequence: ${result.sequenceTime} ms"
        )

        Text(
            text =
                "Categorías recursivas encontradas: " +
                        result.categoryCount
        )

        Text(
            text =
                "Recomendaciones",
            style =
                MaterialTheme.typography.titleLarge
        )

        if (
            result.recommendations.isEmpty()
        ) {

            Text(
                text =
                    "No se encontraron recomendaciones."
            )

        } else {

            result.recommendations.forEach {
                    recommendation ->

                Text(
                    text =
                        "${recommendation.product.name} " +
                                "- Puntuación: " +
                                "%.2f".format(
                                    recommendation.score
                                )
                )

                Text(
                    text =
                        "Categoría: " +
                                recommendation.product.category
                )

                Text(
                    text =
                        "Calificación: " +
                                recommendation.product.rating
                )

                Text(
                    text =
                        "Precio: $" +
                                recommendation.product.price
                )

                Text(
                    text =
                        "Razones: " +
                                recommendation.reasons
                                    .joinToString(
                                        separator = " | "
                                    )
                )
            }
        }

        Text(
            text =
                "Reporte",
            style =
                MaterialTheme.typography.titleLarge
        )

        Text(
            text =
                "Total recomendado: " +
                        result.totalRecommendations
        )

        Text(
            text =
                "Puntuación promedio: " +
                        "%.2f".format(
                            result.averageScore
                        )
        )

        Text(
            text =
                "Archivo de resultados: " +
                        result.recommendationsFilePath
        )

        Text(
            text =
                "Archivo del reporte: " +
                        result.reportFilePath
        )
    }
}

data class ScreenResult(
    val userName: String,
    val productCount: Int,
    val interactionCount: Int,
    val similarUserCount: Int,
    val listTime: Long,
    val sequenceTime: Long,
    val categoryCount: Int,
    val recommendations: List<Recommendation>,
    val totalRecommendations: Int,
    val averageScore: Double,
    val recommendationsFilePath: String,
    val reportFilePath: String
)

fun executeRecommendationSystem(
    context: Context
): ScreenResult {

    val data =
        generateData(
            config =
                DataGeneratorConfig(
                    userCount = 50,
                    productCount = 500,
                    interactionCount = 5_000
                ),
            clock =
                System::currentTimeMillis
        )

    val targetUser =
        data.users.first()

    val profiles =
        generateProfiles(
            users =
                data.users,
            products =
                data.products,
            interactions =
                data.interactions
        )

    val targetProfile =
        profiles[targetUser.id]
            ?: UserProfile(
                favoriteCategories =
                    emptyList(),
                favoriteTags =
                    emptyList(),
                averagePurchasePrice =
                    0.0,
                viewedProductIds =
                    emptySet(),
                purchasedProductIds =
                    emptySet(),
                interactionFrequency =
                    emptyMap()
            )

    val similarUsers =
        findSimilarUsers(
            targetUser =
                targetUser,
            users =
                data.users,
            profiles =
                profiles,
            limit = 10
        )

    val similarProfiles =
        similarUsers.mapNotNull {
                pair ->

            val similarUser =
                pair.first

            val similarity =
                pair.second

            profiles[similarUser.id]
                ?.let {
                        profile ->

                    profile to similarity
                }
        }

    val recommendationResult =
        generateAdvancedRecommendations(
            user =
                targetUser,
            profile =
                targetProfile,
            products =
                data.products,
            interactions =
                data.interactions,
            scoringRules =
                defaultScoringRules,
            profileRules =
                defaultProfileScoringRules,
            similarProfiles =
                similarProfiles,
            limit = 10
        )

    val recommendations =
        recommendationResult.fold(
            onSuccess = {
                    value ->

                value
            },
            onFailure = {

                emptyList()
            }
        )

    val interactionCountByProduct =
        data.interactions
            .groupingBy {
                    interaction ->

                interaction.productId
            }
            .eachCount()

    val popularProducts =
        data.products
            .sortedByDescending {
                    product ->

                interactionCountByProduct[
                    product.id
                ] ?: 0
            }
            .take(10)

    val report =
        generateRecommendationReport(
            products =
                data.products,
            recommendations =
                recommendations,
            rejectedProducts =
                emptyList(),
            popularProducts =
                popularProducts
        )

    val performance =
        compareListAndSequence(
            user =
                targetUser,
            products =
                data.products,
            limit = 10
        )

    val categoryTree =
        Category(
            name =
                "Tecnología",
            children =
                listOf(
                    Category(
                        name =
                            "Computadoras",
                        children =
                            listOf(
                                Category(
                                    name =
                                        "Laptops",
                                    children =
                                        emptyList()
                                ),
                                Category(
                                    name =
                                        "Escritorio",
                                    children =
                                        emptyList()
                                )
                            )
                    ),
                    Category(
                        name =
                            "Celulares",
                        children =
                            listOf(
                                Category(
                                    name =
                                        "Android",
                                    children =
                                        emptyList()
                                ),
                                Category(
                                    name =
                                        "iPhone",
                                    children =
                                        emptyList()
                                )
                            )
                    )
                )
        )

    val flattenedCategories =
        flattenCategories(
            categoryTree
        )

    val recommendationsFilePath =
        saveRecommendationsToFile(
            context =
                context,
            recommendations =
                recommendations
        )

    val reportFilePath =
        saveReportToFile(
            context =
                context,
            report =
                report
        )

    return ScreenResult(
        userName =
            targetUser.name,
        productCount =
            data.products.size,
        interactionCount =
            data.interactions.size,
        similarUserCount =
            similarUsers.size,
        listTime =
            performance.listTimeMillis,
        sequenceTime =
            performance.sequenceTimeMillis,
        categoryCount =
            flattenedCategories.size,
        recommendations =
            recommendations,
        totalRecommendations =
            report.totalRecommendations,
        averageScore =
            report.averageScore,
        recommendationsFilePath =
            recommendationsFilePath,
        reportFilePath =
            reportFilePath
    )
}