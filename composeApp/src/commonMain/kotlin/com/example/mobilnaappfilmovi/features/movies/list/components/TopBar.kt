package com.example.mobilnaappfilmovi.features.movies.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    onFilterClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onProfileClick: () -> Unit, // 🔥 DODATO
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // LEFT SIDE (filter + favorites + watchlist)
        Row {

            Button(onClick = onFilterClick) {
                Text("Filters ($filtersCount)")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onFavoritesClick) {
                Text("Favorites")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onWatchlistClick) {
                Text("Watchlist")
            }
        }

        // RIGHT SIDE 🔥 PROFILE
        Button(
            onClick = onProfileClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE50914)
            )
        ) {
            Text("Profile")
        }
    }
}