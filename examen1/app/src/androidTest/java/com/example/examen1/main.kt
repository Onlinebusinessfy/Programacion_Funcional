package com.example.examen1

data class Student(val nombre: String, val calificacion: Double, val faltas: Int)

val estudiantes = listOf(
    Student("Samuel", 100.0, 0),
    Student("Christian", 95.0, 3),
    Student("Alejandro", 60.0, 5),
    Student("Esteban", 80.0, 3),
    Student("Alan", 40.0, 0)
)

val aprobados = estudiantes.filter {
    it.calificacion >= 70
}

val riesgo = estudiantes.filter {
    it.calificacion < 70 || it.faltas > 3
}

val listaEstudiantes = estudiantes.map{
    it.nombre
}

val promedio = estudiantes.map {
    it.calificacion
}.average()

fun main(){
    println("Estudiantes:")
    estudiantes.forEach {
        println(it)
    }

    println("Estudiantes aprobados:")
    println(aprobados)

    println("Estudiantes en riesgo:")
    println(riesgo)

    println("Lista de Estudiantes con map:")
    println(listaEstudiantes)

    println("Promedio del grupo: $promedio")

    if (promedio >= 80){
        println("El rendimiento del grupo es bueno")
    } else {
        println("El grupo necesita mejorar")
    }
}