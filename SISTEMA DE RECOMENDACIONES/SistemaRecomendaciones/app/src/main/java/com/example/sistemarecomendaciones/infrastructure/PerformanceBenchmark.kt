package com.example.sistemarecomendaciones.infrastructure

import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.User
import com.example.sistemarecomendaciones.normalization.normalizeProduct
import kotlin.system.measureTimeMillis

data class PerformanceResult(
    val listTimeMillis: Long,
    val sequenceTimeMillis: Long,
    val listResultCount: Int,
    val sequenceResultCount: Int
)

fun processWithList(
    user: User,
    products: List<Product>,
    limit: Int
): List<Product> =
    products
        .map(normalizeProduct)
        .filter { product ->
            product.stock > 0
        }
        .filterNot { product ->
            product.category in user.blockedCategories
        }
        .sortedByDescending { product ->
            product.rating
        }
        .take(limit)

fun processWithSequence(
    user: User,
    products: List<Product>,
    limit: Int
): List<Product> =
    products
        .asSequence()
        .map(normalizeProduct)
        .filter { product ->
            product.stock > 0
        }
        .filterNot { product ->
            product.category in user.blockedCategories
        }
        .sortedByDescending { product ->
            product.rating
        }
        .take(limit)
        .toList()

fun compareListAndSequence(
    user: User,
    products: List<Product>,
    limit: Int
): PerformanceResult {

    val listResult =
        processWithList(
            user = user,
            products = products,
            limit = limit
        )

    val listTime =
        measureTimeMillis {
            processWithList(
                user = user,
                products = products,
                limit = limit
            )
        }

    val sequenceResult =
        processWithSequence(
            user = user,
            products = products,
            limit = limit
        )

    val sequenceTime =
        measureTimeMillis {
            processWithSequence(
                user = user,
                products = products,
                limit = limit
            )
        }

    return PerformanceResult(
        listTimeMillis = listTime,
        sequenceTimeMillis = sequenceTime,
        listResultCount = listResult.size,
        sequenceResultCount = sequenceResult.size
    )
}