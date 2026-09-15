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
            setupGraphicsEnvironment()
        }

        fun setupGraphicsEnvironment() {
            try {
                // Configure Mesa driver environment to prevent searching for non-existent DRM render nodes in containerized emulator
                Os.setenv("LIBGL_DRI3_DISABLE", "1", true)
                Os.setenv("LIBGL_KMS_DRI3_DISABLE", "1", true)
                Os.setenv("MESA_LOADER_DRIVER_OVERRIDE", "swrast", true)
                Os.setenv("GALLIUM_DRIVER", "llvmpipe", true)
                Os.setenv("LIBGL_ALWAYS_SOFTWARE", "1", true)
                Os.setenv("MESA_DEBUG", "silent", true)
                Os.setenv("MESA_LOG_FILE", "/dev/null", true)
                Os.setenv("EGL_LOG_LEVEL", "fatal", true)
                Os.setenv("LIBGL_DRIVERS_PATH", "/dev/null", true)
            } catch (e: Throwable) {
                Log.w(TAG, "Failed setting MESA environment variables", e)
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
        setupGraphicsEnvironment()
        ensureWebViewCacheDirectories(this)
    }
}
