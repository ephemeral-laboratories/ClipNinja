package garden.ephemeral.clipboard.ninja

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

class Settings {
    val showAllText = mutableStateOf(false)
    val enableDiscordEmbed = mutableStateOf(true)
    val enableStripTracking = mutableStateOf(true)
}

@Composable
fun rememberSettings() = rememberSaveable { Settings() }
