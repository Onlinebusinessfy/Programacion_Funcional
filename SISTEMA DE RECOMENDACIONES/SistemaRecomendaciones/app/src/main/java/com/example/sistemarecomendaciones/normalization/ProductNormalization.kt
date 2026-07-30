package com.example.sistemarecomendaciones.normalization

import com.example.sistemarecomendaciones.domain.Product
import kotlin.math.round

infix fun <A, B, C> ((A) -> B).then(
    next: (B) -> C
): (A) -> C = { value ->
    next(this(value))
}

fun normalizeName(
    product: Product
): Product =
    product.copy(
        name = product.name
            .trim()
            .replace(
                Regex("\\s+"),
                " "
            )
            .lowercase()
            .replaceFirstChar { character ->
                character.uppercase()
            }
    )

fun normalizeCategory(
    product: Product
): Product =
    product.copy(
        category = product.category
            .trim()
            .lowercase()
            .replace(
                Regex("\\s+"),
                " "
            )
    )

fun normalizeTags(
    product: Product
): Product =
    product.copy(
        tags = product.tags
            .map { tag ->
                tag
                    .trim()
                    .lowercase()
                    .replace(
                        Regex("\\s+"),
                        " "
                    )
            }
            .filter { tag ->
                tag.isNotBlank()
            }
            .toSet()
    )

fun roundPrice(
    product: Product
): Product =
    product.copy(
        price = round(
            product.price * 100
        ) / 100
    )

val normalizeProduct: (Product) -> Product =
    ::normalizeName then
            ::normalizeCategory then
            ::normalizeTags then
            ::roundPrice