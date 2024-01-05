package garden.ephemeral.clipboard.ninja

/**
 * URL fixer replacing various hosts with alternative hosts which handle Discord
 * embeds better.
 */
internal class DiscordEmbedURLFixer : URLFixer {
    override fun fix(url: URL): Pair<URL, String?> {
        val replacementHost = replacements[url.host]
        if (replacementHost != null) {
            return modified(url.copy(host = replacementHost), "Replaced URL domain to fix embeds")
        }
        return unmodified(url)
    }

    companion object {
        val replacements = mapOf(
            "twitter.com" to "vxtwitter.com",
            "x.com" to "fixupx.com",
            "tiktok.com" to "fxtiktok.com",
        )
    }
}
