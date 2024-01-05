package garden.ephemeral.clipboard.ninja

import garden.ephemeral.clipboard.ninja.clipboard.ClipboardContents

/**
 * Holds one entry in the clipboard history.
 */
class ClipboardHistoryEntry(
    val contents: ClipboardContents,
    val recognised: Boolean = false,
    val changesApplied: List<String> = emptyList(),
)
