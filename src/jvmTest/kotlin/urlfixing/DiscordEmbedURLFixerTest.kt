package garden.ephemeral.clipninja.urlfixing

import garden.ephemeral.clipninja.util.URL
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs

class DiscordEmbedURLFixerTest : FreeSpec({
    "does nothing for URLs which don't match" {
        val originalUrl = URL("https", "example.com", "/", null)
        val (newUrl, explanation) = DiscordEmbedURLFixer.fix(originalUrl)
        newUrl.shouldBeSameInstanceAs(originalUrl)
        explanation.shouldBeNull()
    }

    "replaces domain for Twitter URLs" {
        val originalUrl = URL("https", "twitter.com", "/Ikol_Tweets/status/1820803622348693752", null)
        val (newUrl, explanation) = DiscordEmbedURLFixer.fix(originalUrl)
        newUrl.shouldBe(URL("https", "vxtwitter.com", "/Ikol_Tweets/status/1820803622348693752", null))
        explanation.shouldBe("Replaced URL domain to fix embeds")
    }
})
