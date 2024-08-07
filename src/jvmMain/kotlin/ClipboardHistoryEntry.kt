package garden.ephemeral.clipninja

import garden.ephemeral.clipninja.clipboard.ClipboardContents

/**
 * Holds one entry in the clipboard history.
 */
class ClipboardHistoryEntry(
    val contents: ClipboardContents,
    val recognised: Boolean = false,
    val changesApplied: List<String> = emptyList(),
) {
    fun formatChangesApplied() = (listOf("Changes applied:") + changesApplied).joinToString(separator = "\n \u2022")
}
