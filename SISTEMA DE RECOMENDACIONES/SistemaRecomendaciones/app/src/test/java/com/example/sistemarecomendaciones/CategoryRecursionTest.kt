package com.example.sistemarecomendaciones

import com.example.sistemarecomendaciones.domain.Category
import com.example.sistemarecomendaciones.recommendation.flattenCategories
import org.junit.Test
import kotlin.test.assertEquals


class CategoryRecursionTest {


    @Test
    fun flattenCategories_returnsAllChildren() {


        val category =
            Category(
                name = "Tecnologia",
                children =
                    listOf(
                        Category(
                            name = "Computadoras",
                            children =
                                listOf(
                                    Category(
                                        name = "Laptops",
                                        children = emptyList()
                                    )
                                )
                        )
                    )
            )


        val result =
            flattenCategories(category)


        assertEquals(
            3,
            result.size
        )

        assertEquals(
            listOf(
                "Tecnologia",
                "Computadoras",
                "Laptops"
            ),
            result.map {
                it.name
            }
        )
    }
}