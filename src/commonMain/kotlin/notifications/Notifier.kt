package garden.ephemeral.clipninja.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Abstraction of notification interface.
 */
interface Notifier {

    /**
     * Notifies the user about something.
     */
    fun notify(message: String)

    /**
     * Companion object allows calling the interface as if it were a static utility.
     */
    companion object : Notifier by createNotifier()
}

/**
 * Creates an appropriate notifier for the current platform.
 */
expect fun createNotifier(): Notifier

@Composable
fun rememberNotifier() = remember { createNotifier() }
