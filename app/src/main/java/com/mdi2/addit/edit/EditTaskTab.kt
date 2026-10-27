package com.mdi2.addit.edit

import android.R.attr.label
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.mdi2.addit.Destinations
import com.mdi2.addit.MovieApplication
import com.mdi2.addit.home.MovieViewModel

@Composable
fun EditTaskTab(
    taskId: Int,
    navController: NavController,
) {
    val movieViewModel: MovieViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val application =
                    checkNotNull(
                        this[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    ) as MovieApplication

                MovieViewModel(application.repository)
            }
        })
    val uiState by movieViewModel.uiState.collectAsStateWithLifecycle()

    val task = uiState.movieTasks.find {
        it.id == taskId
    }

    if (task == null) {
        Text("Task not found")
        return
    }

    var title by remember { mutableStateOf(task.title) }
    var isMovie by remember { mutableStateOf(task.isMovie) }
    var season by remember { mutableStateOf(task.season.toString()) }
    var episode by remember { mutableStateOf(task.episode.toString()) }
    var timestamp by remember { mutableStateOf(task.timestamp) }
    var completed by remember { mutableStateOf(task.completed) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        IconButton(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }// end of icon button


        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isMovie,
                onClick = { isMovie = true }
            )
            Text("Movie")

            RadioButton(
                selected = !isMovie,
                onClick = { isMovie = false }
            )
            Text("TV Show")
        } // end of row
        if (!isMovie) {

            TextField(
                value = season,
                onValueChange = { season = it },
                label = { Text("Season") }
            )

            TextField(
                value = episode,
                onValueChange = { episode = it },
                label = { Text("Episode") }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        TextField(
            value = timestamp,
            onValueChange = { timestamp = it },
            placeholder = {timestamp},
            label = { Text("Timestamp") }
        )
                Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
                selected = completed,
                onClick = { completed = true }
                )
            Text(text = " finished watching?")
        } // end of row

        Button(
            onClick = {

                movieViewModel.updateMovieTask(
                    taskId = taskId,
                    title = title,
                    season = season.toIntOrNull() ?: 0,
                    episode = episode.toIntOrNull() ?: 0,
                    timestamp = timestamp
                )

                navController.popBackStack()
            }
        ) {
            Text("Save")
        }
    }
}