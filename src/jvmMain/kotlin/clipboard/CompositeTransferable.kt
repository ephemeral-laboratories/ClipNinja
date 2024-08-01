package garden.ephemeral.clipninja.clipboard

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException

/**
 * A transferable formed as a union of the provided transferables.
 */
class CompositeTransferable(private val transferables: List<Transferable>) : Transferable {
    private val flavors = transferables
        .flatMap { t -> t.transferDataFlavors.asSequence() }
        .toTypedArray()

    override fun getTransferDataFlavors() = flavors.clone()

    override fun isDataFlavorSupported(flavor: DataFlavor?) = flavors.contains(flavor)

    override fun getTransferData(flavor: DataFlavor?): Any {
        transferables.forEach { transferable ->
            if (transferable.isDataFlavorSupported(flavor)) {
                return transferable.getTransferData(flavor)
            }
        }
        throw UnsupportedFlavorException(flavor)
    }
}