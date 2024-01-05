package garden.ephemeral.clipboard.ninja

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication {
    MaterialTheme {
        val (showSwitch1, setShowSwitch1) = remember { mutableStateOf(false) }
        val (showSwitch2, setShowSwitch2) = remember { mutableStateOf(true) }
        val (showSwitch3, setShowSwitch3) = remember { mutableStateOf(true) }

        LazyVerticalGrid(
            modifier = Modifier.padding(16.dp),
            columns = GridCells.Fixed(2)
        ) {
            item { Text("A switch") }
            item { Switch(showSwitch1, setShowSwitch1) }
            item { Text("Another switch") }
            item { Switch(showSwitch2, setShowSwitch2) }
            item { Text("A third switch with an even longer label") }
            item { Switch(showSwitch3, setShowSwitch3) }
        }
    }
}
