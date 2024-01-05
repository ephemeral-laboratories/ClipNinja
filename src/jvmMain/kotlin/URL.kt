package garden.ephemeral.clipboard.ninja

import java.net.MalformedURLException

data class URL(
    val protocol: String,
    val host: String,
    val path: String,
    val query: Map<String, String>?
) {
    init {
        require(path.startsWith('/'))
    }

    override fun toString() = buildString {
        append("$protocol://$host$path")
        if (query != null) {
            val queryString = query.entries.joinToString(separator = "&") { (key, value) ->
                "$key=$value"
            }
            append("?$queryString")
        }
    }

    companion object {
        fun tryFromString(string: String): URL? {
            return try {
                fromString(string)
            } catch (e: MalformedURLException) {
                null
            }
        }

        fun fromString(string: String): URL {
            val javaUrl = java.net.URL(string)
            return URL(
                javaUrl.protocol,
                javaUrl.host,
                javaUrl.path,
                javaUrl.query
                    ?.splitToSequence('&')
                    ?.map { queryElement ->
                        val parts = queryElement.split('=')
                        val name = parts.getOrElse(0) { "" }
                        val value = parts.getOrElse(1) { "" }
                        Pair(name, value)
                    }
                    ?.toMap()
            )
        }
    }
}
