package fz.financialportfolio.kernel.trader.infra

import fz.financialportfolio.kernel.trader.domain.Trader
import fz.financialportfolio.kernel.trader.domain.TraderRepository
import java.util.concurrent.atomic.AtomicLong

class InMemoryTraderRepository: TraderRepository {
    private val traders: MutableMap<Long, Trader> = mutableMapOf()
    private val sequence = AtomicLong(1)

    override fun findById(traderId: Long): Trader? {
        return traders[traderId]
    }

    override fun save(trader: Trader): Trader {
        if (trader.id == null) {
            trader.id = sequence.getAndIncrement()
        }

        traders[trader.id!!] = trader
        return trader
    }

}
