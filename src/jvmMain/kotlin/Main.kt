package garden.ephemeral.clipninja

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import com.russhwolf.settings.PreferencesSettings
import garden.ephemeral.clipninja.autorun.rememberAutoRunRegistry
import garden.ephemeral.clipninja.clipboard.rememberClipboardManager
import garden.ephemeral.clipninja.clipninja.generated.resources.Res
import garden.ephemeral.clipninja.clipninja.generated.resources.action_exit
import garden.ephemeral.clipninja.clipninja.generated.resources.action_settings
import garden.ephemeral.clipninja.clipninja.generated.resources.action_show_window
import garden.ephemeral.clipninja.clipninja.generated.resources.settings_title_format
import garden.ephemeral.clipninja.notifications.rememberNotifier
import garden.ephemeral.clipninja.ui.AppSettingsScreen
import garden.ephemeral.clipninja.ui.Branding
import garden.ephemeral.clipninja.ui.ClipboardEffects
import garden.ephemeral.clipninja.ui.ClipboardHistoryEntry
import garden.ephemeral.clipninja.ui.HistoryScreen
import garden.ephemeral.clipninja.ui.rememberAppSettings
import org.jetbrains.compose.resources.stringResource
import java.util.prefs.Preferences

private val preferencesNode = Preferences.userRoot().node("garden/ephemeral/clipninja/ui")

fun main() = application {
    var showHistoryWindow by remember { mutableStateOf(true) }
    var showSettingsWindow by remember { mutableStateOf(false) }
    val autoRunRegistry = rememberAutoRunRegistry()
    val settings = rememberAppSettings(autoRunRegistry, PreferencesSettings(preferencesNode))

    val trayState = rememberTrayState()
    Tray(
        state = trayState,
        tooltip = Branding.AppName,
        icon = Branding.AppIcon,
        onAction = { showHistoryWindow = true },
        menu = {
            // FIXME: "Show Window" text should be bold
            // FIXME: The whole menu should be in dark mode but currently looks like Windows 95 :(
            //        JetBrains' Toolbox app seems to do it somehow, but how?
            Item(stringResource(Res.string.action_show_window)) { showHistoryWindow = true }
            Item(stringResource(Res.string.action_settings)) { showSettingsWindow = true }
            Separator()
            Item(stringResource(Res.string.action_exit)) { exitApplication() }
        }
    )

    val clipboardManager = rememberClipboardManager()
    val clipboardEntries = remember { mutableStateListOf<ClipboardHistoryEntry>() }
    val notifier = rememberNotifier()

    ClipboardEffects(
        clipboardManager = clipboardManager,
        clipboardEntries = clipboardEntries,
        notifier = notifier,
        settings = settings,
    )

    if (showHistoryWindow) {
        Window(
            state = rememberWindowState(size = DpSize(800.dp, 1_200.dp)),
            title = Branding.AppName,
            icon = Branding.AppIcon,
            onCloseRequest = { showHistoryWindow = false }
        ) {
            HistoryScreen(
                clipboardManager = clipboardManager,
                clipboardEntries = clipboardEntries,
                settings = settings,
                onSettingsClicked = { showSettingsWindow = !showSettingsWindow },
            )
        }
    }

    if (showSettingsWindow) {
        Window(
            state = rememberWindowState(size = DpSize.Unspecified),
            title = stringResource(Res.string.settings_title_format, Branding.AppName),
            icon = Branding.AppIcon,
            resizable = false,
            onCloseRequest = { showSettingsWindow = false }
        ) {
            AppSettingsScreen(settings)
        }
    }
}
