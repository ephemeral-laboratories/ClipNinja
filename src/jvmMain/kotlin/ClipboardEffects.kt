package garden.ephemeral.clipninja

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import garden.ephemeral.clipninja.clipboard.ClipboardContents
import garden.ephemeral.clipninja.clipboard.ClipboardManager
import garden.ephemeral.clipninja.notifications.Notifier

@Composable
fun ClipboardEffects(clipboardManager: ClipboardManager, clipboardEntries: MutableList<ClipboardHistoryEntry>, settings: Settings) {
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

            val newEntry = ClipboardHistoryEntry(
                contents = contents,
                recognised = true,
                changesApplied = changesApplied.toList()
            )
            clipboardEntries.add(0, newEntry)

            if (originalUrl != url) {
                Notifier.notify(newEntry.formatChangesApplied())

                // This will be picked up as a new history entry the next time we refresh,
                // so no need to add it as an entry here.
                clipboardManager.setContents(ClipboardContents(text = url.toString(), image = null))
            }
        }
    }

}
