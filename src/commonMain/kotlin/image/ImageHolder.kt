package garden.ephemeral.clipninja.image

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toPixelMap

/**
 * Holder for an image.
 *
 * Implements [equals] and [hashCode] for simpler comparison of images.
 * These methods compare the internals of the image, so it's possible to have
 * two images which look identical, but don't compare as equal.
 */
class ImageHolder(private val imageBitmap: ImageBitmap) {
    fun toComposeImageBitmap() = imageBitmap

    override fun equals(other: Any?) = other is ImageHolder && imageBitmap.contentEquals(other.imageBitmap)

    override fun hashCode() = imageBitmap.contentHashCode()

    companion object {
        private fun PixelMap.contentEquals(other: PixelMap): Boolean {
            require(bufferOffset == 0 && other.bufferOffset == 0)
            return buffer.contentEquals(other.buffer)
        }

        private fun PixelMap.contentHashCode(): Int {
            require(bufferOffset == 0)
            return buffer.contentHashCode()
        }

        private fun ImageBitmap.contentEquals(other: ImageBitmap) = width == other.width &&
                height == other.height &&
                colorSpace == other.colorSpace &&
                hasAlpha == other.hasAlpha &&
                config == other.config &&
                toPixelMap().contentEquals(other.toPixelMap())

        private fun ImageBitmap.contentHashCode() = width.hashCode()
            .times(31).plus(height.hashCode())
            .times(31).plus(colorSpace.hashCode())
            .times(31).plus(hasAlpha.hashCode())
            .times(31).plus(config.hashCode())
            .times(31).plus(toPixelMap().contentHashCode())
    }
}
