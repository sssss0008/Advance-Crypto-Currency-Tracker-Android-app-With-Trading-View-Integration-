package com.example.ui.tradingview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

private const val TAG = "TradingViewWidget"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TradingViewWidgetView(
    htmlContent: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    baseUrl: String = "https://www.tradingview.com",
    onFallbackNative: (() -> Unit)? = null,
    onSymbolSelected: ((String) -> Unit)? = null
) {
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var reloadKey by remember { mutableIntStateOf(0) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    LaunchedEffect(htmlContent) {
        hasError = false
        isLoading = true
    }

    Box(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxSize()
    ) {
        if (!hasError) {
            key(reloadKey) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("tradingview_webview"),
                    factory = { context ->
                        try {
                            com.example.CryptoApplication.ensureWebViewCacheDirectories(context)
                            WebView(context).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                // Force software rendering layer on WebView to avoid Mesa DRI rendernode lookups
                                // and renderer process crash (code -1) in emulator / virtualized container environments
                                setLayerType(View.LAYER_TYPE_SOFTWARE, null)

                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    databaseEnabled = true
                                    loadWithOverviewMode = true
                                    useWideViewPort = true
                                    cacheMode = WebSettings.LOAD_DEFAULT
                                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                    allowFileAccess = false
                                    allowContentAccess = false
                                    setSupportZoom(false)
                                    builtInZoomControls = false
                                    displayZoomControls = false
                                    // CRITICAL: Disable multiple windows to prevent Chromium from attempting
                                    // to create detached offscreen RenderViews, which causes renderer process crash (code -1).
                                    javaScriptCanOpenWindowsAutomatically = false
                                    setSupportMultipleWindows(false)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        safeBrowsingEnabled = false
                                    }
                                }

                                addJavascriptInterface(object {
                                    @JavascriptInterface
                                    fun onSymbolClick(urlOrSymbol: String?) {
                                        if (urlOrSymbol.isNullOrBlank()) return
                                        val uri = try { Uri.parse(urlOrSymbol) } catch (_: Throwable) { null }
                                        val sym = if (uri != null) parseTradingViewSymbol(uri) else cleanSymbolFormat(urlOrSymbol)
                                        if (!sym.isNullOrBlank() && onSymbolSelected != null) {
                                            Handler(Looper.getMainLooper()).post {
                                                onSymbolSelected.invoke(sym)
                                            }
                                        }
                                    }
                                }, "AndroidBridge")

                                webChromeClient = object : WebChromeClient() {
                                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                        if (newProgress >= 80) {
                                            isLoading = false
                                        }
                                    }

                                    override fun onConsoleMessage(consoleMessage: android.webkit.ConsoleMessage?): Boolean {
                                        Log.d(TAG, "WebView Console: [${consoleMessage?.messageLevel()}] ${consoleMessage?.message()}")
                                        return true
                                    }
                                }

                                webViewClient = object : WebViewClient() {
                                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                        isLoading = true
                                        hasError = false
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        isLoading = false
                                    }

                                    private fun processUrl(view: WebView?, urlStr: String?): Boolean {
                                        if (urlStr.isNullOrBlank()) return false
                                        val uri = try { Uri.parse(urlStr) } catch (_: Throwable) { null } ?: return false

                                        // Check if this URL contains a symbol selection
                                        val symbol = parseTradingViewSymbol(uri)
                                        if (symbol != null && onSymbolSelected != null) {
                                            view?.post {
                                                onSymbolSelected(symbol)
                                            }
                                            return true
                                        }

                                        // Allow internal TradingView embed and script resources
                                        val host = uri.host?.lowercase() ?: ""
                                        if (host.contains("tradingview.com") && (urlStr.contains("/embed") || urlStr.contains("/widget") || urlStr.contains("embed-widget") || urlStr.contains("tv.js") || urlStr.contains("static/bundles"))) {
                                            return false
                                        }

                                        // Block navigation to external website
                                        return true
                                    }

                                    override fun shouldOverrideUrlLoading(
                                        view: WebView?,
                                        request: WebResourceRequest?
                                    ): Boolean {
                                        val uri = request?.url ?: return false
                                        val url = uri.toString()

                                        // Symbol check
                                        val symbol = parseTradingViewSymbol(uri)
                                        if (symbol != null && onSymbolSelected != null) {
                                            view?.post {
                                                onSymbolSelected(symbol)
                                            }
                                            return true
                                        }

                                        // Allow subframe / iframe resource loads
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !request.isForMainFrame) {
                                            if (!request.hasGesture()) {
                                                return false
                                            }
                                        }

                                        return processUrl(view, url)
                                    }

                                    @Deprecated("Deprecated in Java")
                                    override fun shouldOverrideUrlLoading(view: WebView?, urlStr: String?): Boolean {
                                        return processUrl(view, urlStr)
                                    }

                                    override fun onReceivedError(
                                        view: WebView?,
                                        request: WebResourceRequest?,
                                        error: WebResourceError?
                                    ) {
                                        if (request?.isForMainFrame == true) {
                                            hasError = true
                                            isLoading = false
                                        }
                                    }

                                    // CRITICAL: Prevent host application crash when Chromium renderer process terminates
                                    override fun onRenderProcessGone(
                                        view: WebView?,
                                        detail: RenderProcessGoneDetail?
                                    ): Boolean {
                                        val didCrash = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            detail?.didCrash() ?: true
                                        } else {
                                            true
                                        }
                                        Log.e(TAG, "WebView render process exited (didCrash=$didCrash). Cleanly handled.")
                                        hasError = true
                                        isLoading = false

                                        view?.let { wv ->
                                            try {
                                                (wv.parent as? ViewGroup)?.removeView(wv)
                                                wv.destroy()
                                            } catch (e: Throwable) {
                                                Log.w(TAG, "Error destroying crashed WebView", e)
                                            }
                                        }
                                        webViewRef = null
                                        if (onFallbackNative != null) {
                                            Handler(Looper.getMainLooper()).post {
                                                try {
                                                    onFallbackNative.invoke()
                                                } catch (t: Throwable) {
                                                    Log.w(TAG, "Error invoking fallback native", t)
                                                }
                                            }
                                        }
                                        return true
                                    }
                                }

                                tag = htmlContent
                                loadDataWithBaseURL(
                                    baseUrl,
                                    htmlContent,
                                    "text/html",
                                    "UTF-8",
                                    null
                                )
                                webViewRef = this
                            }
                        } catch (e: Throwable) {
                            Log.e(TAG, "Fatal error creating WebView", e)
                            hasError = true
                            View(context)
                        }
                    },
                    update = { webView ->
                        if (webView is WebView) {
                            webViewRef = webView
                            try {
                                if (webView.tag != htmlContent) {
                                    webView.tag = htmlContent
                                    hasError = false
                                    isLoading = true
                                    webView.loadDataWithBaseURL(
                                        baseUrl,
                                        htmlContent,
                                        "text/html",
                                        "UTF-8",
                                        null
                                    )
                                }
                            } catch (e: Throwable) {
                                Log.w(TAG, "Error updating WebView content", e)
                                hasError = true
                            }
                        }
                    }
                )
            }
        }

        // Loading Overlay
        AnimatedVisibility(
            visible = isLoading && !hasError,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("loading_spinner"),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Loading TradingView Widget...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Error State with Retry & Native Fallback
        if (hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = "Connection Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Unable to load TradingView widget",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "A network timeout or web renderer issue occurred. You can reload or switch to the native interactive view.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                hasError = false
                                isLoading = true
                                reloadKey++
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reload")
                        }

                        if (onFallbackNative != null) {
                            OutlinedButton(
                                onClick = onFallbackNative
                            ) {
                                Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = "Native")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Use Native View")
                            }
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(reloadKey) {
        onDispose {
            try {
                webViewRef?.let { wv ->
                    wv.stopLoading()
                    wv.onPause()
                    (wv.parent as? ViewGroup)?.removeView(wv)
                    wv.destroy()
                }
            } catch (e: Throwable) {
                Log.w(TAG, "Error disposing WebView", e)
            }
            webViewRef = null
        }
    }
}

internal fun cleanSymbolFormat(rawInput: String): String {
    var raw = rawInput.trim().uppercase()
    if (raw.endsWith("/")) {
        raw = raw.dropLast(1)
    }

    if (raw.contains(":")) {
        val parts = raw.split(":")
        if (parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
            val ex = parts[0]
            val rest = parts[1]
            return if (rest.startsWith("$ex:")) rest else "$ex:$rest"
        }
        return raw
    }

    if (raw.contains("-")) {
        val parts = raw.split("-")
        if (parts.size >= 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
            return "${parts[0]}:${parts[1]}"
        }
    }

    val knownCoins = listOf(
        "BTC" to "BITSTAMP:BTCUSD",
        "ETH" to "BITSTAMP:ETHUSD",
        "SOL" to "BINANCE:SOLUSDT",
        "BNB" to "BINANCE:BNBUSDT",
        "XRP" to "BINANCE:XRPUSDT",
        "USDT.D" to "CRYPTOCAP:USDT.D",
        "DOGE" to "BINANCE:DOGEUSDT",
        "AVAX" to "BINANCE:AVAXUSDT",
        "ADA" to "BINANCE:ADAUSDT",
        "NEAR" to "BINANCE:NEARUSDT",
        "LINK" to "BINANCE:LINKUSDT",
        "SUI" to "BINANCE:SUIUSDT",
        "APT" to "BINANCE:APTUSDT",
        "DOT" to "BINANCE:DOTUSDT",
        "ZEC" to "BINANCE:ZECUSDT",
        "PEPE" to "BINANCE:PEPEUSDT",
        "SHIB" to "BINANCE:SHIBUSDT",
        "RENDER" to "BINANCE:RENDERUSDT"
    )

    for ((sym, tv) in knownCoins) {
        if (raw == sym || raw == "${sym}USD" || raw == "${sym}USDT" || (raw.length > 2 && raw.contains(sym))) {
            return tv
        }
    }

    return if (raw.endsWith("USDT") || raw.endsWith("BUSD")) {
        "BINANCE:$raw"
    } else if (raw.endsWith("USD")) {
        "BITSTAMP:$raw"
    } else {
        "BITSTAMP:${raw}USD"
    }
}

internal fun parseTradingViewSymbol(uri: android.net.Uri): String? {
    try {
        val querySymbol = uri.getQueryParameter("symbol")?.trim()
        if (!querySymbol.isNullOrBlank()) {
            return cleanSymbolFormat(querySymbol)
        }

        val tvWidgetSymbol = uri.getQueryParameter("tvwidgetsymbol")?.trim()
        if (!tvWidgetSymbol.isNullOrBlank()) {
            return cleanSymbolFormat(tvWidgetSymbol)
        }

        val pathSegments = uri.pathSegments ?: emptyList()
        val symbolsIndex = pathSegments.indexOf("symbols")
        if (symbolsIndex != -1 && symbolsIndex + 1 < pathSegments.size) {
            val raw = pathSegments[symbolsIndex + 1].trim()
            return cleanSymbolFormat(raw)
        }

        val chartIndex = pathSegments.indexOf("chart")
        if (chartIndex != -1 && chartIndex + 1 < pathSegments.size) {
            val candidate = pathSegments[chartIndex + 1].trim()
            if (candidate.length in 2..20 && !candidate.equals("embed", ignoreCase = true)) {
                return cleanSymbolFormat(candidate)
            }
        }

        val exchange = uri.getQueryParameter("exchange")?.trim()?.uppercase()
        if (!exchange.isNullOrBlank()) {
            val coin = uri.getQueryParameter("coin") ?: uri.getQueryParameter("symbol")
            if (!coin.isNullOrBlank()) {
                return "$exchange:${coin.trim().uppercase()}"
            }
        }
    } catch (e: Throwable) {
        Log.w("TradingViewWidgetView", "Failed to parse symbol from URI: $uri", e)
    }
    return null
}
