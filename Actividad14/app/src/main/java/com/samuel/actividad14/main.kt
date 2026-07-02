package com.samuel.actividad14

data class Task(
    val name: String,
    val priority: Int,
    val estimatedHours: Int,
    val completed: Boolean
)

val tareas = listOf(
    Task("Hacer tarea de matemáticas", 3, 2, false),
    Task("Estudiar Kotlin", 5, 4, false),
    Task("Completar proyecto de FastAPI", 9, 6, true),
    Task("Diseñar página web", 2, 3, false),
    Task("Subir proyecto a GitHub", 8, 1, true),
    Task("Practicar ejercicios de programación", 5, 5, false),
    Task("Ver tutorial de Android Studio", 3, 2, true),
    Task("Optimizar modelo de Machine Learning", 5, 7, false),
    Task("Actualizar documentación", 9, 1, true),
    Task("Preparar presentación", 10, 3, false)
)

fun main(){

    println("===== TASK MANAGER =====")

    println("\nAll Tasks:")

    tareas.forEach {
        println("Nombre: ${it.name}")
        println("Prioridad: ${it.priority}")
        println("Horas estimadas: ${it.estimatedHours}")
        println("Completado: ${it.completed}")
        println("----------------------")
    }

    println("\nCompleted Tasks:")

    tareas.filter {
        it.completed == true
    }.forEach {
        println("${it.name} - Completada")
    }

    println("\nPending Tasks:")

    tareas.filter {
        it.completed == false
    }.forEach {
        println("${it.name} - Pendiente")
    }

    println("\nTask Names:")

    println(tareas.map { it.name })

    println("\nHigh Priority Tasks:")

    println(
        tareas.filter {
            it.priority >= 8
        }
    )

    println("\nTasks Sorted by Priority:")

    tareas.sortedByDescending {
        it.priority
    }.forEach {
        println("${it.name} - Prioridad ${it.priority}")
    }

    println("\nTotal Estimated Hours:")

    println(
        tareas.sumOf {
            it.estimatedHours
        }
    )

    println("\nAverage Estimated Hours:")

    println(promedio)

    println("\nSearch Result:")

    println("Ingresa el nombre de la tarea:")

    val nombreBuscado = readln()

    val tareaEncontrada = tareas.find {
        it.name.contains(nombreBuscado, ignoreCase = true)
    }

    println(tareaEncontrada)

    println("\nProcessing Tasks:")

    processTasks(tareas) {
        println(it.name)
    }

    println("\nUpdated Task:")

    println("Original: $tareaOriginal")
    println("Nueva: $tareaCompletada")
}

val promedio = tareas.map {
    it.estimatedHours
}.average()

fun processTasks(
    tareas: List<Task>,
    accion: (Task) -> Unit
) {
    tareas.forEach {
        accion(it)
    }
}

val tareaOriginal = tareas[0]

val tareaCompletada = tareaOriginal.copy(
    completed = true
)