package garden.ephemeral.clipboard.ninja.clipboard.win32

import com.sun.jna.Native
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

@Suppress("FunctionName")
interface ExtraUser32 : StdCallLibrary {
    /**
     * Retrieves the clipboard sequence number for the current window station.
     *
     * @return the clipboard sequence number. If you do not have `WINSTA_ACCESSCLIPBOARD`
     *         access to the window station, the function returns zero.
     */
    fun GetClipboardSequenceNumber(): Int

    companion object : ExtraUser32 by Native.load("user32", ExtraUser32::class.java, W32APIOptions.DEFAULT_OPTIONS)
}
