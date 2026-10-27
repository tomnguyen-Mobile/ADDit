package com.mdi2.addit.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import com.mdi2.addit.home.HomeView
import com.mdi2.addit.home.MovieViewModel

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



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
        } // end of row

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
                    title = title,
                    isMovie = isMovie,
                    season = season.toIntOrNull() ?: 0,
                    episode = episode.toIntOrNull() ?: 0,
                    timestamp = timestamp ,
                    completed = completed,
                )
                // reset the variables
                title = ""
                isMovie = true
                season = ""
                episode = ""
                timestamp = ""
                completed = false
                // navigate back to the home screen
                navController.navigate(Destinations.HOME)
            }, // end of onclick
            enabled = title.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),

            ) {
            Text("Save")
        } // end of button

    }
}