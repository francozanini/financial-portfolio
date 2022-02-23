package fz.financialportfolio.crypto.application

import fz.financialportfolio.kernel.trader.domain.Trader
import fz.financialportfolio.kernel.trader.infra.InMemoryTraderRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class BuyCryptoUseCaseTest {
    @Test
    fun `when buying a new coin it should add it to the portfolio and register it in transaction history`() {
        val traderRepository = InMemoryTraderRepository()
        val trader = traderRepository.save(Trader())
        val cryptoPortfolioService = CryptoPortfolioServiceImpl(traderRepository)

        val tokenBought = "XYZ"
        val amountBought = BigDecimal.valueOf(0.1)

        cryptoPortfolioService.buy(
            traderId = trader.id!!,
            token = tokenBought,
            amount = amountBought,
            unitPrice = BigDecimal.TEN)

        val updatedTrader = traderRepository.findById(trader.id!!)!!
        val portfolio = updatedTrader.portfolio

        assertThat(portfolio.tokensBought().contains(tokenBought))
        assertThat(portfolio.amountForCurrency(tokenBought)).isEqualTo(amountBought)
        assertThat(portfolio.transactions()).isNotEmpty
        assertThat(portfolio.transactions()).hasSize(1)
        assertThat(portfolio.transactions()).allMatch { transaction -> transaction.amount == amountBought && transaction.token == tokenBought }
    }
}