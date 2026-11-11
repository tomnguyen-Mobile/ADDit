package com.mdi2.addit.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mdi2.addit.Destinations
import com.mdi2.addit.MovieApplication
import com.mdi2.addit.data.network.OmdbMovie
import com.mdi2.addit.data.network.OmdbSearchCard
import com.mdi2.addit.data.network.OmdbSearchItem
import com.mdi2.addit.edit.TimestampSlider
import com.mdi2.addit.home.MovieViewModel
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.platform.LocalContext

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false

    val capabilities =
        connectivityManager.getNetworkCapabilities(network)
            ?: return false

    return capabilities.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_INTERNET
    )
}
@Composable
fun AddTab(
    navController: NavController
) {

    val movieViewModel: MovieViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val application =
                    checkNotNull(
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    ) as MovieApplication

                MovieViewModel(application.repository)
            }
        }
    )

    var progress by remember {
        mutableStateOf(0f)
    }
    val context = LocalContext.current
    var title by rememberSaveable { mutableStateOf("") }
    var isMovie by rememberSaveable { mutableStateOf(true) }
    var season by rememberSaveable { mutableStateOf("") }
    var episode by rememberSaveable { mutableStateOf("") }
    var timestamp by rememberSaveable { mutableStateOf("00:00:00") }
    var completed by rememberSaveable { mutableStateOf(false) }
    var imageUrl:String? by rememberSaveable { mutableStateOf(null) }
    var searchResult by remember {mutableStateOf<OmdbMovie?>(null)} // will only return one result
    var errorMessage by remember { mutableStateOf<String?>(null) } // handles no internet
    var isLoading by remember {        mutableStateOf(false)    } // loading spinner
    var searchResults by remember {        mutableStateOf<List<OmdbSearchItem>>(emptyList())    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        Text(
            text = "Add Movie / TV Show",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
        )
        // SEARCH BUTTON
        Button(
            onClick = {

                // check for the internet first
                if (!isInternetAvailable(context)) {
                    errorMessage = "No internet connection."
                    return@Button
                }

                // Clear previously selected movie details
                searchResult = null
                imageUrl = null
                errorMessage = null
                isLoading = true

                movieViewModel.searchMovies(title) { movies ->
                    isLoading = false
                    searchResults = movies
                    if (movies.isEmpty()) {
                        errorMessage = "No results found."
                    }
                }
            },
            enabled = title.isNotBlank() && isInternetAvailable(context)
        ) {
            Text("Search OMDb")
        } // end of search button
        if (!isInternetAvailable(context)) {
            Text(
                text = "Offline: OMDb unavailable",
                color = MaterialTheme.colorScheme.error
            )
        }
        if (isLoading) {
            CircularProgressIndicator()
        }
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }
        searchResult?.let { movie ->

            if (!movie.Poster.isNullOrBlank()) {
                AsyncImage(
                    model = movie.Poster,
                    contentDescription = movie.Title,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            val runtimeMinutes =
                movie.Runtime
                    ?.replace(" min", "")
                    ?.toIntOrNull()
                    ?: 0

            val totalSeconds = runtimeMinutes * 60

            Text(
                text = movie.Title.orEmpty(),
                style = MaterialTheme.typography.titleLarge
            )

            Text("Year: ${movie.Year}")

            Text("Type: ${movie.Type}")
            Text("Runtime: ${movie.Runtime}")

            Text(
                text = if (!movie.Plot.isNullOrBlank()
                    && movie.Plot != "N/A"
                ) {
                    "Plot: ${movie.Plot}"
                } else {
                    "Plot: Not available"
                }
            )
            TimestampSlider(
                totalSeconds = totalSeconds,
                currentSeconds = progress,
                onPositionChange = {
                    progress = it
                    val seconds = it.toInt()
                    timestamp = "%02d:%02d:%02d".format(
                        seconds / 3600,
                        (seconds % 3600) / 60,
                        seconds % 60
                    )
                }
            )

        }
        //                      result from search
        Column {
            searchResults.forEach { movie ->
                OmdbSearchCard(
                    movie = movie,
                    selected = searchResult?.imdbID == movie.imdbID,
                    onClick = {
                        movieViewModel.searchMovieById(
                            movie.imdbID ?: ""
                        ) { fullMovie ->

                            searchResult = fullMovie

                            title = fullMovie?.Title.orEmpty()
                            imageUrl = fullMovie?.Poster
                            isMovie = fullMovie?.Type == "movie"
                            errorMessage = null
                            if (!isMovie) {
                                season = "1"
                                episode = "1"
                            }
                        }
                    }
                )
            }
        }

        if (!isMovie) {

            OutlinedTextField(
                value = season,
                onValueChange = { season = it },
                label = { Text("Season") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (!isMovie && season.isBlank()) {
                Text(
                    text = "Season is required",
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = episode,
                onValueChange = { episode = it },
                label = { Text("Episode") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )


            if (!isMovie && episode.isBlank()) {
                Text(
                    text = "Episode is required",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Button(
            onClick = {
                movieViewModel.addMovieTask(
                    title = title,
                    isMovie = isMovie,
                    season = season.toIntOrNull() ?: 0,
                    episode = episode.toIntOrNull() ?: 0,
                    timestamp = timestamp,
                    completed = completed,
                    imageUrl = imageUrl
                )
                // reset the variables
                title = ""
                isMovie = true
                season = ""
                episode = ""
                timestamp = ""
                completed = false
                imageUrl = null
                searchResult = null
                searchResults = emptyList()

                // navigate back to the home screen
                navController.navigate(Destinations.HOME)
            }, // end of onclick
//            enabled = title.isNotBlank() || searchResult != null,
//            enabled = searchResult != null,
            enabled =
                searchResult != null &&
                        (
                                isMovie ||
                                        (
                                                season.isNotBlank() &&
                                                        episode.isNotBlank()
                                                )
                                ),
            modifier = Modifier.fillMaxWidth(),

            ) {
            Text("Add")
        } // end of button

    }
}