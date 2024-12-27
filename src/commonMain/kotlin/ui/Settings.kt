package garden.ephemeral.clipninja.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.util.prefs.Preferences

private val preferencesNode = Preferences.userRoot().node("garden/ephemeral/clipninja/ui")

class Settings {
    val runOnStartup = mutableStateOf(false)
    val showAllText = mutableStateOf(false)
    val reallyShowAllText = mutableStateOf(false)
    val enableDiscordEmbed = mutableStateOf(preferencesNode.getBoolean("enableDiscordEmbed", true))
    val enableStripTracking = mutableStateOf(preferencesNode.getBoolean("enableStripTracking", true))

    @Composable
    internal fun syncPreferences() {
        LaunchedEffect(enableDiscordEmbed.value) {
            preferencesNode.putBoolean("enableDiscordEmbed", enableDiscordEmbed.value)
        }
        LaunchedEffect(enableStripTracking.value) {
            preferencesNode.putBoolean("enableStripTracking", enableStripTracking.value)
        }
    }
}

@Composable
fun rememberSettings() = remember { Settings() }.apply { syncPreferences() }
