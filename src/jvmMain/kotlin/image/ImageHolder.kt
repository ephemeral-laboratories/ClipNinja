package garden.ephemeral.clipninja.image

import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.DataBuffer
import java.util.*

/**
 * Holder for an image.
 *
 * Implements [equals] and [hashCode] for simpler comparison of images.
 * These methods compare the internals of the image, so it's possible to have
 * two images which look identical, but don't compare as equal.
 */
class ImageHolder(private val image: BufferedImage) {
    constructor(image: Image) : this(imageToBufferedImage(image))

    fun toImage() = image

    override fun equals(other: Any?) = other is ImageHolder && imagesAreEqual(image, other.image)

    override fun hashCode() = imageHashCode(image)

    companion object {
        private fun dataBuffersAreEqual(dataBuffer1: DataBuffer, dataBuffer2: DataBuffer): Boolean {
            val size1 = dataBuffer1.size
            val size2 = dataBuffer2.size
            return size1 == size2 &&
                    (0 until size1)
                        .all { index -> dataBuffer1.getElem(index) == dataBuffer2.getElem(index) }
        }

        private fun imagesAreEqual(image1: BufferedImage, image2: BufferedImage) = image1.width == image2.width &&
                image1.height == image2.height &&
                image1.type == image2.type &&
                image1.colorModel == image2.colorModel &&
                dataBuffersAreEqual(image1.raster.dataBuffer, image2.raster.dataBuffer)

        private fun dataBufferHashCode(dataBuffer: DataBuffer): Int {
            val size = dataBuffer.size
            return (0 until size)
                .fold(size) { accumulator, value -> accumulator * 31 + value }
        }

        private fun imageHashCode(image: BufferedImage) = Objects.hash(
            image.width, image.height, image.type, image.colorModel,
            dataBufferHashCode(image.raster.dataBuffer)
        )

        private fun imageToBufferedImage(image: Image) = if (image is BufferedImage) {
            // Short-cut
            image
        } else {
            val copy = BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB)
            val graphics = copy.createGraphics()
            try {
                graphics.drawImage(image, 0, 0, null)
            } finally {
                graphics.dispose()
            }
            copy
        }
    }
}
