package com.example.mobilnaappfilmovi.features.movies.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.mobilnaappfilmovi.features.movies.domain.Movie

@Composable
fun MovieItem(
    movie: Movie,
    onClick:()->Unit
)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable{onClick()},
        shape=RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        )

    )
    {
        Row(modifier = Modifier.padding(12.dp)){
            AsyncImage(
                model=movie.posterUrl,
                contentDescription=null,
                modifier= Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier= Modifier.width(12.dp))
            Column(modifier= Modifier.fillMaxWidth())
            {
                Text(
                    text=movie.name,
                    color=Color.White,
                    style= MaterialTheme.typography.titleMedium
                )
                Text(
                    text="${movie.year}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier= Modifier.height(6.dp))

                Row{
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFE50914)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text("${movie.rating}",
                        color=Color.LightGray)

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("${movie.votes} votes",
                        color=Color.LightGray)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row{
                    movie.genres.take(3).forEach { genre ->
                       GenreChip(name = genre.name)
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }
        }
    }
}
@Composable
fun GenreChip(name :String)
{
    Surface(
        color = Color(0xFFE50914).copy(alpha = 0.2f),
        contentColor = Color.LightGray,
        shape = RoundedCornerShape(50)
    )
    {
        Text(
            text = name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style=MaterialTheme.typography.bodySmall
        )
    }
}