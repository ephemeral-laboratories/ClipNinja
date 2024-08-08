package garden.ephemeral.clipninja

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class URLTest : FreeSpec({
    "fromString" - {
        "parsing no query" {
            URL.fromString("https://example.com/")
                .shouldBe(URL("https", "example.com", "/", null))
        }

        "parsing one query param" {
            URL.fromString("https://example.com/search?q=blah")
                .shouldBe(URL("https", "example.com", "/search", mapOf("q" to "blah")))
        }

        "parsing two query params" {
            URL.fromString("https://example.com/search?q=blah&utm_source=somewhere")
                .shouldBe(
                    URL(
                        "https", "example.com", "/search",
                        mapOf("q" to "blah", "utm_source" to "somewhere")
                    )
                )
        }
    }

    "toString" - {
        "formatting no query" {
            URL("https", "example.com", "/", null)
                .toString()
                .shouldBe("https://example.com/")
        }

        "formatting one query param" {
            URL("https", "example.com", "/search", mapOf("q" to "blah"))
                .toString()
                .shouldBe("https://example.com/search?q=blah")
        }

        "formatting two query params" {
            URL("https", "example.com", "/search", mapOf("q" to "blah", "utm_source" to "somewhere"))
                .toString()
                .shouldBe("https://example.com/search?q=blah&utm_source=somewhere")
        }
    }
})
