package com.mdi2.addit.data.network

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun OmdbSearchCard(
    movie: OmdbSearchItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation =
                if (selected) 8.dp else 2.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor =
                if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ){
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = movie.Poster,
                contentDescription = movie.Title,
                modifier = Modifier.size(75.dp)
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {

                Text(
                    text = movie.Title.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = movie.Year.orEmpty()
                )
                Text(
                    text = movie.Type.orEmpty()
                        .replaceFirstChar { it.uppercase() }
                )
                if (selected) {
                    Text(
                        text = "✓ Selected",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}