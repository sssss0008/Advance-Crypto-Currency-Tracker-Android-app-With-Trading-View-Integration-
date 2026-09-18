package com.example

import android.app.Application
import android.content.Context
import android.util.Log
import java.io.File

class CryptoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        cleanCorruptedWebViewCache(this)
    }

    companion object {
        private const val TAG = "CryptoApplication"

        /**
         * Cleans up any corrupted SimpleCache directory in WebView cache.
         * Previous versions created subdirectories like "Code Cache/wasm" directly inside
         * "HTTP Cache", which violated Chromium's SimpleCache disk format and caused
         * "Could not reconstruct index from disk" and "Failed to write a new fake index" errors.
         */
        fun cleanCorruptedWebViewCache(context: Context) {
            try {
                val httpCache = File(context.cacheDir, "WebView/Default/HTTP Cache")
                if (httpCache.exists()) {
                    val badSubdir = File(httpCache, "Code Cache")
                    if (badSubdir.exists()) {
                        Log.i(TAG, "Removing corrupted SimpleCache directory containing invalid subdirectories")
                        httpCache.deleteRecursively()
                    }
                }
            } catch (e: Throwable) {
                Log.w(TAG, "Failed cleaning corrupted WebView cache", e)
            }
        }
    }
}

