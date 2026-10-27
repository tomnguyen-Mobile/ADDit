package com.mdi2.addit.settingsScreen

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mdi2.addit.models.SettingsViewModel

@Composable
fun SettingsTab(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    ){
    val selectedThemeMode = viewModel.selectedThemeMode
    Column(
        modifier = modifier.padding(16.dp)
        ){

        Text(
            text = "Settings",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,)

        Text(
            text="Select Theme Mode:",
            modifier = Modifier.padding(top=16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyLarge)

//        var selectedThemeMode by rememberSaveable { mutableStateOf(ThemeMode.SYSTEM) }
        val localContext = LocalContext.current
        ThemeMode.entries.forEach { mode ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
                ){
                RadioButton(
                    selected = selectedThemeMode == mode,
                    onClick = {
                        viewModel.setTheme(mode)
                        applyAppTheme(mode, localContext)
                        }
                    )
                Text(
                    text= "${mode.name} MODE",
                    style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}


fun applyAppTheme(
    mode: ThemeMode,
    context: Context
    ){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val nightMode = when(mode){
            ThemeMode.LIGHT -> UiModeManager.MODE_NIGHT_NO
            ThemeMode.DARK -> UiModeManager.MODE_NIGHT_YES
            ThemeMode.SYSTEM -> UiModeManager.MODE_NIGHT_CUSTOM
        }
        uiModeManager.setApplicationNightMode(nightMode)
    } else {
        val nightMode = when(mode){
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}

enum class ThemeMode{
    LIGHT,
    DARK,
    SYSTEM
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun RandomCardPreview(){
    SettingsTab( viewModel = SettingsViewModel())
}