package com.example.water_tracker

import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.water_tracker.ui.theme.Water_TrackerTheme
import java.io.BufferedReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Water_TrackerTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background

                ) {
                    WaterTrackerApp()
                }
            }
        }
    }
}

@Composable
fun WaterTrackerApp(){
    var glasses by remember { mutableIntStateOf(0) }
    val goal = 8
    val progress = glasses.toFloat()/goal
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Water Tracker 💧", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Today's Water Intake",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$glasses / $goal glasses",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(onClick = {
                        if (glasses > 0) {
                            glasses--
                        }
                    }) {
                        Text("Remove")
                    }

                    Button(
                        onClick = { glasses = 0 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Reset")
                    }

                    Button(onClick = {
                        if (glasses < goal) {
                            glasses++
                        }
                    }) {
                        Text("Add")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = when {
                        glasses == 0 -> "Let's start drinking water today!"
                        glasses < 4 -> "Keep going!"
                        glasses < goal -> "You're doing great!"
                        else -> "Good job!"
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WaterTrackerAppPreview(){
    Water_TrackerTheme() {
        WaterTrackerApp()
    }
}