package fz.financialportfolio.kernel.trader.domain

import fz.financialportfolio.kernel.trader.domain.Trader

interface TraderRepository {
    fun findById(traderId: Long): Trader?
    fun save(trader: Trader): Trader
}
