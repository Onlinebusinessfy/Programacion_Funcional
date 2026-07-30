package com.example.sistemarecomendaciones

import com.example.sistemarecomendaciones.domain.Interaction
import com.example.sistemarecomendaciones.domain.InteractionType
import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.domain.User
import com.example.sistemarecomendaciones.validation.AppResult
import com.example.sistemarecomendaciones.validation.validateInteraction
import com.example.sistemarecomendaciones.validation.validateProduct
import com.example.sistemarecomendaciones.validation.validateUser
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ValidationTest {

    @Test
    fun validUser_returnsSuccess() {

        val user =
            User(
                id = 1,
                name = "Samuel",
                preferredCategories =
                    setOf("Tecnología"),
                blockedCategories =
                    emptySet()
            )

        val result =
            validateUser(user)

        assertIs<AppResult.Success<User>>(
            result
        )

        assertEquals(
            user,
            result.value
        )
    }

    @Test
    fun invalidUser_accumulatesMultipleErrors() {

        val user =
            User(
                id = 0,
                name = "",
                preferredCategories =
                    emptySet(),
                blockedCategories =
                    emptySet()
            )

        val result =
            validateUser(user)

        assertIs<AppResult.Failure>(
            result
        )

        assertEquals(
            3,
            result.errors.size
        )

        assertTrue(
            result.errors.contains(
                "El identificador del usuario debe ser positivo."
            )
        )

        assertTrue(
            result.errors.contains(
                "El nombre del usuario no puede estar vacío."
            )
        )

        assertTrue(
            result.errors.contains(
                "El usuario debe tener al menos una categoría preferida."
            )
        )
    }

    @Test
    fun userWithConflictingCategories_returnsFailure() {

        val user =
            User(
                id = 1,
                name = "Samuel",
                preferredCategories =
                    setOf(
                        "Tecnología",
                        "Videojuegos"
                    ),
                blockedCategories =
                    setOf(
                        "Videojuegos"
                    )
            )

        val result =
            validateUser(user)

        assertIs<AppResult.Failure>(
            result
        )

        assertTrue(
            result.errors.contains(
                "Una categoría no puede estar al mismo tiempo en preferencias y bloqueos."
            )
        )
    }

    @Test
    fun validProduct_returnsSuccess() {

        val product =
            Product(
                id = 1,
                name = "Laptop",
                category = "Tecnología",
                price = 20000.0,
                rating = 4.8,
                stock = 10,
                tags =
                    setOf(
                        "computadora"
                    )
            )

        val result =
            validateProduct(product)

        assertIs<AppResult.Success<Product>>(
            result
        )

        assertEquals(
            product,
            result.value
        )
    }

    @Test
    fun invalidProduct_accumulatesAllErrors() {

        val product =
            Product(
                id = 0,
                name = "",
                category = "",
                price = 0.0,
                rating = 6.0,
                stock = -1,
                tags =
                    emptySet()
            )

        val result =
            validateProduct(product)

        assertIs<AppResult.Failure>(
            result
        )

        assertEquals(
            6,
            result.errors.size
        )
    }

    @Test
    fun validInteraction_returnsSuccess() {

        val product =
            Product(
                id = 10,
                name = "Mouse",
                category = "Tecnología",
                price = 500.0,
                rating = 4.5,
                stock = 5,
                tags =
                    setOf(
                        "accesorio"
                    )
            )

        val interaction =
            Interaction(
                userId = 1,
                productId = 10,
                type =
                    InteractionType.PURCHASE,
                timestamp = 1000L
            )

        val result =
            validateInteraction(
                interaction =
                    interaction,
                users =
                    setOf(1),
                products =
                    mapOf(
                        10 to product
                    ),
                currentTime =
                    2000L
            )

        assertIs<AppResult.Success<Interaction>>(
            result
        )

        assertEquals(
            interaction,
            result.value
        )
    }

    @Test
    fun purchaseWithoutStock_returnsFailure() {

        val product =
            Product(
                id = 10,
                name = "Producto agotado",
                category = "Tecnología",
                price = 1000.0,
                rating = 4.0,
                stock = 0,
                tags =
                    emptySet()
            )

        val interaction =
            Interaction(
                userId = 1,
                productId = 10,
                type =
                    InteractionType.PURCHASE,
                timestamp = 1000L
            )

        val result =
            validateInteraction(
                interaction =
                    interaction,
                users =
                    setOf(1),
                products =
                    mapOf(
                        10 to product
                    ),
                currentTime =
                    2000L
            )

        assertIs<AppResult.Failure>(
            result
        )

        assertTrue(
            result.errors.contains(
                "No se puede registrar una compra de un producto sin stock."
            )
        )
    }
}