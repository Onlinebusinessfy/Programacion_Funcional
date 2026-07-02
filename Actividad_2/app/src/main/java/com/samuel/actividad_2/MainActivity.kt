package com.samuel.actividad_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var resultado by remember { mutableStateOf("") }

            var precioTexto by remember { mutableStateOf("") }
            var cantidadTexto by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Button(
                    onClick = {

                        val salida = StringBuilder()

                        var nivel_usuario = "Premium"

                        when (nivel_usuario) {
                            "Basico" -> {
                                salida.append("Eres usuario Basico\n")
                            }

                            "Premium" -> {
                                salida.append("Eres usuario Premium!\n")
                            }

                            "VIP" -> {
                                salida.append("Eres usuario VIP!!\n")
                            }

                            else -> {
                                salida.append("No tienes membresia\n")
                            }
                        }

                        var num_dia = 2

                        when (num_dia) {
                            1 -> salida.append("Lunes\n")
                            2 -> salida.append("Martes\n")
                            3 -> salida.append("Miercoles\n")
                            4 -> salida.append("Jueves\n")
                            5 -> salida.append("Viernes\n")
                            6 -> salida.append("Sabado\n")
                            7 -> salida.append("Domingo\n")
                            else -> salida.append("Ese dia no existe\n")
                        }

                        var calificacion = 100

                        when (calificacion) {
                            in 0..69 -> salida.append("Reprobado\n")
                            in 70..80 -> salida.append("Aprobado\n")
                            in 81..100 -> salida.append("Calificacion perfecta\n")
                            else -> salida.append("Sin calificacion\n")
                        }

                        for (numero in 1..10) {
                            salida.append("$numero\n")
                        }

                        for (numero in 10 downTo 1) {
                            salida.append("$numero\n")
                        }

                        for (numero in 1..10 step 2) {
                            salida.append("$numero\n")
                        }

                        val nombre = "Samuel"
                        var edad = 20

                        salida.append("Hola, soy $nombre y tengo $edad años\n")

                        var materias: List<String> = listOf(
                            "Backend III",
                            "FrontEnd II",
                            "Apps Mobiles",
                            "Pruebas Funcionales",
                            "Orientada a pruebas"
                        )

                        salida.append("$materias\n")

                        if (edad % 2 == 0) {
                            salida.append("El numero es par\n")
                        } else {
                            salida.append("El numero es impar\n")
                        }

                        val precio = precioTexto.toIntOrNull() ?: 0
                        val cantidad = cantidadTexto.toIntOrNull() ?: 0

                        val total = precio * cantidad

                        salida.append("Total: $total\n")

                        if (total > 100) {
                            salida.append("Aplica descuento\n")
                        } else {
                            salida.append("No aplica descuento\n")
                        }

                        val peliculas = mutableSetOf(
                            "Avengers",
                            "Dora",
                            "Jumanji",
                            "Tron"
                        )

                        salida.append("$peliculas\n")

                        salida.append("El tamaño de este set es: ${peliculas.size}\n")

                        val estudiante = mapOf(
                            "nombre" to "Samuel",
                            "carrera" to "Ing. Desarrollo de Software",
                            "escuela" to "CESUN"
                        )

                        salida.append("$estudiante\n")

                        val paises = mapOf(
                            "Mexico" to "Ciudad de Mexico",
                            "España" to "Madrid",
                            "Estados Unidos" to "Washington DC",
                            "Francia" to "Paris",
                            "Sudafrica" to "Pretoria"
                        )

                        salida.append("$paises\n")

                        for ((pais, capital) in paises) {
                            salida.append("$pais -> $capital\n")
                        }

                        peliculas.add("Batman")

                        salida.append("$peliculas\n")

                        resultado = salida.toString()
                    }
                ) {
                    Text("Ejecutar Código")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = precioTexto,
                    onValueChange = { precioTexto = it },
                    label = { Text("Ingresa el precio") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = cantidadTexto,
                    onValueChange = { cantidadTexto = it },
                    label = { Text("Ingresa la cantidad") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(resultado)
            }
        }
    }
}
