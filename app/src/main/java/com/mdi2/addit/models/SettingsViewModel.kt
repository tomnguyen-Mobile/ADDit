package com.mdi2.addit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mdi2.addit.settingsScreen.ThemeMode

class SettingsViewModel: ViewModel() {
    var selectedThemeMode by mutableStateOf(ThemeMode.SYSTEM)
        private set

    fun setTheme(mode: ThemeMode){
        selectedThemeMode = mode
    }
}