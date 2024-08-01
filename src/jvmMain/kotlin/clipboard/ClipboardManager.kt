package garden.ephemeral.clipninja.clipboard

import kotlinx.coroutines.flow.Flow

/**
 * Clipboard interface, vaguely similar to what Compose has, but with
 * a simpler API, and under our own control, so we can add support for
 * images and whatever else.
 */
interface ClipboardManager {
    fun getContentsFlow(): Flow<ClipboardContents>

    fun getContents(): ClipboardContents?

    fun setContents(contents: ClipboardContents)

}
