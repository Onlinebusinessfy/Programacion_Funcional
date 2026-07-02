package com.example.booleananalyzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BooleanApp()
        }
    }
}

@Composable
fun BooleanApp() {

    var input by remember { mutableStateOf("") }
    var values by remember { mutableStateOf(listOf<Boolean>()) }
    var results by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Boolean Analyzer", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Escribe TRUE o FALSE") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            val value = input.uppercase() == "TRUE"
            values = values + value
            input = ""
        }) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.height(150.dp)
        ) {
            items(values) { item ->
                Text(item.toString())
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            results = analyze(values)
        }) {
            Text("Analizar")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(results)
    }
}

fun analyze(list: List<Boolean>): String {

    val trueCount = list.count { it }
    val falseCount = list.count { !it }

    val total = list.size.toFloat()

    val truePercent = if (total > 0) (trueCount / total) * 100 else 0f
    val falsePercent = if (total > 0) (falseCount / total) * 100 else 0f

    val allTrue = list.all { it }
    val anyTrue = list.any { it }
    val noneTrue = list.none { it }

    val inverted = list.map { !it }

    val onlyTrue = list.filter { it }
    val onlyFalse = list.filter { !it }

    val sumFold = list.fold(0) { acc, value ->
        acc + if (value) 1 else 0
    }

    return """
        TRUE: $trueCount
        FALSE: $falseCount
        
        TRUE %: $truePercent
        FALSE %: $falsePercent
        
        ALL TRUE: $allTrue
        ANY TRUE: $anyTrue
        NONE TRUE: $noneTrue
        
        INVERTED: $inverted
        
        ONLY TRUE: $onlyTrue
        ONLY FALSE: $onlyFalse
        
        FOLD (true count): $sumFold
    """.trimIndent()
}