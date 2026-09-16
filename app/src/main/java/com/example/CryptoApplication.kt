package com.example

import android.app.Application
import android.content.Context
import android.system.Os
import android.util.Log
import java.io.File

class CryptoApplication : Application() {

    companion object {
        private const val TAG = "CryptoApplication"

        init {
            try {
                // Force Mesa to use software rendering path directly, avoiding failed attempts
                // to open non-existent DRM render nodes (/dev/dri/renderD128) in virtualized containers.
                Os.setenv("LIBGL_ALWAYS_SOFTWARE", "1", true)
            } catch (e: Throwable) {
                Log.w(TAG, "Failed setting graphics environment", e)
            }
        }

        fun ensureWebViewCacheDirectories(context: Context) {
            try {
                val baseCache = context.cacheDir
                val dirs = listOf(
                    File(baseCache, "WebView"),
                    File(baseCache, "WebView/Default"),
                    File(baseCache, "WebView/Default/HTTP Cache"),
                    File(baseCache, "WebView/Default/HTTP Cache/Code Cache"),
                    File(baseCache, "WebView/Default/HTTP Cache/Code Cache/js"),
                    File(baseCache, "WebView/Default/HTTP Cache/Code Cache/wasm"),
                    File(baseCache, "WebView/Default/Code Cache"),
                    File(baseCache, "WebView/Default/Code Cache/js"),
                    File(baseCache, "WebView/Default/Code Cache/wasm"),
                    File(baseCache, "WebView/Default/Cache")
                )
                for (dir in dirs) {
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                }
            } catch (e: Throwable) {
                Log.w(TAG, "Failed ensuring WebView cache directories", e)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        ensureWebViewCacheDirectories(this)
    }
}
