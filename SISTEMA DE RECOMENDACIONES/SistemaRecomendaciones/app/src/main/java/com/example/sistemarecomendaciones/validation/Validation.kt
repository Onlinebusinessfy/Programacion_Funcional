package com.example.sistemarecomendaciones.validation

import com.example.sistemarecomendaciones.domain.Interaction
import com.example.sistemarecomendaciones.domain.InteractionType
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.User

typealias ValidationRule<T> = (T) -> List<String>

fun <T> validate(
    value: T,
    rules: List<ValidationRule<T>>
): AppResult<T> {
    val errors = rules.flatMap { rule ->
        rule(value)
    }

    return if (errors.isEmpty()) {
        AppResult.Success(value)
    } else {
        AppResult.Failure(errors)
    }
}

val validUserId: ValidationRule<User> = { user ->
    if (user.id > 0) {
        emptyList()
    } else {
        listOf("El identificador del usuario debe ser positivo.")
    }
}

val validUserName: ValidationRule<User> = { user ->
    if (user.name.isNotBlank()) {
        emptyList()
    } else {
        listOf("El nombre del usuario no puede estar vacío.")
    }
}

val hasPreferredCategories: ValidationRule<User> = { user ->
    if (user.preferredCategories.isNotEmpty()) {
        emptyList()
    } else {
        listOf("El usuario debe tener al menos una categoría preferida.")
    }
}

val hasNoConflictingCategories: ValidationRule<User> = { user ->
    val conflicts =
        user.preferredCategories.intersect(
            user.blockedCategories
        )

    if (conflicts.isEmpty()) {
        emptyList()
    } else {
        listOf(
            "Una categoría no puede estar al mismo tiempo en preferencias y bloqueos."
        )
    }
}

val userValidationRules: List<ValidationRule<User>> = listOf(
    validUserId,
    validUserName,
    hasPreferredCategories,
    hasNoConflictingCategories
)

fun validateUser(
    user: User
): AppResult<User> =
    validate(
        value = user,
        rules = userValidationRules
    )

val validProductId: ValidationRule<Product> = { product ->
    if (product.id > 0) {
        emptyList()
    } else {
        listOf("El identificador del producto debe ser positivo.")
    }
}

val validProductName: ValidationRule<Product> = { product ->
    if (product.name.isNotBlank()) {
        emptyList()
    } else {
        listOf("El nombre del producto no puede estar vacío.")
    }
}

val validProductPrice: ValidationRule<Product> = { product ->
    if (product.price > 0.0) {
        emptyList()
    } else {
        listOf("El precio del producto debe ser mayor que cero.")
    }
}

val validProductRating: ValidationRule<Product> = { product ->
    if (product.rating in 0.0..5.0) {
        emptyList()
    } else {
        listOf("La calificación debe estar entre 0 y 5.")
    }
}

val validProductStock: ValidationRule<Product> = { product ->
    if (product.stock >= 0) {
        emptyList()
    } else {
        listOf("El stock no puede ser negativo.")
    }
}

val validProductCategory: ValidationRule<Product> = { product ->
    if (product.category.isNotBlank()) {
        emptyList()
    } else {
        listOf("La categoría del producto no puede estar vacía.")
    }
}

val productValidationRules: List<ValidationRule<Product>> = listOf(
    validProductId,
    validProductName,
    validProductPrice,
    validProductRating,
    validProductStock,
    validProductCategory
)

fun validateProduct(
    product: Product
): AppResult<Product> =
    validate(
        value = product,
        rules = productValidationRules
    )

fun interactionValidationRules(
    users: Set<Int>,
    products: Map<Int, Product>,
    currentTime: Long
): List<ValidationRule<Interaction>> =
    listOf(

        { interaction ->
            if (interaction.userId in users) {
                emptyList()
            } else {
                listOf("El usuario de la interacción no existe.")
            }
        },

        { interaction ->
            if (interaction.productId in products) {
                emptyList()
            } else {
                listOf("El producto de la interacción no existe.")
            }
        },

        { interaction ->
            if (
                interaction.timestamp > 0L &&
                interaction.timestamp <= currentTime
            ) {
                emptyList()
            } else {
                listOf("El timestamp de la interacción no es válido.")
            }
        },

        { interaction ->
            val product = products[interaction.productId]

            if (
                interaction.type != InteractionType.PURCHASE ||
                product == null ||
                product.stock > 0
            ) {
                emptyList()
            } else {
                listOf(
                    "No se puede registrar una compra de un producto sin stock."
                )
            }
        }
    )

fun validateInteraction(
    interaction: Interaction,
    users: Set<Int>,
    products: Map<Int, Product>,
    currentTime: Long
): AppResult<Interaction> =
    validate(
        value = interaction,
        rules = interactionValidationRules(
            users = users,
            products = products,
            currentTime = currentTime
        )
    )