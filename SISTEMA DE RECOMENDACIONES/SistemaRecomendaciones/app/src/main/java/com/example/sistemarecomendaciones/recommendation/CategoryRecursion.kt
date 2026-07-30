package com.example.sistemarecomendaciones.recommendation

import com.example.sistemarecomendaciones.domain.Category

fun flattenCategories(
    category: Category
): List<Category> =
    listOf(category) +
            category.children.flatMap { child ->
                flattenCategories(child)
            }