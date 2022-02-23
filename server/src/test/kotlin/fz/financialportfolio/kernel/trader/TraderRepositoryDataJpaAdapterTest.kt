package fz.financialportfolio.kernel.trader

import fz.financialportfolio.crypto.domain.Currency
import fz.financialportfolio.crypto.domain.TransactionType
import fz.financialportfolio.kernel.trader.domain.Trader
import fz.financialportfolio.kernel.trader.domain.TraderRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.math.BigDecimal


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
open class TraderRepositoryDataJpaAdapterTest {

    @Autowired lateinit var traderRepository: TraderRepository

    @Test
    fun `it should save trader with empty portfolio properly`() {
        val savedTrader = traderRepository.save(Trader())

        assertThat(savedTrader).isNotNull
        assertThat(savedTrader.id).isNotNull
        assertThat(savedTrader.portfolio.transactions()).isEmpty()
        assertThat(savedTrader.portfolio.tokensBought()).isEmpty()
        assertThat(savedTrader.portfolio.balances()).isEmpty()
    }

    @Test
    fun `it should save trader with non empty portfolio properly`() {
        val trader = Trader()
        val token = "btc"
        val amount = BigDecimal.TEN
        trader.portfolio.buyCrypto(Currency.withUppercaseTokenAndAbsAmount(token, amount), BigDecimal.ONE)
        trader.portfolio.buyCrypto(Currency.withUppercaseTokenAndAbsAmount(token, amount), BigDecimal.ONE)

        val savedTrader = traderRepository.save(trader)

        assertThat(savedTrader).isNotNull

        val transactions = savedTrader.portfolio.transactions()
        assertThat(transactions).hasSize(2)
        assertThat(transactions).allMatch { it.type == TransactionType.BUY }

        assertThat(savedTrader.portfolio.tokensBought()).hasSize(1)
        assertThat(savedTrader.portfolio.amountForCurrency(token)).isEqualTo(amount.times(BigDecimal.valueOf(2L)))
    }

    @Test
    fun `when looking for non existing trader it should return null`() {
        assertThat(traderRepository.findById(1L)).isNull()
    }

    @Test
    fun `when looking for existing trader it should return it`() {
        val traderId = traderRepository.save(Trader()).id!!

        assertThat(traderRepository.findById(traderId)).isNotNull
    }


}