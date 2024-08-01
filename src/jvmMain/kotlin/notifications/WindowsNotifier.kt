package garden.ephemeral.clipninja.notifications

import de.mobanisto.toast4j.ToastBuilder
import de.mobanisto.toast4j.Toaster
import de.mobanisto.wintoast.Aumi
import de.mobanisto.wintoast.WinToastTemplate
import garden.ephemeral.clipninja.BuildKonfig
import org.slf4j.LoggerFactory

class WindowsNotifier : Notifier {
    private val toaster: Toaster = Toaster.forAumi(
        Aumi(
            BuildKonfig.OrganisationName,
            BuildKonfig.ApplicationName,
            "",
            BuildKonfig.Version,
        )
    )

    private val available = toaster.initialize()

    override fun notify(message: String) {
        if (available) {
            val template = ToastBuilder(WinToastTemplate.WinToastTemplateType.ToastText01)
                .setSilent()
                .setLine1(message)
                .build()

            toaster.showToast(template)
        } else {
            LoggerFactory.getLogger(this.javaClass).warn("Toaster not available: $message")
        }
    }
}