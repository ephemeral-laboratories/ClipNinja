package garden.ephemeral.clipninja

/**
 * Information about the current platform on which the application is running.
 */
object Platform {
    val isWindows by lazy { System.getProperty("os.name").lowercase().startsWith("windows") }

}
