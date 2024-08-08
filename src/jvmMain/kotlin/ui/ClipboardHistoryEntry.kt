package garden.ephemeral.clipninja.ui

import androidx.compose.runtime.Composable
import garden.ephemeral.clipninja.clipboard.ClipboardContents
import garden.ephemeral.clipninja.clipninja.generated.resources.Res
import garden.ephemeral.clipninja.clipninja.generated.resources.changes_applied_heading
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

/**
 * Holds one entry in the clipboard history.
 */
class ClipboardHistoryEntry(
    val contents: ClipboardContents,
    val recognised: Boolean = false,
    val changesApplied: List<String> = emptyList(),
) {
    // Common helper method for the two paths (one async, one Composable).
    // Not sure if there is a cleaner way to deal with this.
    private fun formatChangesAppliedCommon(heading: String) = (listOf(heading) + changesApplied)
        .joinToString(separator = "\n \u2022 ")

    @Composable
    fun formatChangesApplied() = formatChangesAppliedCommon(stringResource(Res.string.changes_applied_heading))

    suspend fun formatChangesAppliedAsync() = formatChangesAppliedCommon(getString(Res.string.changes_applied_heading))
}
