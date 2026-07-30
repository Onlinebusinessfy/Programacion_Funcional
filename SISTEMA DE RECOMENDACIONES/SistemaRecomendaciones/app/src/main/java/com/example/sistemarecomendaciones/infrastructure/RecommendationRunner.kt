package com.example.sistemarecomendaciones.infrastructure

import com.example.sistemarecomendaciones.recommendation.generateRecommendations
import com.example.sistemarecomendaciones.scoring.defaultScoringRules

fun main() {

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


    println(result)
}