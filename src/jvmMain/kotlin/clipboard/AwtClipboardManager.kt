package garden.ephemeral.clipboard.ninja.clipboard

import garden.ephemeral.clipboard.ninja.Platform
import garden.ephemeral.clipboard.ninja.clipboard.win32.ExtraUser32
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import java.awt.HeadlessException
import java.awt.Toolkit

class AwtClipboardManager : ClipboardManager {
    private val systemClipboard by lazy {
        try {
            Toolkit.getDefaultToolkit().systemClipboard
        } catch (e: HeadlessException) {
            null
        }
    }

    // Naive flow of clipboard contents which simply gets the contents at a regular interval,
    // and removes duplicates.
    private fun getNaiveContentsFlow(): Flow<ClipboardContents> {
        return flow {
            while (true) {
                delay(100)
                getContents()?.let { emit(it) }
            }
        }.distinctUntilChanged()
    }

    // Special implementation on Windows where we can use a native call to figure out whether the clipboard
    // actually changed since last time.
    // Other platforms may want better mechanisms too, but for now...
    // This implementation also removes duplicates, because you can't be completely sure that a new sequence
    // number necessarily means that the new clipboard will be a new value.
    private fun getWindowsContentsFlow(): Flow<ClipboardContents> {
        return flow {
            var lastSequenceNumber = ExtraUser32.GetClipboardSequenceNumber().toInt()
            getContents()?.let { emit(it) }

            while (true) {
                delay(100)
                val currentSequenceNumber = ExtraUser32.GetClipboardSequenceNumber().toInt()
                if (lastSequenceNumber != currentSequenceNumber) {
                    lastSequenceNumber = currentSequenceNumber
                    getContents()?.let { emit(it) }
                }
            }
        }.distinctUntilChanged()
    }

    override fun getContentsFlow() = if (Platform.isWindows) getWindowsContentsFlow() else getNaiveContentsFlow()


    override fun getContents(): ClipboardContents? {
        val transferable = try {
            systemClipboard?.getContents(null) ?: return null
        } catch (e: IllegalStateException) {
            // Windows seems to throw this exception seemingly at random, and then the next
            // try works fine.
            return null
        }

        return ClipboardContents.fromTransferable(transferable)
    }

    override fun setContents(contents: ClipboardContents) {
        // TODO: Open question, if we get IllegalStateException _here_, then what?
        systemClipboard?.setContents(contents.toTransferable(), null)
    }
}