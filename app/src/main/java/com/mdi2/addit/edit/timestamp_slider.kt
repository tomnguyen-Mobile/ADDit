package com.mdi2.addit.edit


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TimestampSlider(
    totalSeconds: Int,
    currentSeconds: Float,
    onPositionChange: (Float) -> Unit
) {

    val hours = (currentSeconds.toInt() / 3600)
    val minutes = (currentSeconds.toInt() % 3600) / 60
    val seconds = currentSeconds.toInt() % 60

    Column {

        Text(
            "Timestamp: "+"%02d:%02d:%02d".format(
                hours,
                minutes,
                seconds
            )
        )

        Slider(
            value = currentSeconds,
            onValueChange = onPositionChange,
            valueRange = 0f..totalSeconds.toFloat()
        )
    }
}