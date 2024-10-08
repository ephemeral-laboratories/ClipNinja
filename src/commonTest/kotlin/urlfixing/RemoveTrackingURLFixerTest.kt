package garden.ephemeral.clipninja.urlfixing

import garden.ephemeral.clipninja.util.URL
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

    "removes utm_ parameters" - {
        "for all URLs" {
            val originalUrl = URL("https", "example.com", "/", mapOf("utm_blah" to "blah", "foo" to "bar"))
            val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
            newUrl.shouldBe(URL("https", "example.com", "/", mapOf("foo" to "bar")))
            explanation.shouldBe("Removed tracking tokens from URL")
        }

        "and removes the param map if it becomes empty" {
            val originalUrl = URL("https", "example.com", "/", mapOf("utm_blah" to "blah"))
            val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
            newUrl.shouldBe(URL("https", "example.com", "/", null))
            explanation.shouldBe("Removed tracking tokens from URL")
        }
    }

    // Since the list of tracking parameters is currently data-driven, testing just one site should suffice.
    // If we end up wanting to thoroughly test all of them, we'll want to make the test data-driven as well.
    "removes si parameter" - {
        "for YouTube URLs" {
            val originalUrl = URL(
                "https", "youtube.com", "/mYekiVRnM_A",
                mapOf("si" to "eKqSQe5NZLDjRq2e", "foo" to "bar")
            )
            val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
            newUrl.shouldBe(URL("https", "youtube.com", "/mYekiVRnM_A", mapOf("foo" to "bar")))
            explanation.shouldBe("Removed tracking tokens from URL")
        }

        "for shortened YouTube URLs" {
            val originalUrl = URL(
                "https", "youtu.be", "/mYekiVRnM_A",
                mapOf("si" to "eKqSQe5NZLDjRq2e", "foo" to "bar")
            )
            val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
            newUrl.shouldBe(URL("https", "youtu.be", "/mYekiVRnM_A", mapOf("foo" to "bar")))
            explanation.shouldBe("Removed tracking tokens from URL")
        }

        "and removes the param map if it becomes empty" {
            val originalUrl = URL("https", "youtu.be", "/mYekiVRnM_A", mapOf("si" to "eKqSQe5NZLDjRq2e"))
            val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
            newUrl.shouldBe(URL("https", "youtu.be", "/mYekiVRnM_A", null))
            explanation.shouldBe("Removed tracking tokens from URL")
        }

        "but does not modify non-YouTube URLs" {
            val originalUrl = URL("https", "example.com", "/", mapOf("si" to "eKqSQe5NZLDjRq2e"))
            val (newUrl, explanation) = RemoveTrackingURLFixer.fix(originalUrl)
            newUrl.shouldBeSameInstanceAs(originalUrl)
            explanation.shouldBeNull()
        }
    }
})
