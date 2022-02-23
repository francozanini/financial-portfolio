package fz.financialportfolio.domain


import fz.financialportfolio.crypto.domain.CryptoPortfolio
import fz.financialportfolio.crypto.domain.Currency
import fz.financialportfolio.crypto.domain.TransactionType
import fz.financialportfolio.crypto.domain.exception.BalanceIsTooSmallException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal


class CryptoPortfolioTest {
    @Test
    fun `when adding to empty portfolio new balance should be same as new currency and transaction should be registered`() {
        val currency =  Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.TEN)
        val portfolio = CryptoPortfolio()

        val unitPrice = BigDecimal.ONE
        portfolio.buyCrypto(currency, unitPrice)

        assertThat(portfolio.tokensBought()).isNotEmpty
        assertThat(portfolio.balances()).containsExactly(currency)
        assertThat(portfolio.amountForCurrency(currency.token)).isEqualTo(currency.amount)

        assertThat(portfolio.transactions()).isNotEmpty
        val transaction = portfolio.transactions()[0]
        assertThat(transaction.type).isEqualTo(TransactionType.BUY)
        assertThat(transaction.amount).isEqualTo(currency.amount)
        assertThat(transaction.unitPrice).isEqualTo(unitPrice)
    }

    @Test
    fun `when currency already in portfolio it adds to the amount of already existing currency`() {
        val portfolio = CryptoPortfolio()

        val firstPurchase = Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.TEN)
        portfolio.buyCrypto(firstPurchase, BigDecimal.ONE)

        val secondPurchase = Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.ONE)
        portfolio.buyCrypto(secondPurchase, BigDecimal.ONE)

        assertThat(portfolio.amountForCurrency(firstPurchase.token)).isEqualTo(firstPurchase.amount + secondPurchase.amount)
    }

    @Test
    fun `when checking for token amount it returns zero amount for non existing token`() {
        val portfolio = CryptoPortfolio()

        assertThat(portfolio.amountForCurrency("btc")).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `when buying with signed unit price it throws an exception`() {
        val portfolio = CryptoPortfolio()

        assertThatThrownBy { portfolio.buyCrypto(
            Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.ONE),
            BigDecimal.valueOf(-25L)
        ) }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `when selling more than the available money it should throw an exception`() {
        val portfolio = CryptoPortfolio()
        assertThatThrownBy { portfolio.sellCrypto(Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.ONE), BigDecimal.TEN) }
            .isInstanceOf(BalanceIsTooSmallException::class.java)
    }

    @Test
    fun `when selling less than the available money it should substract that amount from portfolio`() {
        // given
        val portfolio = CryptoPortfolio()
        val cryptoBought = Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.TEN)
        portfolio.buyCrypto(cryptoBought, BigDecimal.ONE)

        // when
        val cryptoSold = Currency.withUppercaseTokenAndAbsAmount("btc", BigDecimal.ONE)

        // then
        val unitPrice = BigDecimal.valueOf(25L)
        val transaction = portfolio.sellCrypto(cryptoSold, unitPrice)

        assertThat(portfolio.amountForCurrency(cryptoBought.token)).isEqualTo((cryptoBought - cryptoSold).amount)
        assertThat(transaction.type).isEqualTo(TransactionType.SELL)
        assertThat(transaction.amount).isEqualTo(cryptoSold.amount)
        assertThat(transaction.unitPrice).isEqualTo(unitPrice)
        assertThat(portfolio.transactions()).contains(transaction)
    }
}