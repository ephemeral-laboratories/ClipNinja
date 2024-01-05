package garden.ephemeral.clipboard.ninja.clipboard.win32

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.DWORD
import com.sun.jna.win32.W32APIOptions

@Suppress("FunctionName")
interface ExtraUser32 : User32 {
    /**
     * Retrieves the clipboard sequence number for the current window station.
     *
     * @return the clipboard sequence number. If you do not have `WINSTA_ACCESSCLIPBOARD`
     *         access to the window station, the function returns zero.
     */
    fun GetClipboardSequenceNumber(): DWORD

    companion object : ExtraUser32 by Native.load("user32", ExtraUser32::class.java, W32APIOptions.DEFAULT_OPTIONS)
}
