package garden.ephemeral.clipninja.clipboard

import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import garden.ephemeral.clipninja.clipboard.win32.ExtraUser32
import garden.ephemeral.clipninja.image.ImageHolder
import garden.ephemeral.clipninja.util.Platform
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import java.awt.HeadlessException
import java.awt.Image
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.image.BufferedImage

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
            var lastSequenceNumber = ExtraUser32.GetClipboardSequenceNumber()
            getContents()?.let { emit(it) }

            while (true) {
                delay(100)
                val currentSequenceNumber = ExtraUser32.GetClipboardSequenceNumber()
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

        return transferable.toClipboardContents()
    }

    override fun setContents(contents: ClipboardContents) {
        // TODO: Open question, if we get IllegalStateException _here_, then what?
        systemClipboard?.setContents(contents.toTransferable(), null)
    }

    /**
     * Converts a [ClipboardContents] to a [Transferable] in order to set back into a clipboard.
     */
    private fun ClipboardContents.toTransferable(): Transferable {
        val transferables = buildList {
            text?.let { text -> add(StringSelection(text)) }
            image?.let { image -> add(ImageTransferable(image.toComposeImageBitmap().toAwtImage())) }
        }
        return CompositeTransferable(transferables)
    }

    /**
     * Converts a [Transferable] to a [ClipboardContents].
     */
    private fun Transferable.toClipboardContents(): ClipboardContents {
        val text = if (isDataFlavorSupported(DataFlavor.stringFlavor)) {
            getTransferData(DataFlavor.stringFlavor) as String
        } else {
            null
        }
        val image = if (isDataFlavorSupported(DataFlavor.imageFlavor)) {
            ImageHolder((getTransferData(DataFlavor.imageFlavor) as Image).ensureBufferedImage().toComposeImageBitmap())
        } else {
            null
        }
        return ClipboardContents(text, image)
    }

    /**
     * Redraws the image onto a new [BufferedImage] if necessary. Returns the image itself if it already is one.
     */
    private fun Image.ensureBufferedImage() = if (this is BufferedImage) {
        // Short-cut
        this
    } else {
        val copy = BufferedImage(getWidth(null), getHeight(null), BufferedImage.TYPE_INT_ARGB)
        val graphics = copy.createGraphics()
        try {
            graphics.drawImage(this, 0, 0, null)
        } finally {
            graphics.dispose()
        }
        copy
    }
}
