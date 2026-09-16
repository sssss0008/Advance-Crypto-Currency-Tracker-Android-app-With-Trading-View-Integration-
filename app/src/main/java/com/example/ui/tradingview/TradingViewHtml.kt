package com.example.ui.tradingview

object TradingViewHtml {

    private fun wrapHtml(content: String, isDark: Boolean): String {
        val bgColor = if (isDark) "#090D16" else "#FFFFFF"
        val textColor = if (isDark) "#F1F5F9" else "#0F172A"
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <style>
                    * {
                        box-sizing: border-box;
                        margin: 0;
                        padding: 0;
                        -webkit-tap-highlight-color: transparent;
                    }
                    html, body {
                        width: 100%;
                        height: 100%;
                        background-color: $bgColor;
                        color: $textColor;
                        overflow-x: hidden;
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                    }
                    .tradingview-widget-container {
                        width: 100% !important;
                        height: 100% !important;
                    }
                    .tradingview-widget-container__widget {
                        width: 100% !important;
                        height: 100% !important;
                    }
                    .tradingview-widget-container__widget iframe {
                        width: 100% !important;
                        height: 100% !important;
                    }
                    .tradingview-widget-copyright {
                        display: none !important;
                    }
                    tv-ticker-tape, tv-market-overview {
                        width: 100% !important;
                        display: block;
                    }
                </style>
                <script>
                    (function() {
                        function isSymbolUrl(url) {
                            if (!url || typeof url !== 'string') return false;
                            return url.indexOf('/symbols/') !== -1 ||
                                   url.indexOf('symbol=') !== -1 ||
                                   url.indexOf('tvwidgetsymbol=') !== -1 ||
                                   url.indexOf('wealthorbitcenter.com') !== -1 ||
                                   url.indexOf('/markets/cryptocurrencies/') !== -1;
                        }

                        window.open = function(url) {
                            try {
                                if (url && window.AndroidBridge && window.AndroidBridge.onSymbolClick) {
                                    if (isSymbolUrl(url)) {
                                        window.AndroidBridge.onSymbolClick(url);
                                    }
                                }
                            } catch(e) {}
                            return null;
                        };

                        document.addEventListener('click', function(e) {
                            try {
                                var a = e.target.closest ? e.target.closest('a') : null;
                                if (a && a.href && isSymbolUrl(a.href) && window.AndroidBridge && window.AndroidBridge.onSymbolClick) {
                                    e.preventDefault();
                                    e.stopPropagation();
                                    window.AndroidBridge.onSymbolClick(a.href);
                                }
                            } catch(e) {}
                        }, true);
                    })();
                </script>
            </head>
            <body>
                $content
            </body>
            </html>
        """.trimIndent()
    }

    fun getTickerTapeHtml(
        symbols: String = "BITSTAMP:BTCUSD,BITSTAMP:ETHUSD,BINANCE:SOLUSDT,BINANCE:XRPUSDT,CRYPTOCAP:USDT.D,BINANCE:ZECUSDT,BINANCE:DOGEUSDT,BINANCE:NEARUSDT,BINANCE:BNBUSDT,BINANCE:AVAXUSDT",
        isDark: Boolean = true
    ): String {
        val theme = if (isDark) "dark" else "light"
        val content = """
            <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-ticker-tape.js"></script>
            <tv-ticker-tape symbols="$symbols" show-hover theme="$theme"></tv-ticker-tape>
        """.trimIndent()
        return wrapHtml(content, isDark)
    }

    fun getMarketOverviewHtml(isDark: Boolean = true): String {
        val theme = if (isDark) "dark" else "light"
        val content = """
            <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-market-overview.js"></script>
            <tv-market-overview mode="market-movers" assets-type="crypto" theme="$theme"></tv-market-overview>
        """.trimIndent()
        return wrapHtml(content, isDark)
    }

    fun getHeatmapHtml(isDark: Boolean = true): String {
        val theme = if (isDark) "dark" else "light"
        val content = """
            <!-- TradingView Widget BEGIN -->
            <div class="tradingview-widget-container">
              <div class="tradingview-widget-container__widget"></div>
              <div class="tradingview-widget-copyright"><a href="https://www.tradingview.com/heatmap/crypto/" rel="noopener nofollow" target="_blank"><span class="blue-text">Crypto Heatmap</span></a><span class="trademark"> by TradingView</span></div>
              <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-crypto-coins-heatmap.js" async>
              {
                "dataSource": "Crypto",
                "blockSize": "market_cap_calc",
                "blockColor": "24h_close_change|5",
                "locale": "en",
                "symbolUrl": "https://www.tradingview.com/symbols/{proName}/",
                "colorTheme": "$theme",
                "hasTopBar": true,
                "isDataSetEnabled": true,
                "isZoomEnabled": true,
                "hasSymbolTooltip": true,
                "isMonoSize": false,
                "width": "100%",
                "height": "100%"
              }
              </script>
            </div>
            <!-- TradingView Widget END -->
        """.trimIndent()
        return wrapHtml(content, isDark)
    }

    fun getScreenerHtml(isDark: Boolean = true): String {
        val theme = if (isDark) "dark" else "light"
        val content = """
            <!-- TradingView Widget BEGIN -->
            <div class="tradingview-widget-container">
              <div class="tradingview-widget-container__widget"></div>
              <div class="tradingview-widget-copyright"><a href="https://www.tradingview.com/markets/cryptocurrencies/prices-all/" rel="noopener nofollow" target="_blank"><span class="blue-text">Crypto markets</span></a><span class="trademark"> by TradingView</span></div>
              <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-screener.js" async>
              {
                "defaultColumn": "overview",
                "screener_type": "crypto_mkt",
                "displayCurrency": "USD",
                "colorTheme": "$theme",
                "isTransparent": false,
                "locale": "en",
                "largeChartUrl": "https://www.tradingview.com/symbols/{proName}/",
                "width": "100%",
                "height": "100%"
              }
              </script>
            </div>
            <!-- TradingView Widget END -->
        """.trimIndent()
        return wrapHtml(content, isDark)
    }

    fun getAdvancedScreenerHtml(isDark: Boolean = true): String {
        val theme = if (isDark) "dark" else "light"
        val content = """
            <!-- TradingView Widget BEGIN -->
            <div class="tradingview-widget-container" style="width:100%;height:100%">
              <div class="tradingview-widget-container__widget" style="width:100%;height:100%"></div>
              <div class="tradingview-widget-copyright"><a href="https://www.tradingview.com/crypto-coins-screener/" rel="noopener nofollow" target="_blank"><span class="blue-text">Crypto Screener</span></a><span class="trademark"> by TradingView</span></div>
              <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-screener.js" async>
              {
                "market": "crypto",
                "showToolbar": true,
                "defaultColumn": "overview",
                "defaultScreen": "general",
                "isTransparent": false,
                "locale": "en",
                "colorTheme": "$theme",
                "largeChartUrl": "https://wealthorbitcenter.com/free-live-trading-real-time-chart-stocks-forex-crypto/?tvwidgetsymbol={proName}&symbol={proName}",
                "width": "100%",
                "height": "100%"
              }
              </script>
            </div>
            <!-- TradingView Widget END -->
        """.trimIndent()
        return wrapHtml(content, isDark)
    }

    fun getAdvancedChartHtml(
        symbol: String = "BITSTAMP:BTCUSD",
        isDark: Boolean = true
    ): String {
        val theme = if (isDark) "dark" else "light"
        val bgColor = if (isDark) "#090D16" else "#ffffff"
        val gridColor = if (isDark) "rgba(240, 243, 250, 0.06)" else "rgba(46, 46, 46, 0.2)"
        val content = """
            <!-- TradingView Widget BEGIN -->
            <div class="tradingview-widget-container" style="height:100%;width:100%">
              <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
              <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-advanced-chart.js" async>
              {
                "allow_symbol_change": true,
                "calendar": false,
                "details": false,
                "hide_side_toolbar": false,
                "hide_top_toolbar": false,
                "hide_legend": false,
                "hide_volume": false,
                "hotlist": false,
                "interval": "D",
                "locale": "en",
                "save_image": true,
                "style": "1",
                "symbol": "$symbol",
                "theme": "$theme",
                "timezone": "Etc/UTC",
                "backgroundColor": "$bgColor",
                "gridColor": "$gridColor",
                "watchlist": [],
                "withdateranges": true,
                "range": "YTD",
                "compareSymbols": [],
                "support_host": "https://www.tradingview.com",
                "studies": [],
                "autosize": true
              }
              </script>
            </div>
            <!-- TradingView Widget END -->
        """.trimIndent()
        return wrapHtml(content, isDark)
    }

    fun getSymbolChartHtml(symbol: String, isDark: Boolean = true): String {
        val theme = if (isDark) "dark" else "light"
        val content = """
            <div class="tradingview-widget-container" style="height:100%;width:100%">
              <div id="tradingview_chart" style="height:100%;width:100%"></div>
              <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
              <script type="text/javascript">
              new TradingView.widget({
                "autosize": true,
                "symbol": "$symbol",
                "interval": "D",
                "timezone": "Etc/UTC",
                "theme": "$theme",
                "style": "1",
                "locale": "en",
                "toolbar_bg": "${if (isDark) "#090D16" else "#FFFFFF"}",
                "enable_publishing": false,
                "hide_top_toolbar": false,
                "allow_symbol_change": true,
                "save_image": false,
                "container_id": "tradingview_chart"
              });
              </script>
            </div>
        """.trimIndent()
        return wrapHtml(content, isDark)
    }
}
