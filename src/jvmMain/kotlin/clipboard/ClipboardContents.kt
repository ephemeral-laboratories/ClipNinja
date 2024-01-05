package garden.ephemeral.clipboard.ninja.clipboard

import garden.ephemeral.clipboard.ninja.image.ImageHolder
import java.awt.Image
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.util.*

/**
 * Representation of clipboard contents.
 *
 * Equivalent to the JDK's `Transferable` interface, except that it is impossible to rely on
 * object equality for `Transferable`, whereas we want to be able to rely on object equality
 * so that we don't process the same clipboard value more than once.
 *
 * Unfortunately, because `Transferable` does not allow for object equality comparison,
 * we are forced to model this class by eagerly setting the text or image.
 */
data class ClipboardContents(
    val text: String?,
    val image: ImageHolder?,
) {
    /**
     * Converts to a [Transferable] in order to set back into a clipboard.
     */
    fun toTransferable(): Transferable {
        val transferables = buildList {
            text?.let { text -> add(StringSelection(text)) }
            image?.let { image -> add(ImageTransferable(image.toImage())) }
        }
        return CompositeTransferable(transferables)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ClipboardContents) return false
        return Objects.equals(text, other.text) &&
                Objects.equals(image, other.image)
    }

    override fun hashCode() = Objects.hash(text, image)

    companion object {
        /**
         * Converts from a [Transferable].
         */
        fun fromTransferable(transferable: Transferable): ClipboardContents {
            val text = if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                transferable.getTransferData(DataFlavor.stringFlavor) as String
            } else {
                null
            }
            val image = if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                ImageHolder(transferable.getTransferData(DataFlavor.imageFlavor) as Image)
            } else {
                null
            }
            return ClipboardContents(text, image)
        }
    }
}
