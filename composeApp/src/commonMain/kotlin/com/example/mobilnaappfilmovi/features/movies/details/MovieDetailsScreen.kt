package com.example.mobilnaappfilmovi.features.movies.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.mobilnaappfilmovi.features.movies.details.components.InfoCard
import com.example.mobilnaappfilmovi.features.movies.details.components.SectionTitle
import com.example.mobilnaappfilmovi.features.movies.domain.MovieDetails
import kotlin.math.roundToInt

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                MovieDetailsContract.SideEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text("Error: ${state.error}")

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            viewModel.onEvent(
                                MovieDetailsContract.UiEvent.LoadDetails
                            )
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }

        state.movieDetails != null -> {
            MovieDetailsContent(
                movie = state.movieDetails!!,
                onNavigateBack = onNavigateBack
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsContent(
    movie: MovieDetails,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {

            AsyncImage(
                model = movie.backdropPath,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFF0F0F0F)
                            ),
                            startY = 400f
                        )
                    )
            )

            if (movie.trailerUrl != null) {
                IconButton(
                    onClick = { uriHandler.openUri(movie.trailerUrl) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(70.dp)
                        .background(Color.Red, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play Trailer",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .offset(y = (-40).dp)
        ) {

            AsyncImage(
                model = movie.posterPath,
                contentDescription = null,
                modifier = Modifier
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shadow(8.dp, RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.padding(top = 48.dp)
            ) {

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${movie.year ?: ""} • ${movie.runtime ?: ""} min",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "⭐ ${movie.imdbRating ?: "-"}",
                        color = Color.White
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "(${movie.imdbVotes ?: 0})",
                        color = Color.Gray
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "TMDB ${movie.tmdbRating ?: "-"}",
                        color = Color.LightGray
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .offset(y = (-20).dp)
        ) {

            movie.genres.forEach { genre ->

                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Red.copy(alpha = 0.9f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {

                    Text(
                        text = genre.name,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        SectionTitle("OVERVIEW")

        Text(
            text = movie.overview ?: "No overview",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            lineHeight = 22.sp
        )

        SectionTitle("INFO")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            movie.budget?.let {
                InfoCard(
                    "Budget",
                    "$${it / 1000000}M"
                )
            }

            movie.revenue?.let {
                InfoCard(
                    "Revenue",
                    "$${it / 1000000}M"
                )
            }

            movie.languageCode?.let {
                InfoCard(
                    "Lang",
                    it.uppercase()
                )
            }

            movie.popularity?.let {
                InfoCard(
                    "Popularity",
                    it.roundToInt().toString()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (movie.images.isNotEmpty()) {

            SectionTitle("IMAGES")

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(movie.images) { imageUrl ->

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Movie gallery image",
                        modifier = Modifier
                            .width(220.dp)
                            .height(125.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        if (movie.cast.isNotEmpty()) {

            SectionTitle("ACTORS")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                movie.cast.take(10).forEach { actor ->

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AsyncImage(
                            model = actor.profileUrl,
                            contentDescription = actor.name,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = actor.name,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}