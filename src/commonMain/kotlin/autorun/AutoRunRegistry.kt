package garden.ephemeral.clipninja.autorun

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * A registry into which an autorun record can be placed which will cause the currently-running
 * application to be run automatically on login, system start, etc.
 */
interface AutoRunRegistry {

    /**
     * Tests whether the autorun record is installed.
     *
     * @return `true` if the autorun record is installed, `false` otherwise.
     */
    fun isInstalled(): Boolean

    /**
     * Installs the autorun record.
     */
    fun install()

    /**
     * Uninstalls the autorun record.
     */
    fun uninstall()

    /**
     * Installs or uninstalls the record as appropriate to match the given value.
     *
     * @param install `true` to install, `false` to uninstall.
     * @return `true` if the autorun record is now installed, `false` otherwise.
     */
    fun updateInstalled(install: Boolean): Boolean {
        if (isInstalled() != install) {
            when (install) {
                true -> install()
                else -> uninstall()
            }
        }
        return isInstalled()
    }
}

/**
 * Creates an appropriate clipboard manager for the current platform.
 */
expect fun createAutoRunRegistry(): AutoRunRegistry

@Composable
fun rememberAutoRunRegistry() = remember { createAutoRunRegistry() }
