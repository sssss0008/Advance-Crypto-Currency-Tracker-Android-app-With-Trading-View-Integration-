package com.example.data

data class LessonModule(
    val id: String,
    val title: String,
    val category: String,
    val durationMin: Int,
    val level: String, // Beginner, Intermediate, Advanced
    val summary: String,
    val sections: List<LessonSection>,
    val keyTakeaways: List<String>
)

data class LessonSection(
    val heading: String,
    val content: String,
    val tip: String? = null
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val category: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

object CryptoEducationData {

    val modules: List<LessonModule> = listOf(
        LessonModule(
            id = "lesson_blockchain_101",
            title = "Blockchain & Crypto Fundamentals",
            category = "Foundations",
            durationMin = 8,
            level = "Beginner",
            summary = "Understand how decentralized ledgers, cryptographic hashing, and consensus algorithms work together to eliminate third-party intermediaries.",
            sections = listOf(
                LessonSection(
                    heading = "1. What is a Blockchain?",
                    content = "A blockchain is a decentralized, distributed, and append-only digital ledger. Instead of a single bank or government maintaining balances on a private database, an open global network of independent nodes maintains and synchronizes identical copies of the transaction ledger simultaneously.",
                    tip = "No single entity can reverse or forge transactions once confirmed by consensus."
                ),
                LessonSection(
                    heading = "2. Cryptographic Hashes & Chaining",
                    content = "Each block contains a cryptographic hash of all transactions inside it, plus the cryptographic hash of the preceding block. If an attacker attempts to alter any transaction in block 50, its hash changes, breaking the mathematical links for block 51, 52, and every block thereafter.",
                    tip = "SHA-256 takes any input data and generates an unforgeable, fixed 256-bit digital fingerprint."
                ),
                LessonSection(
                    heading = "3. Public & Private Keys",
                    content = "Your public key (or derived wallet address) is like your bank account IBAN number: anyone can see it and send funds to it. Your private key is like your secret digital signature: it gives mathematical authority to sign transactions and transfer coins.",
                    tip = "Remember the golden crypto rule: 'Not your keys, not your coins!'"
                )
            ),
            keyTakeaways = listOf(
                "Blockchains replace trust in centralized institutions with cryptographic proof.",
                "Immutable transaction chaining makes historical alteration computationally infeasible.",
                "Private keys must always be guarded offline; whoever holds the key controls the assets."
            )
        ),
        LessonModule(
            id = "lesson_bitcoin_macro",
            title = "Bitcoin & The Digital Gold Standard",
            category = "Macro & Store of Value",
            durationMin = 10,
            level = "Beginner",
            summary = "Explore Bitcoin's economic design, absolute scarcity, mining incentives, and why it is treated as global digital sound money.",
            sections = listOf(
                LessonSection(
                    heading = "1. Absolute Mathematical Scarcity",
                    content = "Unlike fiat currencies (which can be printed indefinitely by central banks causing inflation), Bitcoin's total supply is mathematically capped at exactly 21 million BTC. This hard supply cap is enforced by all running node software worldwide.",
                    tip = "Over 19.7 million Bitcoins have already been mined; the final satoshi will be minted around the year 2140."
                ),
                LessonSection(
                    heading = "2. The 4-Year Halving Cycle",
                    content = "Every 210,000 blocks (roughly every 4 years), the number of newly minted Bitcoins awarded to miners is slashed by 50%. This predictable programmatic supply reduction creates structural supply shocks.",
                    tip = "Halvings dropped rewards from 50 BTC (2009) to 25, 12.5, 6.25, and now 3.125 BTC per block."
                ),
                LessonSection(
                    heading = "3. Proof of Work & Security",
                    content = "Miners dedicate vast electrical energy and computational hardware (ASICs) to solve SHA-256 cryptographic puzzles. This links digital consensus directly to the physical laws of thermodynamics.",
                    tip = "To reverse a Bitcoin transaction after 6 confirmations would require more energy than entire industrialized nations."
                )
            ),
            keyTakeaways = listOf(
                "Bitcoin is the first engineered monetary asset with absolute mathematical scarcity.",
                "The 4-year halving cycle halves new issuance, reinforcing its disinflationary monetary policy.",
                "Proof of Work anchors network security into real-world physical energy expenditure."
            )
        ),
        LessonModule(
            id = "lesson_ethereum_smart_contracts",
            title = "Smart Contracts & The EVM",
            category = "Programmable Money",
            durationMin = 12,
            level = "Intermediate",
            summary = "Learn how Ethereum revolutionized blockchain from simple value transfers into a global, Turing-complete decentralized world computer.",
            sections = listOf(
                LessonSection(
                    heading = "1. What is a Smart Contract?",
                    content = "A smart contract is a self-executing software program stored on a blockchain that deterministically executes predefined actions when specified conditions are met. No lawyers, escrows, or banks are needed.",
                    tip = "Code is Law: Once deployed, immutable smart contracts execute exactly as written without human bias."
                ),
                LessonSection(
                    heading = "2. The Ethereum Virtual Machine (EVM)",
                    content = "The EVM is a decentralized computing engine that computes state transitions across millions of nodes worldwide. Developers write smart contracts in languages like Solidity or Vyper, which are compiled into EVM bytecode.",
                    tip = "EVM compatibility has become the dominant industry standard across Polygon, Avalanche, and BNB Chain."
                ),
                LessonSection(
                    heading = "3. Gas Mechanics & EIP-1559",
                    content = "Every computation on Ethereum requires 'gas' paid in ETH. EIP-1559 introduced a dynamic base fee that is permanently burned (destroyed) with every transaction, causing ETH supply to become deflationary during high activity.",
                    tip = "Layer 2 rollups bundle thousands of transactions to compress gas costs by over 95%."
                )
            ),
            keyTakeaways = listOf(
                "Smart contracts automate financial agreements with zero intermediary trust.",
                "The EVM provides a universal runtime for decentralized applications (dApps).",
                "Transaction fees (gas) prevent network denial-of-service and incentivize computational honesty."
            )
        ),
        LessonModule(
            id = "lesson_defi_mechanics",
            title = "DeFi: AMMs, Lending & Yield",
            category = "Decentralized Finance",
            durationMin = 14,
            level = "Intermediate",
            summary = "Master decentralized exchanges, automated market makers (x * y = k), over-collateralized borrowing, and yield generation.",
            sections = listOf(
                LessonSection(
                    heading = "1. Automated Market Makers (AMMs)",
                    content = "Traditional exchanges match buyers and sellers on an order book. In DeFi, AMMs like Uniswap replace order books with liquidity pools governed by mathematical formulas such as x * y = k, allowing trades at any second against pooled liquidity.",
                    tip = "Anyone can become a market maker by depositing equal values of two tokens into a pool."
                ),
                LessonSection(
                    heading = "2. Over-Collateralized Lending",
                    content = "Protocols like Aave and Compound enable instant loans without credit checks. To borrow \$1,000 in USDC, a user might deposit \$1,500 worth of ETH as collateral. If ETH drops near the liquidation threshold, the smart contract liquidates the collateral to protect lenders.",
                    tip = "Always monitor your Health Factor: keep it comfortably above 1.5 to prevent liquidation."
                ),
                LessonSection(
                    heading = "3. Understanding Impermanent Loss",
                    content = "When you provide liquidity to an AMM and the price ratio between the two assets changes dramatically, your pool share is worth less than if you had simply held the individual tokens in your wallet. High trading fees compensate for this risk.",
                    tip = "Use our in-app Impermanent Loss Calculator to model price divergence scenarios!"
                )
            ),
            keyTakeaways = listOf(
                "AMMs allow continuous 24/7 peer-to-pool liquidity without centralized market makers.",
                "DeFi lending requires over-collateralization to protect solvency without credit scores.",
                "Impermanent loss occurs during asset price divergence in liquidity pools."
            )
        ),
        LessonModule(
            id = "lesson_technical_analysis",
            title = "Candlesticks & Technical Analysis",
            category = "Trading & TA",
            durationMin = 12,
            level = "Intermediate",
            summary = "Decode candlestick psychology, support and resistance zones, trendlines, RSI momentum, and high-probability chart patterns.",
            sections = listOf(
                LessonSection(
                    heading = "1. Anatomy of a Candlestick",
                    content = "A candlestick shows the Open, High, Low, and Close (OHLC) over a selected time interval. The wide body shows the distance between open and close, while thin upper and lower wicks (shadows) reveal price extremes rejected by the market.",
                    tip = "Long lower wicks at major support indicate aggressive buyer absorption."
                ),
                LessonSection(
                    heading = "2. Support & Resistance & Liquidity Pools",
                    content = "Support is a price level where buying interest is strong enough to overcome selling pressure. Resistance is where selling overcomes buying. Once resistance is broken on high volume, it often flips to become new support.",
                    tip = "Market makers frequently sweep swing highs and lows to trigger retail stop losses before true trend moves."
                ),
                LessonSection(
                    heading = "3. RSI & MACD Momentum",
                    content = "The Relative Strength Index (RSI) identifies overbought (>70) and oversold (<30) conditions. Bullish divergence occurs when price makes a lower low but RSI makes a higher low, warning of waning selling momentum.",
                    tip = "Never rely on a single indicator: look for confluence between price action, volume, and momentum."
                )
            ),
            keyTakeaways = listOf(
                "Candlestick wicks reveal rejected prices and liquidity absorption by institutional participants.",
                "Support and resistance levels are zones of interest, not precise rigid lines.",
                "Indicator divergence provides early alerts for impending trend exhaustion."
            )
        ),
        LessonModule(
            id = "lesson_risk_management",
            title = "Risk Management & Trading Psychology",
            category = "Trading Mastery",
            durationMin = 10,
            level = "Advanced",
            summary = "The #1 factor separating profitable traders from liquidated traders: position sizing, the 1% risk rule, risk-to-reward ratios, and emotional control.",
            sections = listOf(
                LessonSection(
                    heading = "1. The 1% Golden Rule",
                    content = "Professional traders never risk more than 1% to 2% of their total account equity on any single trade. If your account is \$10,000, your maximum dollar loss on a stopped-out trade should never exceed \$100 to \$200.",
                    tip = "Surviving drawdown streaks is the foundation of compound wealth in volatile crypto markets."
                ),
                LessonSection(
                    heading = "2. Risk/Reward Asymmetry (1:2.5+)",
                    content = "Only enter setups where your target profit is at least 2.5 times your risk distance. With a 1:3 risk/reward ratio, you only need to win 35% of your trades to be consistently profitable over time!",
                    tip = "Set your stop-loss before entering the trade, and never move your stop further away when losing."
                ),
                LessonSection(
                    heading = "3. Conquering FOMO & Revenge Trading",
                    content = "Fear Of Missing Out (FOMO) causes retail traders to buy tops. Revenge trading—jumping immediately into another reckless trade after taking a loss—is the fastest route to account blowup.",
                    tip = "The market is open 24/7/365: missing an entry is always better than losing your hard-earned capital."
                )
            ),
            keyTakeaways = listOf(
                "Cap risk at 1% of total portfolio capital per individual trade.",
                "Maintain positive risk-to-reward ratios (1:2 or higher) to achieve long-term edge.",
                "Master emotional composure: accept losses as a standard business expense."
            )
        )
    )

    val practiceQuiz: List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "q1",
            question = "What is the absolute maximum number of Bitcoins that will ever exist?",
            category = "Bitcoin",
            options = listOf("100 Million", "21 Million", "18.5 Million", "Infinite, adjusts with inflation"),
            correctIndex = 1,
            explanation = "Bitcoin's source code hardcodes a strict maximum supply limit of exactly 21,000,000 BTC, enforced across all network consensus nodes."
        ),
        QuizQuestion(
            id = "q2",
            question = "What happens to Bitcoin's block reward during a Halving event?",
            category = "Bitcoin",
            options = listOf(
                "It increases by 50% to reward miners",
                "It is completely eliminated, leaving only fees",
                "It is cut in half every 210,000 blocks (~4 years)",
                "It doubles the transaction fees automatically"
            ),
            correctIndex = 2,
            explanation = "Bitcoin halving cuts the new block subsidy in half every 210,000 blocks (roughly every 4 years), reducing supply issuance."
        ),
        QuizQuestion(
            id = "q3",
            question = "Which automated formula governs standard AMM liquidity pools like Uniswap v2?",
            category = "DeFi",
            options = listOf(
                "a² + b² = c²",
                "x * y = k",
                "E = mc²",
                "Price = Volume / MarketCap"
            ),
            correctIndex = 1,
            explanation = "The Constant Product formula x * y = k ensures that the product of the quantities of two pooled tokens remains constant after any trade."
        ),
        QuizQuestion(
            id = "q4",
            question = "What is 'Impermanent Loss' in decentralized finance?",
            category = "DeFi",
            options = listOf(
                "Losing tokens permanently due to a smart contract hack",
                "A temporary loss of potential value compared to holding tokens outside the pool when prices diverge",
                "Paying high gas fees on Ethereum during peak network hours",
                "Exchange trading fees deducted on market orders"
            ),
            correctIndex = 1,
            explanation = "Impermanent loss occurs when token prices in an AMM pool diverge from their deposit ratio, resulting in less value than simply HODLing both assets."
        ),
        QuizQuestion(
            id = "q5",
            question = "What is a 51% attack on a Proof of Work blockchain?",
            category = "Security",
            options = listOf(
                "A discount offering 51% off transaction gas fees",
                "When a malicious entity controls over 50% of the network hashrate, enabling double-spends and block reorganizations",
                "When 51 validators fail to submit attestations on time",
                "A phishing attack that steals 51% of a user's wallet balance"
            ),
            correctIndex = 1,
            explanation = "Controlling >50% of mining power allows an attacker to rewrite recent blocks, execute double-spends, and block confirmations."
        ),
        QuizQuestion(
            id = "q6",
            question = "What does a Relative Strength Index (RSI) reading below 30 typically indicate?",
            category = "Trading & TA",
            options = listOf(
                "Overbought conditions, high likelihood of immediate crash",
                "Oversold conditions, where selling momentum may be exhausted",
                "Zero volume traded over the last 24 hours",
                "That the asset must be delisted from exchanges"
            ),
            correctIndex = 1,
            explanation = "An RSI below 30 is traditionally considered oversold, alerting traders that sellers may have exhausted their momentum."
        ),
        QuizQuestion(
            id = "q7",
            question = "What is the primary difference between a Hot Wallet and a Cold Wallet?",
            category = "Security",
            options = listOf(
                "Hot wallets trade only Bitcoin; cold wallets trade altcoins",
                "Hot wallets are connected to the internet; cold wallets store private keys completely offline",
                "Cold wallets require government KYC identity verification",
                "Hot wallets charge monthly subscription fees"
            ),
            correctIndex = 1,
            explanation = "Hot wallets (browser extensions, apps) are online and exposed to remote vulnerabilities; cold wallets (hardware devices) keep private keys isolated offline."
        ),
        QuizQuestion(
            id = "q8",
            question = "What does Ethereum's EIP-1559 upgrade do to base transaction fees?",
            category = "Ethereum",
            options = listOf(
                "Transfers them directly to the Ethereum Foundation",
                "Permanently burns (destroys) the base fee portion, reducing ETH supply",
                "Refunds 100% of fees to the sender after 24 hours",
                "Donates all fees to open-source developers"
            ),
            correctIndex = 1,
            explanation = "EIP-1559 burns the base gas fee in every transaction, transforming Ethereum into a disinflationary or deflationary asset during high activity."
        ),
        QuizQuestion(
            id = "q9",
            question = "If a trader opens a 10x leveraged position on Bitcoin, what percentage drop leads to liquidation?",
            category = "Trading & TA",
            options = listOf(
                "A 50% price drop",
                "Approximately a 10% price drop (minus maintenance margin)",
                "A 100% price drop",
                "A 1% price drop"
            ),
            correctIndex = 1,
            explanation = "At 10x leverage, a 10% adverse price move equals 100% loss of initial margin, triggering liquidation before equity drops below zero."
        ),
        QuizQuestion(
            id = "q10",
            question = "What is a 'Zero-Knowledge Proof' (ZKP)?",
            category = "Cryptography",
            options = listOf(
                "Proving you know nothing about trading to avoid capital gains taxes",
                "A cryptographic method allowing one party to prove a statement is true without revealing any underlying sensitive details",
                "A blockchain that deletes transaction history every 30 days",
                "A consensus algorithm that requires zero electricity"
            ),
            correctIndex = 1,
            explanation = "ZKP enables mathematical verification of correctness without exposing underlying secret data, powering privacy and ZK-rollups."
        )
    )
}
