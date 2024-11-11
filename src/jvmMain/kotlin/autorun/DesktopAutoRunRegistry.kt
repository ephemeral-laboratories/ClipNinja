package garden.ephemeral.clipninja.autorun

import garden.ephemeral.clipninja.util.Platform

actual fun createAutoRunRegistry(): AutoRunRegistry = if (Platform.isWindows) {
    WindowsAutoRunRegistry()
} else {
    TODO(
        "Non-Windows is not yet supported. " +
                "If you want this on another platform, " +
                "talk to me - it probably isn't hard to get something going!"
    )
}
