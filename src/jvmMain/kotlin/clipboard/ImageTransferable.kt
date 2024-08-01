package garden.ephemeral.clipninja.clipboard

import java.awt.Image
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException

/**
 * A transferable wrapping an AWT image.
 */
class ImageTransferable(private val image: Image): Transferable {
    override fun getTransferDataFlavors() = flavors.clone()

    override fun isDataFlavorSupported(flavor: DataFlavor?) = flavors.contains(flavor)

    override fun getTransferData(flavor: DataFlavor?) = if (flavor == DataFlavor.imageFlavor) {
        image
    } else {
        throw UnsupportedFlavorException(flavor)
    }

    companion object {
        val flavors = arrayOf(DataFlavor.imageFlavor)
    }
}