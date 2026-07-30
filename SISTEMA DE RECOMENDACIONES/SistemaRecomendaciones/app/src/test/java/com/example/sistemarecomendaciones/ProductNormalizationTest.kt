package com.example.sistemarecomendaciones

import com.example.sistemarecomendaciones.domain.Product
import com.example.sistemarecomendaciones.normalization.normalizeCategory
import com.example.sistemarecomendaciones.normalization.normalizeName
import com.example.sistemarecomendaciones.normalization.normalizeProduct
import com.example.sistemarecomendaciones.normalization.normalizeTags
import com.example.sistemarecomendaciones.normalization.roundPrice
import com.example.sistemarecomendaciones.normalization.then
import org.junit.Test
import kotlin.test.assertEquals

class ProductNormalizationTest {

    private val product =
        Product(
            id = 1,
            name = "  laptop    GAMER  ",
            category = "  TECNOLOGIA COMPUTADORAS ",
            price = 19999.999,
            rating = 4.8,
            stock = 5,
            tags =
                setOf(
                    " Gaming ",
                    " gaming",
                    "",
                    " Alta   Gama "
                )
        )


    @Test
    fun normalizeName_removesSpacesAndFormatsText() {

        val result =
            normalizeName(product)

        assertEquals(
            "Laptop gamer",
            result.name
        )
    }


    @Test
    fun normalizeCategory_convertsToLowercase() {

        val result =
            normalizeCategory(product)

        assertEquals(
            "tecnologia computadoras",
            result.category
        )
    }


    @Test
    fun normalizeTags_removesEmptyAndDuplicates() {

        val result =
            normalizeTags(product)

        assertEquals(
            setOf(
                "gaming",
                "alta gama"
            ),
            result.tags
        )
    }


    @Test
    fun roundPrice_roundsToTwoDecimals() {

        val result =
            roundPrice(product)

        assertEquals(
            20000.0,
            result.price
        )
    }


    @Test
    fun normalizeProduct_executesCompletePipeline() {

        val result =
            normalizeProduct(product)

        assertEquals(
            "Laptop gamer",
            result.name
        )

        assertEquals(
            "tecnologia computadoras",
            result.category
        )

        assertEquals(
            setOf(
                "gaming",
                "alta gama"
            ),
            result.tags
        )

        assertEquals(
            20000.0,
            result.price
        )
    }


    @Test
    fun thenComposition_appliesFunctionsInOrder() {

        val customPipeline =
            ::normalizeName then
                    ::normalizeCategory

        val result =
            customPipeline(product)

        assertEquals(
            "Laptop gamer",
            result.name
        )

        assertEquals(
            "tecnologia computadoras",
            result.category
        )
    }
}