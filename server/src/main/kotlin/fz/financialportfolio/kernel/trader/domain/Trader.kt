package fz.financialportfolio.kernel.trader.domain

import fz.financialportfolio.crypto.domain.CryptoPortfolio

data class Trader(val portfolio: CryptoPortfolio, var id: Long?) {

    constructor(): this(CryptoPortfolio(), null)
}


