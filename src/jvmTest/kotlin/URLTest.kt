package garden.ephemeral.clipboard.ninja

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class URLTest {
    @Test
    fun `parsing no query`() {
        assertThat(URL.fromString("https://example.com/"))
            .isEqualTo(URL("https", "example.com", "/", null))
    }

    @Test
    fun `formatting no query`() {
        assertThat(URL("https", "example.com", "/", null).toString())
            .isEqualTo("https://example.com/")
    }

    @Test
    fun `parsing one query param`() {
        assertThat(URL.fromString("https://example.com/search?q=blah"))
            .isEqualTo(URL("https", "example.com", "/search", mapOf("q" to "blah")))
    }

    @Test
    fun `formatting one query param`() {
        assertThat(URL("https", "example.com", "/search", mapOf("q" to "blah")).toString())
            .isEqualTo("https://example.com/search?q=blah")
    }

    @Test
    fun `parsing two query params`() {
        assertThat(URL.fromString("https://example.com/search?q=blah&utm_source=somewhere"))
            .isEqualTo(
                URL(
                    "https", "example.com", "/search",
                    mapOf("q" to "blah", "utm_source" to "somewhere")
                )
            )
    }

    @Test
    fun `formatting two query params`() {
        assertThat(
            URL(
                "https", "example.com", "/search",
                mapOf("q" to "blah", "utm_source" to "somewhere")
            ).toString()
        ).isEqualTo("https://example.com/search?q=blah&utm_source=somewhere")
    }
}