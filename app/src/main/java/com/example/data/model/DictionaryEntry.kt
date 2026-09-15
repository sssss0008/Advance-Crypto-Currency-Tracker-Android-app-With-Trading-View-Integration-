package com.example.data.model

data class DictionaryEntry(
    val id: String,
    val term: String,
    val category: String,
    val definition: String,
    val example: String,
    val relatedTerms: List<String> = emptyList()
)

object CryptoDictionaryCategories {
    const val ALL = "All"
    const val DEFI = "DeFi"
    const val TRADING = "Trading & TA"
    const val SECURITY = "Security & Crypto"
    const val CONSENSUS = "Consensus & L1/L2"
    const val TOKENOMICS = "Tokenomics"
    const val WEB3_DAO = "Web3 & DAOs"
    const val REGULATION = "Regulation & Legal"
    const val SLANG = "Slang & Culture"
    const val NFTS = "NFTs & Gaming"

    val ALL_CATEGORIES = listOf(
        ALL,
        DEFI,
        TRADING,
        SECURITY,
        CONSENSUS,
        TOKENOMICS,
        WEB3_DAO,
        REGULATION,
        SLANG,
        NFTS
    )
}
