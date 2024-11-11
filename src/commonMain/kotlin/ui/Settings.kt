package garden.ephemeral.clipninja.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

class Settings {
    val runOnStartup = mutableStateOf(false)
    val showAllText = mutableStateOf(false)
    val reallyShowAllText = mutableStateOf(false)
    val enableDiscordEmbed = mutableStateOf(true)
    val enableStripTracking = mutableStateOf(true)
}

@Composable
fun rememberSettings() = rememberSaveable { Settings() }
