package com.samuel.actividad22

data class Producto(
    val nombre: String,
    val categoria: String?,
    val precio: Double,
    val existencia: Int
)

fun main() {

    val productos = listOf(
        Producto("Laptop", "Electrónica", 15000.0, 10),
        Producto("Mouse", "Electrónica", 250.0, 5),
        Producto("Teclado", "Electrónica", 500.0, 2),
        Producto("Monitor", "Electrónica", 4000.0, 0),
        Producto("Silla", "Muebles", 1800.0, 8),
        Producto("Escritorio", "Muebles", 3500.0, 1),
        Producto("Cuaderno", "Papelería", 50.0, 25),
        Producto("Lápiz", "Papelería", 10.0, 100),
        Producto("Mochila", "Papelería", 600.0, 4),
        Producto("Impresora", "Electrónica", 2500.0, 3),
        Producto("Cafetera", "Hogar", 1200.0, 6),
        Producto("Ventilador", "Hogar", 900.0, 0),
        Producto("Lampara", "Hogar", 300.0, 7),
        Producto("Audífonos", null, 800.0, 2),
        Producto("Tablet", null, 7000.0, 0)
    )

    println("***** LISTADO DE PRODUCTOS *****")
    productos.forEach { println(it) }

    println("\n***** CLASIFICACIÓN POR PRECIO *****")
    productos.forEach {
        val rango = when {
            it.precio < 500 -> "Bajo"
            it.precio <= 3000 -> "Medio"
            else -> "Alto"
        }
        println("${it.nombre}: $rango")
    }

    println("\n***** PRODUCTOS AGOTADOS *****")
    productos.filter { it.existencia == 0 }
        .forEach { println(it.nombre) }

    println("\n***** INVENTARIO BAJO (<5) *****")
    productos.filter { it.existencia in 1..4 }
        .forEach { println("${it.nombre} (${it.existencia})") }

    println("\n***** AGRUPADOS POR CATEGORÍA *****")
    productos.groupBy { it.categoria ?: "Sin categoría" }
        .forEach { (categoria, lista) ->
            println("\n$categoria")
            lista.forEach { println("- ${it.nombre}") }
        }

    println("\n***** PRODUCTOS SIN CATEGORÍA *****")
    productos.filter { it.categoria == null }
        .forEach { println(it.nombre) }

    val valorTotal = productos.sumOf { it.precio * it.existencia }
    val precioPromedio = productos.map { it.precio }.average()
    val masCaro = productos.maxByOrNull { it.precio }
    val menorExistencia = productos.minByOrNull { it.existencia }

    println("\n***** ESTADÍSTICAS *****")
    println("Valor total inventario: $$valorTotal")
    println("Precio promedio: $precioPromedio")
    println("Producto más caro: ${masCaro?.nombre}")
    println("Menor existencia: ${menorExistencia?.nombre} (${menorExistencia?.existencia})")

    val reposicion = productos.any { it.existencia <= 2 }

    println("\n¿Existen productos que requieren reposición inmediata?")
    println(if (reposicion) "Sí" else "No")

    println("\n***** REPORTE FINAL *****")
    println("Total de productos: ${productos.size}")
    println("Agotados: ${productos.count { it.existencia == 0 }}")
    println("Inventario bajo: ${productos.count { it.existencia in 1..4 }}")
    println("Sin categoría: ${productos.count { it.categoria == null }}")

    println("\n***** RESUMEN POR CATEGORÍA *****")
    productos.groupBy { it.categoria ?: "Sin categoría" }
        .forEach { (categoria, lista) ->
            println(
                "$categoria -> Productos: ${lista.size}, " +
                        "Existencias: ${lista.sumOf { it.existencia }}"
            )
        }
}