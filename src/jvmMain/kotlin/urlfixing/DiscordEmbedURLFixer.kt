package garden.ephemeral.clipninja.urlfixing

import garden.ephemeral.clipninja.util.URL
import garden.ephemeral.clipninja.clipninja.generated.resources.Res
import garden.ephemeral.clipninja.clipninja.generated.resources.change_replaced_domain_to_fix_embeds
import org.jetbrains.compose.resources.getString

/**
 * URL fixer replacing various hosts with alternative hosts which handle Discord
 * embeds better.
 */
internal object DiscordEmbedURLFixer : URLFixer {
    private val replacements = mapOf(
        "twitter.com" to "vxtwitter.com",
        "x.com" to "fixupx.com",
        "tiktok.com" to "fxtiktok.com",
        "reddit.com" to "rxddit.com",
        "old.reddit.com" to "old.rxddit.com",
        "www.reddit.com" to "www.rxddit.com",
        "instagram.com" to "ddinstagram.com"
    )

    override suspend fun fix(url: URL): Pair<URL, String?> {
        val replacementHost = replacements[url.host]
        if (replacementHost != null) {
            return modified(
                url.copy(host = replacementHost),
                getString(Res.string.change_replaced_domain_to_fix_embeds),
            )
        }
        return unmodified(url)
    }
}
