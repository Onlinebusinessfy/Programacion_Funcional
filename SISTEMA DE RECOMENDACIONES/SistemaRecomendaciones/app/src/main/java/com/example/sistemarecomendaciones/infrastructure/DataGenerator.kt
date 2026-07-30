package com.example.sistemarecomendaciones.infrastructure

import com.example.sistemarecomendaciones.domain.Interaction
import com.example.sistemarecomendaciones.domain.InteractionType
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.User

data class GeneratedData(
    val users: List<User>,
    val products: List<Product>,
    val interactions: List<Interaction>
)

data class DataGeneratorConfig(
    val userCount: Int = 1_000,
    val productCount: Int = 10_000,
    val interactionCount: Int = 100_000
)

fun generateUsers(
    count: Int
): List<User> {

    val categories = listOf(
        "tecnología",
        "videojuegos",
        "ropa",
        "hogar",
        "deportes",
        "libros",
        "música",
        "salud"
    )

    return (1..count).map { id ->

        val preferredCategories =
            categories
                .filterIndexed { index, _ ->
                    (id + index) % 3 == 0
                }
                .take(3)
                .toSet()

        val safePreferredCategories =
            if (preferredCategories.isEmpty()) {
                setOf(
                    categories[id % categories.size]
                )
            } else {
                preferredCategories
            }

        val blockedCategories =
            categories
                .filterIndexed { index, category ->
                    (id + index) % 7 == 0 &&
                            category !in safePreferredCategories
                }
                .take(2)
                .toSet()

        User(
            id = id,
            name = "Usuario $id",
            preferredCategories =
                safePreferredCategories,
            blockedCategories =
                blockedCategories
        )
    }
}

fun generateProducts(
    count: Int
): List<Product> {

    val categories = listOf(
        "tecnología",
        "videojuegos",
        "ropa",
        "hogar",
        "deportes",
        "libros",
        "música",
        "salud"
    )

    val tags = listOf(
        "nuevo",
        "popular",
        "oferta",
        "premium",
        "económico",
        "moderno",
        "digital",
        "profesional",
        "hogar",
        "entretenimiento"
    )

    return (1..count).map { id ->

        val category =
            categories[id % categories.size]

        val productTags =
            tags
                .filterIndexed { index, _ ->
                    (id + index) % 4 == 0
                }
                .take(4)
                .toSet()

        Product(
            id = id,
            name = "Producto $id",
            category = category,
            price =
                50.0 + ((id * 37) % 5_000),
            rating =
                1.0 + ((id * 13) % 41) / 10.0,
            stock =
                if (id % 15 == 0) {
                    0
                } else {
                    1 + (id % 100)
                },
            tags =
                if (productTags.isEmpty()) {
                    setOf("general")
                } else {
                    productTags
                }
        )
    }
}

fun generateInteractions(
    userCount: Int,
    productCount: Int,
    interactionCount: Int,
    clock: () -> Long
): List<Interaction> {

    val interactionTypes =
        InteractionType.entries

    val currentTime =
        clock()

    return (1..interactionCount).map { id ->

        val type =
            interactionTypes[
                id % interactionTypes.size
            ]

        Interaction(
            userId =
                ((id * 17) % userCount) + 1,

            productId =
                ((id * 31) % productCount) + 1,

            type = type,

            timestamp =
                currentTime -
                        ((id % 30) * 86_400_000L)
        )
    }
}

fun generateData(
    config: DataGeneratorConfig,
    clock: () -> Long
): GeneratedData {

    val users =
        generateUsers(
            config.userCount
        )

    val products =
        generateProducts(
            config.productCount
        )

    val interactions =
        generateInteractions(
            userCount =
                config.userCount,

            productCount =
                config.productCount,

            interactionCount =
                config.interactionCount,

            clock =
                clock
        )

    return GeneratedData(
        users = users,
        products = products,
        interactions = interactions
    )
}