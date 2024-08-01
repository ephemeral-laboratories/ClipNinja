package garden.ephemeral.clipninja

import androidx.compose.runtime.*
import garden.ephemeral.clipninja.clipboard.AwtClipboardManager
import garden.ephemeral.clipninja.clipboard.ClipboardContents
import garden.ephemeral.clipninja.notifications.Notifier

@Composable
fun ClipboardEffects(clipboardEntries: MutableList<ClipboardHistoryEntry>, settings: Settings) {
    val clipboardManager = remember { AwtClipboardManager() }

    val fixers by derivedStateOf {
        buildList {
            if (settings.enableDiscordEmbed.value) {
                add(DiscordEmbedURLFixer())
            }
            if (settings.enableStripTracking.value) {
                add(RemoveTrackingURLFixer())
            }
        }
    }

    LaunchedEffect(Unit) {
        clipboardManager.getContentsFlow().collect { contents ->
            if (contents.text == null) {
                clipboardEntries.add(0, ClipboardHistoryEntry(contents = contents))
                return@collect
            }
            val text = contents.text

            val originalUrl = URL.tryFromString(text)
            if (originalUrl == null) {
                clipboardEntries.add(0, ClipboardHistoryEntry(contents = contents))
                return@collect
            }

            var url: URL = originalUrl
            val changesApplied = mutableListOf<String>()

            fixers.forEach { fixer ->
                val (newUrl, explanation) = fixer.fix(url)
                url = newUrl
                if (explanation != null) {
                    changesApplied.add(explanation)
                }
            }

            clipboardEntries.add(
                0, ClipboardHistoryEntry(
                    contents = contents,
                    recognised = true,
                    changesApplied = changesApplied.toList()
                )
            )

            if (originalUrl != url) {
                changesApplied.add(0, "Changes applied:")
                Notifier.notify(changesApplied.joinToString(separator = "\n \u2022"))

                clipboardManager.setContents(ClipboardContents(text = url.toString(), image = null))
            }
        }
    }

}
