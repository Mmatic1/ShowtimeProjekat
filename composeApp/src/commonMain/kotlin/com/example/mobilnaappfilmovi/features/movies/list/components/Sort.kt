package com.example.mobilnaappfilmovi.features.movies.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilnaappfilmovi.features.movies.domain.SortType

@Composable
fun SortRow(
    sort: SortType,
    moviesCount: Int,
    onSortClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        AssistChip(
            onClick = onSortClick,
            label = { Text("Sort: ${sort.name}") }
        )

        Text("$moviesCount movies")
    }
}