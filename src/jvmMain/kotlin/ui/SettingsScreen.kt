package garden.ephemeral.clipninja.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import garden.ephemeral.clipninja.BuildKonfig
import garden.ephemeral.clipninja.clipninja.generated.resources.Res
import garden.ephemeral.clipninja.clipninja.generated.resources.copyright_format
import garden.ephemeral.clipninja.clipninja.generated.resources.make_discord_embeds_work
import garden.ephemeral.clipninja.clipninja.generated.resources.really_show_all_text
import garden.ephemeral.clipninja.clipninja.generated.resources.show_all_text
import garden.ephemeral.clipninja.clipninja.generated.resources.strip_tracking_parameters
import garden.ephemeral.clipninja.clipninja.generated.resources.version_format
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(settings: Settings) {

    @Composable
    fun SwitchRow(state: MutableState<Boolean>, label: String) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = state.value,
                onCheckedChange = { state.value = it }
            )
            Text(
                text = label,
                modifier = Modifier.clickable { state.value = !state.value }
            )
        }
    }

    EphemeralLaboratoriesTheme {
        Surface {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                SwitchRow(settings.showAllText, stringResource(Res.string.show_all_text))
                if (settings.showAllText.value) {
                    SwitchRow(settings.reallyShowAllText, stringResource(Res.string.really_show_all_text))
                }
                SwitchRow(settings.enableDiscordEmbed, stringResource(Res.string.make_discord_embeds_work))
                SwitchRow(settings.enableStripTracking, stringResource(Res.string.strip_tracking_parameters))

                Spacer(modifier = Modifier.height(16.dp))

                Divider(modifier = Modifier.width(500.dp))

                SelectionContainer {
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.wrapContentSize()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(
                                Res.string.version_format,
                                BuildKonfig.ApplicationName,
                                BuildKonfig.Version,
                            ),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.h6,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(
                                Res.string.copyright_format,
                                BuildKonfig.CopyrightYears,
                                BuildKonfig.OrganisationName,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun SettingsScreenPreview() {
    SettingsScreen(Settings())
}
