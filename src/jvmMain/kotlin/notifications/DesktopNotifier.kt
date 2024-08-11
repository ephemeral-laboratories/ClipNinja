package garden.ephemeral.clipninja.notifications

import garden.ephemeral.clipninja.util.Platform

actual fun createNotifier(): Notifier = if (Platform.isWindows) {
    WindowsNotifier()
} else {
    TODO(
        "Non-Windows is not yet supported. " +
                "If you want this on another platform, " +
                "talk to me - it probably isn't hard to get something going!"
    )
}
