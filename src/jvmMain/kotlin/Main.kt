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
import garden.ephemeral.clipninja.clipboard.AwtClipboardManager

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
    var showHistoryWindow by remember { mutableStateOf(true) }
    var showSettingsWindow by remember { mutableStateOf(false) }
    val settings = rememberSettings()

    if (isOpen) {
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
                Item("Show Window") { showHistoryWindow = true }
                Item("Settings") { showSettingsWindow = true }
                Separator()
                Item("Exit") { isOpen = false }
            }
        )

        val clipboardManager = remember { AwtClipboardManager() }
        val clipboardEntries = remember { mutableStateListOf<ClipboardHistoryEntry>() }

        ClipboardEffects(
            clipboardManager = clipboardManager,
            clipboardEntries = clipboardEntries,
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
                title = "${Branding.AppName} Settings",
                icon = Branding.AppIcon,
                resizable = false,
                onCloseRequest = { showSettingsWindow = false }
            ) {
                SettingsScreen(settings)
            }
        }
    }
}
