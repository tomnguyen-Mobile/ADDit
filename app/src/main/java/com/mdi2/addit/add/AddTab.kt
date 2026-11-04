package com.mdi2.addit.add

import android.R.attr.contentDescription
import android.R.attr.enabled
import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.mdi2.addit.Destinations
import com.mdi2.addit.MovieApplication
import com.mdi2.addit.data.network.OmdbMovie
import com.mdi2.addit.home.HomeView
import com.mdi2.addit.home.MovieTaskCard
import com.mdi2.addit.home.MovieViewModel
import coil.compose.AsyncImage
import com.mdi2.addit.data.network.OmdbSearchItem

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

    val focusManager = LocalFocusManager.current
    var title by rememberSaveable { mutableStateOf("") }
    var isMovie by rememberSaveable { mutableStateOf(true) }
    var season by rememberSaveable { mutableStateOf("") }
    var episode by rememberSaveable { mutableStateOf("") }
    var timestamp by rememberSaveable { mutableStateOf("00:00:00") }
    var completed by rememberSaveable { mutableStateOf(false) }
    var imageUrl:String? by rememberSaveable { mutableStateOf(null) }
    var searchResult by remember {mutableStateOf<OmdbMovie?>(null)} // will only return one result
    var searchResults by remember {
        mutableStateOf<List<OmdbSearchItem>>(emptyList())
    }



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
        Button(
            onClick = {

                // Clear previously selected movie details
                searchResult = null
                imageUrl = null

                movieViewModel.searchMovies(title) { movies ->
                    searchResults = movies
                }
            }
        ) {
            Text("Search OMDb")
        }
        searchResult?.let { movie ->

            if (!movie.Poster.isNullOrBlank()) {
                AsyncImage(
                    model = movie.Poster,
                    contentDescription = movie.Title,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = movie.Title.orEmpty(),
                style = MaterialTheme.typography.titleLarge
            )

            Text("Year: ${movie.Year}")

            Text("Type: ${movie.Type}")

            Text(movie.Plot ?: "")
        }
        //                      result from search
        Column {
            searchResults.forEach { movie ->
                TextButton(
                    onClick = {
                        movieViewModel.searchMovieById(
                            movie.imdbID ?: ""
                        ) { fullMovie ->

                            searchResult = fullMovie

                            title = fullMovie?.Title.orEmpty()
                            imageUrl = fullMovie?.Poster
                            isMovie = fullMovie?.Type == "movie"
                        }
                    }
                ) {
                    Text("${movie.Title} (${movie.Year})")
                }
            }
        }


//        searchResult?.let { movie ->
//            if (!movie.Poster.isNullOrEmpty()) {
//                imageUrl = movie.Poster
//            }
//            AsyncImage(
//                model = imageUrl,
//                contentDescription = movie.Title,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//
//            Text(
//                text = movie.Title.orEmpty(),
//                style = MaterialTheme.typography.titleLarge
//            )
//
//            Text(if (movie.Year != null) "Year: ${movie.Year}" else "not found")
//            Text(if (movie.Type != null) "Type: ${movie.Type}" else "not found")
//            Text(movie.Plot ?: "")
//        }

        if (searchResult == null) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isMovie,
                    onClick = { isMovie = true }
                )
                Text("Movie")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = !isMovie,
                    onClick = { isMovie = false }
                )
                Text("TV Show")
            }

        } else {

            Text(
                text = if (isMovie) "Type: Movie" else "Type: TV Show",
                style = MaterialTheme.typography.titleMedium
            )

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

            OutlinedTextField(
                value = episode,
                onValueChange = { episode = it },
                label = { Text("Episode") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = timestamp,
            onValueChange = { timestamp = it },
            label = { Text("Timestamp") },
            placeholder = { Text("00:00:00") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                movieViewModel.addMovieTask(
//                    title = if(title.isBlank()){ searchResult?.Title ?: ""} else{ title},
                    title = title,
                    isMovie = isMovie,
                    season = season.toIntOrNull() ?: 0,
                    episode = episode.toIntOrNull() ?: 0,
                    timestamp = timestamp,
                    completed = completed,
                    imageUrl = imageUrl
//                    imageUrl = searchResult?.Poster
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
            enabled = searchResult != null,
            modifier = Modifier.fillMaxWidth(),

            ) {
            Text("Add")
        } // end of button

    }
}