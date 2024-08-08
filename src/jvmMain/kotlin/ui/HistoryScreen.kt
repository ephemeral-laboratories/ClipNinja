@file:OptIn(ExperimentalFoundationApi::class)

package garden.ephemeral.clipninja.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import garden.ephemeral.clipninja.clipboard.ClipboardManager
import garden.ephemeral.clipninja.clipninja.generated.resources.Res
import garden.ephemeral.clipninja.clipninja.generated.resources.action_copy
import garden.ephemeral.clipninja.clipninja.generated.resources.action_settings
import garden.ephemeral.clipninja.clipninja.generated.resources.changes_made
import garden.ephemeral.clipninja.clipninja.generated.resources.placeholder_unrecognized_data
import garden.ephemeral.clipninja.clipninja.generated.resources.placeholder_unrecognized_text
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun HistoryScreen(
    clipboardManager: ClipboardManager,
    clipboardEntries: List<ClipboardHistoryEntry>,
    settings: Settings,
    onSettingsClicked: () -> Unit
) {
    EphemeralLaboratoriesTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = { onSettingsClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(Res.string.action_settings),
                        )
                    }
                }
            ) {
                LazyColumn {
                    itemsIndexed(items = clipboardEntries) { index, clipboardEntry ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    color = if (index % 2 == 0)
                                        Color.Transparent
                                    else
                                        MaterialTheme.colors.onBackground.copy(alpha = 0.05f)
                                )
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(weight = 1.0f),
                            ) {
                                if (clipboardEntry.contents.text != null) {
                                    if (clipboardEntry.recognised || (settings.showAllText.value && settings.reallyShowAllText.value)) {
                                        SelectionContainer {
                                            Text(text = clipboardEntry.contents.text.trim())
                                        }
                                    } else {
                                        Text(text = stringResource(Res.string.placeholder_unrecognized_text))
                                    }
                                } else if (clipboardEntry.contents.image != null) {
                                    Image(
                                        bitmap = clipboardEntry.contents.image.toImage().toComposeImageBitmap(),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .sizeIn(maxWidth = 360.dp, maxHeight = 360.dp)
                                    )
                                } else {
                                    Text(text = stringResource(Res.string.placeholder_unrecognized_data))
                                }
                            }

                            // No weight on this means it will always get the width it asks for.
                            // This is to prevent things like the button text wrapping.
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (clipboardEntry.changesApplied.isNotEmpty()) {
                                    TooltipArea(
                                        tooltip = {
                                            Text(text = clipboardEntry.formatChangesApplied())
                                        }
                                    ) {
                                        Icon(
                                            painter = Branding.Shuriken,
                                            contentDescription = stringResource(Res.string.changes_made),
                                            modifier = Modifier.requiredSize(24.dp)
                                        )
                                    }
                                }

                                // XXX: Show only if different from the current clipboard content?
                                TextButton(
                                    onClick = { clipboardManager.setContents(clipboardEntry.contents) },
                                    modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                                ) {
                                    Text(text = stringResource(Res.string.action_copy))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
