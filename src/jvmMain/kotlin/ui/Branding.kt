package garden.ephemeral.clipninja.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import garden.ephemeral.clipninja.BuildKonfig

object Branding {
    val AppName = BuildKonfig.ApplicationName
    val AppIcon @Composable get() = painterResource("/app-icon.svg")

    val Shuriken @Composable get() = painterResource("/shuriken.svg")

    val NinjaPicture @Composable get() = painterResource("/ninny.png")
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