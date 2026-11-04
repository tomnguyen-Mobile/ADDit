package com.mdi2.addit.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mdi2.addit.models.MovieTask

@Composable
fun MovieTaskCard(
    task: MovieTask,
    posterUrl: String?,
    onClick: (Int) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick(task.id)
            }
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = posterUrl,
                contentDescription = task.title,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (task.isMovie) "Movie" else "TV Show"
                )

                if (!task.isMovie) {
                    Text("Season ${task.season}")
                    Text("Episode ${task.episode}")
                }
            }
        }
    }
}

//package com.mdi2.addit.home
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.mdi2.addit.models.MovieTask
//
//@Composable
//fun MovieTaskCard(
//    task: MovieTask,
//    onClick: (Int) -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clickable { onClick(task.id) },
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 4.dp
//        )
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                text = task.title.split(" ")
//                    .joinToString(" ") {
//                        it.replaceFirstChar { c -> c.uppercase() }
//                    },
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold
//            )
//
//            Text(
//                text = if (task.isMovie) "Movie" else "TV Show",
//                fontStyle = FontStyle.Italic
//            )
//
//            if (!task.isMovie) {
//                Text("Season: ${task.season}")
//                Text("Episode: ${task.episode}")
//            }
//
//            Text(
//                text = "Timestamp: ${task.timestamp ?: "00:00:00"}"
//            )
//        }
//    }
//}

