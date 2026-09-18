package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Crypto Screener", appName)
  }

  @Test
  fun `cleanCorruptedWebViewCache removes invalid subdirectories`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val badDir = java.io.File(context.cacheDir, "WebView/Default/HTTP Cache/Code Cache")
    badDir.mkdirs()
    assertTrue(badDir.exists())

    CryptoApplication.cleanCorruptedWebViewCache(context)
    assertFalse(badDir.exists())
  }
}
