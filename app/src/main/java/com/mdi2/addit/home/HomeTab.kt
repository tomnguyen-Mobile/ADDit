package com.mdi2.addit.home

import android.R.attr.clickable
import android.R.attr.text
import android.R.id.bold
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.mdi2.addit.Destinations
import com.mdi2.addit.MovieApplication

@Composable
fun HomeTab(
    navController: NavController
    ) {
    val movieViewModel: MovieViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
//                MovieViewModel(InMemoryMovieRepository())
                val application = checkNotNull(this[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as MovieApplication
                val repository = application.repository
                MovieViewModel(repository)
            }
        }
    )

    val uiState by movieViewModel.uiState.collectAsStateWithLifecycle()

    HomeView(
        uiState = uiState,
        modifier = Modifier,
        viewModel = movieViewModel,
        onTaskCompletionToggle = { taskId ->
            movieViewModel.toggleTaskCompletion(taskId)},
        onTaskClick = { taskId ->
            navController.navigate("${Destinations.TASK_DETAIL}/$taskId")})
}

@Composable
fun HomeView(
    modifier: Modifier,
    uiState: MovieUiState,
    viewModel: MovieViewModel,
    onTaskCompletionToggle: (Int) -> Unit,
    onTaskClick: (Int) -> Unit = {},
    ) {
    var taskTitle by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = modifier
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        )   {
        Text(
            "ADDit - Movies/Shows Tracker",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()

        ) // end of Text for header


        if (uiState.movieTasks.isEmpty()) {
            Text("No tasks to show", style = MaterialTheme.typography.bodyMedium)
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(
                uiState.movieTasks,
                key = { task -> task.id }
                ) {
                task ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable{onTaskClick(task.id)}
                    .padding(8.dp)){
                    uiState.errorMessage?.let{errorMessage ->
                        Text(errorMessage,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error) }
                    var commentText by rememberSaveable(task.id) {
                        mutableStateOf("")                }
                    Text(
                        text = task.title.split(" ").joinToString(" "){
                            word -> word.replaceFirstChar{ it.uppercase()}                    }, //end of joinToString
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold                )
                    Text(
                        text = if (task.isMovie) "Movie" else "TV Show",
                        fontStyle = FontStyle.Italic,                )
                    if (!task.isMovie) {
                        Text("Season: ${task.season}")
                        Text("Episode: ${task.episode}")                        }
                    Text(text = "Timestamp: ${task.timestamp ?: "00:00:00"}")
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } // end of item
        } // end of lazy column
    }// end of  column
} // end of Hom view
// todo: filter out completed tasks
