package garden.ephemeral.clipninja.autorun

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.Win32Exception
import com.sun.jna.platform.win32.WinReg
import garden.ephemeral.clipninja.BuildKonfig

class WindowsAutoRunRegistry : AutoRunRegistry {
    private val appName = BuildKonfig.ApplicationName
    private val fullCommandLine: String by lazy {
        val appPath = ProcessHandle.current().info().command().orElseThrow()
        "\"$appPath\" --autostart=true"
    }

    override fun isInstalled(): Boolean {
        val value: String
        try {
            value = Advapi32Util.registryGetStringValue(ROOT_KEY, RUN_KEY, appName)
        } catch (e: Win32Exception) {
            if (e.errorCode == 2) {
                return false
            } else {
                throw e
            }
        }
        return value == fullCommandLine
    }

    override fun install() {
        if (!Advapi32Util.registryKeyExists(ROOT_KEY, RUN_KEY)) {
            Advapi32Util.registryCreateKey(ROOT_KEY, RUN_KEY)
        }

        Advapi32Util.registrySetStringValue(ROOT_KEY, RUN_KEY, appName, fullCommandLine)
    }

    override fun uninstall() {
        if (Advapi32Util.registryKeyExists(ROOT_KEY, RUN_KEY)) {
            Advapi32Util.registryDeleteValue(ROOT_KEY, RUN_KEY, appName)
        }
    }

    private companion object {
        private val ROOT_KEY = WinReg.HKEY_CURRENT_USER
        private const val RUN_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Run"
    }
}
