package garden.ephemeral.clipninja.urlfixing

import garden.ephemeral.clipninja.clipninja.generated.resources.Res
import garden.ephemeral.clipninja.clipninja.generated.resources.change_replaced_domain_to_fix_embeds
import garden.ephemeral.clipninja.util.URL
import org.jetbrains.compose.resources.getString

/**
 * URL fixer replacing various hosts with alternative hosts which handle Discord
 * embeds better.
 */
internal object DiscordEmbedURLFixer : URLFixer {
    private data class Replacement(val old: String, val new: String) {
        val oldSuffix: String = ".$old"
        val newSuffix: String = ".$new"
    }

    private val replacements = listOf(
        Replacement("twitter.com", "vxtwitter.com"),
        Replacement("x.com", "fixupx.com"),
        Replacement("tiktok.com", "vxtiktok.com"),
        Replacement("reddit.com", "rxddit.com"),
        Replacement("instagram.com", "ddinstagram.com"),
    )

    override suspend fun fix(url: URL): Pair<URL, String?> {
        for (replacement in replacements) {
            if (url.host == replacement.old) {
                return modified(
                    url.copy(host = replacement.new),
                    getString(Res.string.change_replaced_domain_to_fix_embeds),
                )
            }
            if (url.host.endsWith(replacement.oldSuffix)) {
                return modified(
                    url.copy(
                        host = url.host.replaceRange(
                            startIndex = url.host.length - replacement.oldSuffix.length,
                            endIndex = url.host.length,
                            replacement = replacement.newSuffix,
                        )
                    ),
                    getString(Res.string.change_replaced_domain_to_fix_embeds),
                )
            }
        }
        return unmodified(url)
    }
}
