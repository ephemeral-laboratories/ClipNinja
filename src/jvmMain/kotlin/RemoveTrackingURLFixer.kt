package garden.ephemeral.clipninja

import garden.ephemeral.clipninja.clipninja.generated.resources.Res
import garden.ephemeral.clipninja.clipninja.generated.resources.change_removed_tracking_tokens
import org.jetbrains.compose.resources.getString

/**
 * URL fixer removing various query parameters which are used to track the source of clicks.
 */
internal class RemoveTrackingURLFixer : URLFixer {
    override suspend fun fix(url: URL): Pair<URL, String?> {
        if (url.query != null && url.query.keys.any { k -> k.startsWith(UTM_PREFIX) }) {
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