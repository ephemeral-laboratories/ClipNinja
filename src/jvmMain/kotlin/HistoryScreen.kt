@file:OptIn(ExperimentalFoundationApi::class)

package garden.ephemeral.clipboard.ninja

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun HistoryScreen(clipboardEntries: List<ClipboardHistoryEntry>, settings: Settings, onSettingsClicked: () -> Unit) {
    EphemeralLaboratoriesTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = { onSettingsClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                        )
                    }
                }
            ) {
                LazyColumn {
                    itemsIndexed(items = clipboardEntries) { index, clipboardEntry ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
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
                            if (clipboardEntry.contents.text != null) {
                                if (clipboardEntry.recognised || settings.showAllText.value) {
                                    SelectionContainer {
                                        Text(text = clipboardEntry.contents.text.trim())
                                    }
                                } else {
                                    Text(text = "[UNRECOGNISED TEXT REDACTED]")
                                }
                            } else if (clipboardEntry.contents.image != null) {
                                Image(
                                    bitmap = clipboardEntry.contents.image.toImage().toComposeImageBitmap(),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .sizeIn(maxWidth = 360.dp, maxHeight = 360.dp)
                                )
                            } else {
                                Text(text = "[UNRECOGNISED DATA]")
                            }

                            if (clipboardEntry.changesApplied.isNotEmpty()) {
                                TooltipArea(
                                    tooltip = {
                                        Column {
                                            clipboardEntry.changesApplied.forEach { change ->
                                                Text(text = change)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = Branding.Shuriken,
                                        contentDescription = "Changes made",
                                        modifier = Modifier.requiredSize(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
