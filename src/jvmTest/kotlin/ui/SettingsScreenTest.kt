package garden.ephemeral.clipninja.ui

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onSibling
import androidx.compose.ui.test.performClick
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {
    // XXX: JUnit 4 :(
    @get:Rule
    val compose = createComposeRule()

    private lateinit var settings: Settings

    private lateinit var showAllTextLabel: SemanticsNodeInteraction
    private lateinit var showAllTextSwitch: SemanticsNodeInteraction
    private lateinit var reallyShowAllTextLabel: SemanticsNodeInteraction
    private lateinit var reallyShowAllTextSwitch: SemanticsNodeInteraction
    private lateinit var enableDiscordEmbedLabel: SemanticsNodeInteraction
    private lateinit var enableDiscordEmbedSwitch: SemanticsNodeInteraction
    private lateinit var enableStripTrackingLabel: SemanticsNodeInteraction
    private lateinit var enableStripTrackingSwitch: SemanticsNodeInteraction

    private fun runTest(testBody: suspend TestScope.() -> Unit) {
        val scope = TestScope(StandardTestDispatcher())
        scope.runTest(testBody = testBody)
    }

    @Before
    fun setUp() {
        runTest {
            settings = Settings()
            compose.setContent {
                SettingsScreen(settings)
            }
            compose.awaitIdle()

            showAllTextLabel = compose.onNodeWithText("Show all text", substring = true)
            showAllTextSwitch = showAllTextLabel.onSibling()
            reallyShowAllTextLabel = compose.onNodeWithText("Really show all text", substring = true)
            reallyShowAllTextSwitch = reallyShowAllTextLabel.onSibling()
            enableDiscordEmbedLabel = compose.onNodeWithText("Make Discord embeds work", substring = true)
            enableDiscordEmbedSwitch = enableDiscordEmbedLabel.onSibling()
            enableStripTrackingLabel = compose.onNodeWithText("Strip tracking parameters", substring = true)
            enableStripTrackingSwitch = enableStripTrackingLabel.onSibling()

            // Initial state checks
            showAllTextSwitch.assertIsOff()
            settings.showAllText.value.shouldBe(false)
            reallyShowAllTextLabel.assertDoesNotExist()
            settings.reallyShowAllText.value.shouldBe(false)
            enableDiscordEmbedSwitch.assertIsOn()
            settings.enableDiscordEmbed.value.shouldBe(true)
            enableStripTrackingSwitch.assertIsOn()
            settings.enableStripTracking.value.shouldBe(true)
        }
    }

    @Test
    fun `clicking switch for show all text toggles the setting`() {
        runTest {
            showAllTextSwitch.performClick()

            showAllTextSwitch.assertIsOn()
            settings.showAllText.value.shouldBe(true)
        }
    }

    @Test
    fun `clicking label for show all text toggles the setting`() {
        runTest {
            showAllTextLabel.performClick()

            showAllTextSwitch.assertIsOn()
            settings.showAllText.value.shouldBe(true)
        }
    }

    @Test
    fun `clicking switch for show all text toggles the visibility of really show all text`() {
        runTest {
            showAllTextSwitch.performClick()

            reallyShowAllTextLabel.assertIsDisplayed()
        }
    }

    @Test
    fun `clicking switch for really show all text toggles the setting`() {
        runTest {
            showAllTextSwitch.performClick()
            reallyShowAllTextSwitch.performClick()

            reallyShowAllTextSwitch.assertIsOn()
            settings.reallyShowAllText.value.shouldBe(true)
        }
    }

    @Test
    fun `clicking label for really show all text toggles the setting`() {
        runTest {
            showAllTextLabel.performClick()
            reallyShowAllTextLabel.performClick()

            reallyShowAllTextSwitch.assertIsOn()
            settings.reallyShowAllText.value.shouldBe(true)
        }
    }

    @Test
    fun `clicking switch for make embeds work toggles the setting`() {
        runTest {
            enableDiscordEmbedSwitch.performClick()

            enableDiscordEmbedSwitch.assertIsOff()
            settings.enableDiscordEmbed.value.shouldBe(false)
        }
    }

    @Test
    fun `clicking label for make embeds work toggles the setting`() {
        runTest {
            enableDiscordEmbedLabel.performClick()

            enableDiscordEmbedSwitch.assertIsOff()
            settings.enableDiscordEmbed.value.shouldBe(false)
        }
    }

    @Test
    fun `clicking switch for strip tracking toggles the setting`() {
        runTest {
            enableStripTrackingSwitch.performClick()

            enableStripTrackingSwitch.assertIsOff()
            settings.enableStripTracking.value.shouldBe(false)
        }
    }

    @Test
    fun `clicking label for strip tracking toggles the setting`() {
        runTest {
            enableStripTrackingLabel.performClick()

            enableStripTrackingSwitch.assertIsOff()
            settings.enableStripTracking.value.shouldBe(false)
        }
    }
}
