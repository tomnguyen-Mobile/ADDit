package com.mdi2.addit.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.mdi2.addit.MovieApplication
import com.mdi2.addit.home.MovieViewModel

@Composable
fun TaskDetailTab(
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
    var commentText by remember { mutableStateOf("") }
    val comments by movieViewModel
        .getCommentsForTask(taskId)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Column {
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

        val task = uiState.movieTasks.find {
            it.id == taskId
        }

        if (task == null) {
            Text("Task not found")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = task.title.split(" ")
                        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (task.isMovie) "Movie" else "TV Show"
                )

                if (!task.isMovie) {
                    Text("Season: ${task.season}")
                    Text("Episode: ${task.episode}")
                }

                Text("Timestamp: ${task.timestamp}")
                Text("Completed: ${task.completed}")
                Button(
                    onClick = {
                        navController.navigate("editTask/$taskId")
                    }
                ) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "Add Comments:",
                    style = MaterialTheme.typography.titleMedium
                )

                TextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Enter comment...")
                    }
                )

                Button(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            movieViewModel.addComment(
                                movieTaskId = taskId,
                                comment = commentText
                            )

                            commentText = ""
                        }
                    }
                ) {
                    Text("Add Comment")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Comments:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (comments.isEmpty()) {
                    Text("No comments yet")
                } else {
                    LazyColumn {
                        items(comments) { comment ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = comment.comment,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            } // end of column
        } // end of else
    }
}