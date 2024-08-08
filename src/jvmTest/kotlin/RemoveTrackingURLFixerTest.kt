package garden.ephemeral.clipninja

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs

class RemoveTrackingURLFixerTest : FreeSpec({
    "does nothing for URLs which don't match" {
        val originalUrl = URL("https", "example.com", "/", null)
        val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
        newUrl.shouldBeSameInstanceAs(originalUrl)
        explanation.shouldBeNull()
    }

    "removes utm_ parameters for all URLs" {
        val originalUrl = URL("https", "example.com", "/", mapOf("utm_blah" to "blah", "foo" to "bar"))
        val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
        newUrl.shouldBe(URL("https", "example.com", "/", mapOf("foo" to "bar")))
        explanation.shouldBe("Removed tracking tokens from URL")
    }

    "removes si parameter" - {
        "for YouTube URLs" {
            val originalUrl = URL("https", "youtube.com", "/mYekiVRnM_A", mapOf("si" to "eKqSQe5NZLDjRq2e"))
            val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
            newUrl.shouldBe(URL("https", "youtube.com", "/mYekiVRnM_A", null))
            explanation.shouldBe("Removed tracking tokens from URL")
        }

        "for shortened YouTube URLs" {
            val originalUrl = URL("https", "youtu.be", "/mYekiVRnM_A", mapOf("si" to "eKqSQe5NZLDjRq2e"))
            val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
            newUrl.shouldBe(URL("https", "youtu.be", "/mYekiVRnM_A", null))
            explanation.shouldBe("Removed tracking tokens from URL")
        }

        "but not for non-YouTube URLs" {
            val originalUrl = URL("https", "example.com", "/", mapOf("si" to "eKqSQe5NZLDjRq2e"))
            val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
            newUrl.shouldBeSameInstanceAs(originalUrl)
            explanation.shouldBeNull()
        }
    }
})
