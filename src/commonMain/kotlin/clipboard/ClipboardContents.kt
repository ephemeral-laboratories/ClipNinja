package garden.ephemeral.clipninja.clipboard

import garden.ephemeral.clipninja.image.ImageHolder

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
)
