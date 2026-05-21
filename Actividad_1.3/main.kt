fun main() {

    // Parte 1

    val nombreCurso = "Programacion Funcional"
    val nombreProfesor = "Tona Castro Claudia Gabriel"
    var maximoEstudiantes = 5

    println("CONTROL DE ALUMNOS")
    println("Materia asignada: $nombreCurso")
    println("Docente encargado: $nombreProfesor")
    println("Cantidad limite de alumnos: $maximoEstudiantes")


    // Parte 2

    val listaEstudiantes: MutableList<String> = mutableListOf()

    println("Captura de alumnos")

    var i = 1
    while (i <= maximoEstudiantes) {

        print("Escribe el nombre del alumno $i: ")

        val nombre = readLine() ?: ""

        listaEstudiantes.add(nombre)

        i++
    }

    println()
    println("Alumnos almacenados en el sistema:")

    for (estudiante in listaEstudiantes) {

        println("• $estudiante")
    }

    println()


    // Parte 3

    val mapaCalificaciones: MutableMap<String, Double> = mutableMapOf()

    println("Registro de notas")

    for (estudiante in listaEstudiantes) {

        print("Ingresa la nota final de $estudiante: ")

        val entrada = readLine()

        val calificacion = entrada?.toDoubleOrNull() ?: 0.0

        mapaCalificaciones[estudiante] = calificacion
    }

    println()


    // Parte 4

    var totalAprobados = 0
    var totalReprobados = 0

    println("Estado academico")

    for (estudiante in listaEstudiantes) {

        val calificacion = mapaCalificaciones[estudiante] ?: 0.0

        var resultado = ""

        if (calificacion >= 70) {

            resultado = "Aprobado"
            totalAprobados++

        } else {

            resultado = "Reprobado"
            totalReprobados++
        }

        println("Alumno: $estudiante | Nota: $calificacion | Estado: $resultado")
    }

    println()


    // Parte 5

    val setCiudades: MutableSet<String> = mutableSetOf()

    println("Lugar de procedencia")

    for (estudiante in listaEstudiantes) {

        print("Escribe la ciudad de $estudiante: ")

        val ciudad = readLine() ?: ""

        setCiudades.add(ciudad)
    }

    println()
    println("Ciudades registradas dentro del sistema:")

    for (ciudad in setCiudades) {

        println("• $ciudad")
    }

    println("Numero total de ciudades distintas: ${setCiudades.size}")

    println()


    // Parte 6

    var suma = 0.0

    for (cal in mapaCalificaciones.values) {

        suma = suma + cal
    }

    val promedio = suma / listaEstudiantes.size

    println("REPORTE FINAL")

    println("Asignatura: $nombreCurso")

    println("Profesor responsable: $nombreProfesor")

    println("Total de alumnos registrados: ${listaEstudiantes.size}")

    println()

    println("Listado general de alumnos:")

    for (estudiante in listaEstudiantes) {

        println("• $estudiante")
    }

    println()

    println("Promedio del grupo: $promedio")

    println("Estudiantes aprobados: $totalAprobados")

    println("Estudiantes reprobados: $totalReprobados")

    println("Fin del reporte")
}