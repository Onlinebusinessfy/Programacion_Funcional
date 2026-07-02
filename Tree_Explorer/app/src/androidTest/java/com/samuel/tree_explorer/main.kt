package com.samuel.tree_explorer

data class Empleado(
    val nombre: String,
    val puesto: String,
    val correo: String? = null,
    val subordinados: MutableList<Empleado> = mutableListOf()
)

fun crearOrganizacion(): Empleado {

    val director = Empleado(
        "Samuel Dominguez",
        "Director General",
        "director@empresa.com"
    )

    for (i in 1..3) {

        val gerente = Empleado(
            "Gerente $i",
            "Gerente",
            "gerente$i@empresa.com"
        )

        for (j in 1..2) {

            val supervisor = Empleado(
                "Supervisor $i.$j",
                "Supervisor",
                if (j == 2) null else "supervisor$i$j@empresa.com"
            )

            for (k in 1..2) {

                val empleado = Empleado(
                    "Empleado $i.$j.$k",
                    "Empleado",
                    if (k == 2) null else "empleado$i$j$k@empresa.com"
                )

                supervisor.subordinados.add(empleado)
            }

            gerente.subordinados.add(supervisor)
        }

        director.subordinados.add(gerente)
    }

    return director
}

fun mostrarArbol(empleado: Empleado, nivel: Int = 0) {

    println("${"    ".repeat(nivel)}- ${empleado.nombre} (${empleado.puesto})")

    for (sub in empleado.subordinados) {
        mostrarArbol(sub, nivel + 1)
    }
}

fun buscarEmpleado(empleado: Empleado, nombre: String): Empleado? {

    if (empleado.nombre.equals(nombre, ignoreCase = true)) {
        return empleado
    }

    for (sub in empleado.subordinados) {

        val encontrado = buscarEmpleado(sub, nombre)

        if (encontrado != null) {
            return encontrado
        }
    }

    return null
}

fun contarEmpleados(empleado: Empleado): Int {

    var total = 1

    for (sub in empleado.subordinados) {
        total += contarEmpleados(sub)
    }

    return total
}

fun profundidad(empleado: Empleado): Int {

    if (empleado.subordinados.isEmpty()) {
        return 1
    }

    var maxProfundidad = 0

    for (sub in empleado.subordinados) {
        val profundidadActual = profundidad(sub)

        if (profundidadActual > maxProfundidad) {
            maxProfundidad = profundidadActual
        }
    }

    return maxProfundidad + 1
}

fun empleadosSinCorreo(
    empleado: Empleado,
    lista: MutableList<Empleado>
) {

    if (empleado.correo == null) {
        lista.add(empleado)
    }

    for (sub in empleado.subordinados) {
        empleadosSinCorreo(sub, lista)
    }
}

fun obtenerTodosLosEmpleados(
    empleado: Empleado,
    lista: MutableList<Empleado>
) {

    lista.add(empleado)

    for (sub in empleado.subordinados) {
        obtenerTodosLosEmpleados(sub, lista)
    }
}

fun main() {

    val empresa = crearOrganizacion()

    println("====================================")
    println("   ORGANIZATIONAL TREE EXPLORER")
    println("====================================\n")

    println("ESTRUCTURA ORGANIZACIONAL:\n")
    mostrarArbol(empresa)

    val totalEmpleados = contarEmpleados(empresa)

    println("\n------------------------------------")
    println("TOTAL DE EMPLEADOS")
    println("------------------------------------")
    println(totalEmpleados)

    println("\n------------------------------------")
    println("NIVELES DE LA ORGANIZACION")
    println("------------------------------------")
    println(profundidad(empresa))

    println("\n------------------------------------")
    println("BUSQUEDA DE EMPLEADO")
    println("------------------------------------")

    val nombreBuscar = "Empleado 2.1.1"

    val encontrado = buscarEmpleado(
        empresa,
        nombreBuscar
    )

    if (encontrado != null) {
        println("Empleado encontrado:")
        println("Nombre: ${encontrado.nombre}")
        println("Puesto: ${encontrado.puesto}")
        println("Correo: ${encontrado.correo ?: "No registrado"}")
    } else {
        println("Empleado no encontrado")
    }

    println("\n------------------------------------")
    println("EMPLEADOS SIN CORREO")
    println("------------------------------------")

    val sinCorreo = mutableListOf<Empleado>()

    empleadosSinCorreo(
        empresa,
        sinCorreo
    )

    sinCorreo.forEach {
        println("${it.nombre} - ${it.puesto}")
    }

    println("\n------------------------------------")
    println("ESTADISTICAS")
    println("------------------------------------")

    val promedioPorArea =
        empresa.subordinados.sumOf {
            contarEmpleados(it)
        }.toDouble() / empresa.subordinados.size

    println("Total de empleados: $totalEmpleados")
    println("Promedio de empleados por area: %.2f".format(promedioPorArea))
    println("Profundidad maxima: ${profundidad(empresa)} niveles")

    println("\n------------------------------------")
    println("REPRESENTACION COMO LISTA")
    println("------------------------------------")

    val lista = mutableListOf<Empleado>()

    obtenerTodosLosEmpleados(
        empresa,
        lista
    )

    lista.forEach {
        println("${it.nombre} - ${it.puesto}")
    }

    println("\n------------------------------------")
    println("ANALISIS")
    println("------------------------------------")

    println(
        """
La estructura tipo arbol es la mas adecuada para representar
una organizacion empresarial porque refleja naturalmente las
relaciones jerarquicas entre director, gerentes, supervisores
y empleados.

Una lista tradicional permite almacenar los datos de forma
secuencial, pero no muestra facilmente quien depende de quien.
En cambio, un arbol permite visualizar la jerarquia, calcular
niveles, buscar subordinados y recorrer la organizacion de
forma eficiente mediante recursividad.

Por estas razones, la estructura tipo arbol resulta superior
para representar organigramas empresariales.
        """.trimIndent()
    )

    println("\n====================================")
    println("FIN DEL REPORTE")
    println("====================================")
}