package com.example.funciones_samuel

fun main(){
    saludo()

    saludar(nombre = "Samuel")

    println(numCubo(3))

    println("Resultado: $resultado")

    println(resultadoOperacion)

    println(numerosPares)
    colores.forEach{
        println(it)
    }

    println(nuevaLista)

    println(total)

    println(colores2)

    println(numero)

    println(alumnos)

    println(alumnosActivos)
}

fun saludar(nombre: String){
    println("Hola $nombre")
}
fun suma(num1: Int, num2: Int): Int{
    return num1 + num2
}

val saludo = {println("Hola")}

val numCubo = {num: Int -> num*num*num}

val resultado = suma(10,7)

val resultadoOperacion = operacion(5, 7){x, y -> x+y}

fun operacion(
    num1: Int,
    num2: Int,
    calculo: (Int, Int) -> Int): Int{
    return calculo(num1, num2)
}

val colores = listOf(
    "azul",
    "blanco",
    "amarillo",
    "verde",
    "rojo",
    "cafe",
    "negro",
    "gris"
)

val numeros = listOf(
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8,
    9,
    10,
    11,
    12,
    13,
    14,
    15
)

val numerosPares = numeros.filter{
    it % 2 == 0
}

val nuevaLista = numeros.map{
    it + 3
}

val total = nuevaLista.reduce{ ac, valor -> ac + valor}

val colores2 = colores.sortedBy{
    it
}

val numero = numeros.find{it > 5}

val alumno1 = Alumno(
    "Samuel",
    20,
    "Chula Vista",
    "Masculino",
    true
)

val alumno2 = Alumno(
    "Esteban",
    23,
    "Popotla",
    "Femenino",
    false
)

val alumno3 = Alumno(
    "Christian",
    20,
    "Tijuana",
    "Masculino",
    true
)

val alumnos = listOf(
    alumno1, alumno2, alumno3
)

val alumnosActivos = alumnos.filter{
    it.activo
}