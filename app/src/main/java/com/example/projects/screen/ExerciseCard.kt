package com.example.projects.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.projects.R
import com.example.projects.ui.theme.c1
import com.example.projects.ui.theme.c2

@Composable
fun ExerciseCard(
    name: String,
    duration: Int,
    repetition: Int,
    onClick: () -> Unit,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            c1,
                            c2
                        )
                    )
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .padding(8.dp)
                    .weight(2.0f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Row {
                    if (duration != 0) {
                        Text(
                            text = "duration: $duration",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    } else if (repetition != 0) {
                        Text(
                            text = "repetition: $repetition",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}