package com.mdi2.addit.edit

import android.R.attr.label
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    var showDeleteDialog by remember { mutableStateOf(false) }
    var progress by remember {

        val parts = task.timestamp.split(":")

        val totalSeconds =
            if (parts.size == 3) {
                (parts[0].toIntOrNull() ?: 0) * 3600 +
                        (parts[1].toIntOrNull() ?: 0) * 60 +
                        (parts[2].toIntOrNull() ?: 0)
            } else {
                0
            }

        mutableStateOf(totalSeconds.toFloat())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) { // Column

        TextButton(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Back")
        }
//        IconButton(
//            onClick = {
//                navController.popBackStack()
//            }
//        ) {
//            Icon(
//                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                contentDescription = "Back"
//            )
//            Text("Back")
//        }// end of icon button


        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
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
                label = { Text("Season") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = episode,
                onValueChange = { episode = it },
                label = { Text("Episode") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            text = "Current Timestamp: $timestamp"
        )
        TimestampSlider(
            totalSeconds = 7200,
            currentSeconds = progress,
            onPositionChange = {
                progress = it

                val totalSeconds = it.toInt()

                val hours = totalSeconds / 3600
                val minutes = (totalSeconds % 3600) / 60
                val seconds = totalSeconds % 60

                timestamp = "%02d:%02d:%02d".format(
                    hours,
                    minutes,
                    seconds
                )
            }
        )
                Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
                    if (!completed) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    completed = true
                                }
                            ) {
                                Text("Mark as Finished")
                            }
                        }

                    } else {

                        Text(
                            text = "✅ Finished Watching"
                        )

                    }
        } // end of row

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = {

                    movieViewModel.updateMovieTask(
                        taskId = taskId,
                        title = title,
                        season = season.toIntOrNull() ?: 0,
                        episode = episode.toIntOrNull() ?: 0,
                        timestamp = timestamp,
                        completed = completed
                    )

                    navController.popBackStack()
                }
            ) {
                Text("Save")
            }

            Button(
                onClick = {
                    showDeleteDialog = true
                }
            ) {
                Text("Delete")
            }
        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                },
                title = {
                    Text("Delete Task")
                },
                text = {
                    Text("Are you sure you want to delete this task? This action cannot be undone.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            movieViewModel.deleteMovieTask(taskId)

                            navController.navigate(Destinations.HOME) {
                                popUpTo(Destinations.HOME) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }

                            showDeleteDialog = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

    }
}