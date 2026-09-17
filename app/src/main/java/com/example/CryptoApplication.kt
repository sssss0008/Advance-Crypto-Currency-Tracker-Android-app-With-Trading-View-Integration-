package com.example

import android.app.Application
import android.content.Context
import android.system.Os
import android.util.Log
import java.io.File

class CryptoApplication : Application() {

    init {
        applyGraphicsEnvironment()
    }

    override fun attachBaseContext(base: Context?) {
        applyGraphicsEnvironment()
        super.attachBaseContext(base)
    }

    companion object {
        private const val TAG = "CryptoApplication"

        init {
            applyGraphicsEnvironment()
        }

        fun applyGraphicsEnvironment() {
            try {
                // Prevent Mesa from attempting to probe non-existent DRM render nodes (/dev/dri/renderD128)
                // in virtualized Android containers and silence driver logging.
                Os.setenv("MESA_LOG_FILE", "/dev/null", true)
                Os.setenv("MESA_DEBUG", "silent", true)
                Os.setenv("MESA_LOG_LEVEL", "none", true)
                Os.setenv("LIBGL_ALWAYS_SOFTWARE", "1", true)
                Os.setenv("GALLIUM_DRIVER", "softpipe", true)
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
