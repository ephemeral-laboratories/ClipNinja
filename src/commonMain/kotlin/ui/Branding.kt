package garden.ephemeral.clipninja.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import garden.ephemeral.clipninja.BuildKonfig
import garden.ephemeral.clipninja.clipninja.generated.resources.Res
import garden.ephemeral.clipninja.clipninja.generated.resources.app_icon
import garden.ephemeral.clipninja.clipninja.generated.resources.ninny
import garden.ephemeral.clipninja.clipninja.generated.resources.shuriken
import org.jetbrains.compose.resources.painterResource

object Branding {
    val AppName = BuildKonfig.ApplicationName
    val AppIcon @Composable get() = painterResource(Res.drawable.app_icon)

    val Shuriken @Composable get() = painterResource(Res.drawable.shuriken)

    val NinjaPicture @Composable get() = painterResource(Res.drawable.ninny)
}

@Composable
fun EphemeralLaboratoriesTheme(body: @Composable () -> Unit) {
    MaterialTheme(
        colors = darkColors(
            primary = Color(233, 55, 84),
            secondary = Color(65, 152, 223)
        )
    ) {
        body()
    }
}