package garden.ephemeral.clipninja.urlfixing

import garden.ephemeral.clipninja.util.URL
import garden.ephemeral.clipninja.clipninja.generated.resources.Res
import garden.ephemeral.clipninja.clipninja.generated.resources.change_removed_tracking_tokens
import garden.ephemeral.clipninja.util.reject
import garden.ephemeral.clipninja.util.toMap
import org.jetbrains.compose.resources.getString

/**
 * URL fixer removing various query parameters which are used to track the source of clicks.
 */
internal object RemoveTrackingURLFixer : URLFixer {
    private const val UTM_PREFIX = "utm_"

    private data class KnownSiteEntry(
        val shortName: String,
        val queryKeys: Set<String>,
        val domains: Set<String>,
    )

    // Compare with the list at: chrome://global/content/antitracking/StripOnShare.json
    private val knownSites = listOf(
        KnownSiteEntry(
            shortName = "YouTube",
            queryKeys = setOf("si"),
            domains = setOf("www.youtube.com", "youtube.com", "youtu.be"),
        ),
        KnownSiteEntry(
            shortName = "Twitter",
            queryKeys = setOf("ref_src", "ref_url"),
            domains = setOf("www.twitter.com", "twitter.com", "www.x.com", "x.com"),
        ),
        KnownSiteEntry(
            shortName = "Instagram",
            queryKeys = setOf("igshid", "ig_rid"),
            domains = setOf("www.instagram.com", "instagram.com"),
        ),
        KnownSiteEntry(
            shortName = "Amazon",
            queryKeys = setOf("keywords", "pd_rd_r", "pd_rd_w", "pd_rd_wg", "pf_rd_r", "pf_rd_p", "sr", "content-id"),
            domains = setOf(
                "www.amazon.com", "amazon.com",
                "www.amazon.de", "amazon.de",
                "www.amazon.nl", "amazon.nl",
                "www.amazon.fr", "amazon.fr",
                "www.amazon.co.jp", "amazon.co.jp",
                "www.amazon.in", "amazon.in",
                "www.amazon.es", "amazon.es",
                "www.amazon.ac", "amazon.ac",
                "www.amazon.cn", "amazon.cn",
                "www.amazon.eg", "amazon.eg",
                "www.amazon.in", "amazon.in",
                "www.amazon.co.uk", "amazon.co.uk",
                "www.amazon.it", "amazon.it",
                "www.amazon.pl", "amazon.pl",
                "www.amazon.sg", "amazon.sg",
                "www.amazon.ca", "amazon.ca",
            )
        ),
        KnownSiteEntry(
            shortName = "Handelsblatt",
            queryKeys = setOf("share"),
            domains = setOf("www.handelsblatt.com", "handelsblatt.com"),
        ),
    )

    override suspend fun fix(url: URL): Pair<URL, String?> {
        if (url.query != null) {
            val newUrl = url.copy(query = url.query.asSequence()
                .reject { (k, _) -> isTrackingQueryKey(url, k) }
                .toMap()
                .ifEmpty { null })
            if (newUrl == url) return unmodified(url)
            return modified(newUrl, getString(Res.string.change_removed_tracking_tokens))
        }
        return unmodified(url)
    }

    private fun isTrackingQueryKey(url: URL, queryKey: String): Boolean {
        if (queryKey.startsWith(UTM_PREFIX)) {
            return true
        }

        knownSites.forEach { site ->
            if (url.host in site.domains) {
                return queryKey in site.queryKeys
            }
        }

        return false
    }
}
