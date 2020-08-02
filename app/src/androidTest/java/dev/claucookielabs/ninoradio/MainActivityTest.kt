package dev.claucookielabs.ninoradio

import android.app.Activity
import androidx.test.rule.ActivityTestRule
import com.karumi.shot.ScreenshotTest
import org.junit.Rule
import org.junit.Test


class MyActivityTest: ScreenshotTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun theActivityIsShownProperly() {
        val activity : Activity = mActivityTestRule.launchActivity(null)
        /*
          * Take the actual screenshot. At the end of this call, the screenshot
          * is stored on the device and the gradle plugin takes care of
          * pulling it and displaying it to you in nice ways.
          */
        compareScreenshot(activity)
    }
}