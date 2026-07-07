package com.example.examen2

data class Medicamento(
    val nombre: String,
    val laboratorio: String?,
    val categoria: String?,
    val precio: Double,
    val inventario: Int,
    val fechaCaducidad: String?,
    val requiereReceta: Boolean
)

fun clasificarInventario(cantidad: Int): String =
    when {
        cantidad >= 50 -> "Inventario suficiente"
        cantidad >= 20 -> "Inventario medio"
        cantidad >= 1 -> "Inventario bajo"
        else -> "Agotado"
    }

fun enRiesgo(m: Medicamento): Boolean {
    return m.inventario < 20 ||
            m.inventario == 0 ||
            m.categoria == null ||
            m.laboratorio == null ||
            m.fechaCaducidad == null
}

fun main() {

    val medicamentos = listOf(
        Medicamento("Aspirina", "Bayer", "Analgésico", 30.0, 0, "2027-06-18", false),
        Medicamento("Amoxicilina", "Pfizer", "Antibiótico", 120.0, 15, "2026-08-20", true),
        Medicamento("Ibuprofeno", "Bayer", "Antiinflamatorio", 80.0, 50, "2026-12-01", false),
        Medicamento("Omeprazol", null, "Gastrointestinal", 100.0, 10, "2027-01-10", true),
        Medicamento("Paracetamol", "Genfar", "Analgésico", 50.0, 80, "2027-03-15", false),
        Medicamento("Loratadina", "MK", null, 60.0, 40, "2026-09-30", false),
        Medicamento("Metformina", "Novartis", "Diabetes", 130.0, 0, "2026-11-15", true),
        Medicamento("Diclofenaco", null, null, 90.0, 5, null, false),
        Medicamento("Losartán", "Sandoz", "Hipertensión", 130.0, 18, null, true),
        Medicamento("Salbutamol", "GSK", "Respiratorio", 200.0, 55, "2027-12-25", true),
        Medicamento("Vitamina C", "Nature's", "Suplemento", 60.0, 70, "2028-05-12", false),
        Medicamento("Insulina", "Novo Nordisk", "Diabetes", 500.0, 40, "2026-10-01", true),
    )

    println("FARMACIA CESUN:\n")

    println("1: LISTA COMPLETA DE LOS MEDICAMENTOS: \n")

    medicamentos.forEach {

        println("Medicamento: ${it.nombre}")
        println("Laboratorio: ${it.laboratorio ?: "Laboratorio no registrado"}")
        println("Categoría: ${it.categoria ?: "Categoría no registrada"}")
        println("Precio: $${it.precio}")
        println("Inventario: ${it.inventario}")
        println("Estado: ${clasificarInventario(it.inventario)}")
        println("Caducidad: ${it.fechaCaducidad ?: "Fecha de caducidad pendiente"}")
        println("Requiere receta: ${if(it.requiereReceta) "Sí" else "No"}")
        println("*******************************************************")
    }

    println("\n2. MEDICAMENTOS EN RIESGO: \n")

    val riesgo = medicamentos.filter { enRiesgo(it) }

    riesgo.forEach {
        println("- ${it.nombre}")
    }

    val valorInventario = medicamentos
        .map { it.precio * it.inventario }
        .sum()

    val precioPromedio = medicamentos
        .map { it.precio }
        .average()

    val requierenReceta = medicamentos.count { it.requiereReceta }

    println("\n3. MEDICAMENTOS AGRUPADOS POR CATEGORÍA\n")

    medicamentos.groupBy { it.categoria ?: "Sin categoría" }
        .forEach { (categoria, lista) ->

            println("\n$categoria")

            lista.forEach {
                println(" - ${it.nombre}")
            }
        }

    println("\nRESUMEN:")

    println("Total de medicamentos: ${medicamentos.size}")

    println("Medicamentos con categoría registrada: ${
        medicamentos.count { it.categoria != null }
    }")

    println("Medicamentos sin categoría: ${
        medicamentos.count { it.categoria == null }
    }")

    println("Medicamentos sin laboratorio: ${
        medicamentos.count { it.laboratorio == null }
    }")

    println("Medicamentos sin fecha de caducidad: ${
        medicamentos.count { it.fechaCaducidad == null }
    }")

    println("Medicamentos agotados: ${
        medicamentos.count { it.inventario == 0 }
    }")

    println("Medicamentos con inventario bajo: ${
        medicamentos.count { it.inventario in 1..19 }
    }")

    println("Precio promedio: $${"%.2f".format(precioPromedio)}")

    println("Valor total del inventario: $${"%.2f".format(valorInventario)}")

    println("Medicamentos que requieren receta: $requierenReceta")

    val porcentajeRiesgo = riesgo.size.toDouble() / medicamentos.size * 100

    println("Porcentaje de medicamentos en riesgo: ${
        "%.2f".format(porcentajeRiesgo)
    }%")
}