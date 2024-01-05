package garden.ephemeral.clipboard.ninja.notifications

import garden.ephemeral.clipboard.ninja.Platform

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
    companion object : Notifier by if (Platform.isWindows) {
        WindowsNotifier()
    } else {
        TODO(
            "Non-Windows is not yet supported. " +
                    "If you want this on another platform, " +
                    "talk to me - it probably isn't hard to get something going!"
        )
    }
}
