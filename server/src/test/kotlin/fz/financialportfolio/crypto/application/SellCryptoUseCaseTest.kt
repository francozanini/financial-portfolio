package fz.financialportfolio.crypto.application

import fz.financialportfolio.crypto.domain.Currency
import fz.financialportfolio.crypto.domain.TransactionType
import fz.financialportfolio.crypto.domain.exception.BalanceIsTooSmallException
import fz.financialportfolio.kernel.trader.domain.Trader
import fz.financialportfolio.kernel.trader.domain.exception.TraderNotFoundException
import fz.financialportfolio.kernel.trader.infra.InMemoryTraderRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class SellCryptoUseCaseTest {
    @Test
    fun `when user sells crypto that they don't own it should throw an exception`() {
        val traderRepository = InMemoryTraderRepository()
        val trader = traderRepository.save(Trader())
        val cryptoPortfolioService = CryptoPortfolioServiceImpl(traderRepository)

        assertThatThrownBy {
            cryptoPortfolioService.sell(trader.id!!, token = "btc", BigDecimal.TEN, BigDecimal.ONE)
        }.isInstanceOf(BalanceIsTooSmallException::class.java)

    }

    @Test
    fun `when user sells crypto that they own it should save transaction and reflect movement in account balance`() {
        val traderRepository = InMemoryTraderRepository()
        val cryptoPortfolioService = CryptoPortfolioServiceImpl(traderRepository)

        val trader = Trader()
        trader.portfolio.buyCrypto(Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.TEN), BigDecimal.ONE)
        val traderId = traderRepository.save(trader).id!!

        cryptoPortfolioService.sell(traderId, "btc", BigDecimal.TEN, BigDecimal.TEN)

        val updatedPortfolio = traderRepository.findById(traderId)?.portfolio!!

        assertThat(updatedPortfolio.amountForCurrency("btc"))
            .isEqualTo(BigDecimal.ZERO)

        assertThat(updatedPortfolio.transactions())
            .hasSize(2)

        assertThat(updatedPortfolio.transactions())
            .filteredOn{ it.type == TransactionType.SELL }
            .hasSize(1)
    }

    @Test
    fun `when trader doesn't exists it throws an exception`() {
        val traderRepository = InMemoryTraderRepository()
        val cryptoPortfolioService = CryptoPortfolioServiceImpl(traderRepository)

        assertThatThrownBy { cryptoPortfolioService.sell(1L, "BTC", BigDecimal.TEN, BigDecimal.ONE) }
            .isInstanceOf(TraderNotFoundException::class.java)
    }
}