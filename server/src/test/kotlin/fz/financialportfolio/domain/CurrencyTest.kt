package fz.financialportfolio.domain

import fz.financialportfolio.crypto.domain.Currency
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class CurrencyTest {
    @Test
    fun `it adds two currencies of the same token`() {
        val firstCurrency = Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.TEN)
        val secondCurrency = Currency.withUppercaseTokenAndAbsAmount(firstCurrency.token, BigDecimal.ONE)

        assertThat((firstCurrency + secondCurrency).amount).isEqualTo(firstCurrency.amount.plus(secondCurrency.amount))
        assertThat((firstCurrency + secondCurrency).token).isEqualTo(firstCurrency.token)
    }

    @Test
    fun `it subtracts two currencies of the same token`() {
        val firstCurrency = Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.TEN)
        val secondCurrency = Currency.withUppercaseTokenAndAbsAmount(firstCurrency.token, BigDecimal.ONE)

        assertThat((firstCurrency - secondCurrency).amount).isEqualTo(firstCurrency.amount.minus(secondCurrency.amount))
        assertThat((firstCurrency - secondCurrency).token).isEqualTo(firstCurrency.token)
    }

    @Test
    fun `it throws an exception when creating negative amount currency`() {
        assertThatThrownBy { Currency("BTC", BigDecimal.valueOf(-25L)) }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `it only considers token and amount for equality`() {
        val a = Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.ONE)
        val b = Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.ONE)

        assertThat(a == b).isTrue
    }
}