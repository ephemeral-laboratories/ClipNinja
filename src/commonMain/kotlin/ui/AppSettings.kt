package garden.ephemeral.clipninja.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import garden.ephemeral.clipninja.autorun.AutoRunRegistry

class AppSettings internal constructor(private val autoRunRegistry: AutoRunRegistry, private val settings: Settings) {
    val runOnStartup = mutableStateOf(autoRunRegistry.isInstalled())
    val showAllText = mutableStateOf(false)
    val reallyShowAllText = mutableStateOf(false)
    val enableDiscordEmbed = mutableStateOf(settings["enableDiscordEmbed", true])
    val enableStripTracking = mutableStateOf(settings["enableStripTracking", true])

    @Composable
    internal fun syncPreferences() {
        LaunchedEffect(runOnStartup.value) {
            runOnStartup.value = autoRunRegistry.updateInstalled(runOnStartup.value)
        }
        LaunchedEffect(enableDiscordEmbed.value) {
            settings["enableDiscordEmbed"] = enableDiscordEmbed.value
        }
        LaunchedEffect(enableStripTracking.value) {
            settings["enableStripTracking"] = enableStripTracking.value
        }
    }
}

@Composable
fun rememberAppSettings(autoRunRegistry: AutoRunRegistry, settings: Settings) = remember {
    AppSettings(autoRunRegistry, settings)
}.apply { syncPreferences() }
