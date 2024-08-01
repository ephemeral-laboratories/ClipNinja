package garden.ephemeral.clipboard.ninja

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@Preview
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
                SwitchRow(settings.showAllText, "Show all text (WARNING: EXPOSES PRIVATE DATA)")
                SwitchRow(settings.enableDiscordEmbed, "Make Discord embeds work")
                SwitchRow(settings.enableStripTracking, "Strip tracking parameters from URLs")
            }
        }
    }
}
