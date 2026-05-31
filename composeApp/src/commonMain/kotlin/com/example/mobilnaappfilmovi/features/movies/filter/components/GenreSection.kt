package com.example.mobilnaappfilmovi.features.movies.filter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import projekat.features.movies.domain.Genre

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreSection(
    genres: List<projekat.features.movies.domain.Genre>,
    selectedGenreId: Int?,
    onGenreSelected: (projekat.features.movies.domain.Genre?) -> Unit
)
{
    Column {
        Text("Genre", color = Color.White, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // FlowRow automatski prebacuje čipove u novi red ako nema mesta
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                val isSelected = genre.id == selectedGenreId
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (isSelected) onGenreSelected(null) else onGenreSelected(genre)
                    },
                    label = { Text(genre.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.Red,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF1E1E1E),
                        labelColor = Color.LightGray
                    )
                )
            }
        }
    }
}