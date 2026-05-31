package com.example.mobilnaappfilmovi.features.movies.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoviesTopBar(
    filtersCount: Int,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "🎬 Premiere",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Box {
            Button(onClick = onFilterClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor =Color(0xFFE50914).copy(alpha = 0.2f),
                    contentColor = Color.White
                )) {
                Text("Filter")
            }

            if (filtersCount > 0) {
                Badge(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text("$filtersCount")
                }
            }
        }
    }
}