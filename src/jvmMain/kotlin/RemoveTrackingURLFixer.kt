package garden.ephemeral.clipboard.ninja

/**
 * URL fixer removing various query parameters which are used to track the source of clicks.
 */
internal class RemoveTrackingURLFixer : URLFixer {
    override fun fix(url: URL): Pair<URL, String?> {
        if (url.query != null && url.query.keys.any { k -> k.startsWith(UTM_PREFIX) }) {
            val newUrl = url.copy(query = url.query.asSequence()
                .reject { (k, _) -> isTrackingQueryKey(url, k) }
                .toMap()
                .ifEmpty { null })
            if (newUrl == url) return unmodified(url)
            return modified(newUrl, "Removed tracking tokens from URL")
        }
        return unmodified(url)
    }

    private fun isTrackingQueryKey(url: URL, queryKey: String): Boolean {
        if (queryKey.startsWith(UTM_PREFIX)) {
            return true
        }

        if (url.host in KNOWN_YOUTUBE_DOMAINS) {
            return queryKey == YOUTUBE_TRACKING_QUERY_KEY
        }

        return false
    }

    companion object {
        const val UTM_PREFIX = "utm_"
        const val YOUTUBE_TRACKING_QUERY_KEY = "si"

        val KNOWN_YOUTUBE_DOMAINS = setOf("youtube.com", "youtu.be")
    }
}