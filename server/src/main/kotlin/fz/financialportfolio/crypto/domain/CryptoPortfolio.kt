package fz.financialportfolio.crypto.domain

import fz.financialportfolio.crypto.domain.exception.BalanceIsTooSmallException
import java.math.BigDecimal

class CryptoPortfolio(val id: Long?, private val currencies: MutableMap<String, Currency>, private val transactionHistory: MutableList<Transaction>) {

    constructor(): this(null, mutableMapOf(), mutableListOf())

    fun buyCrypto(currency: Currency, unitPrice: BigDecimal): Transaction {
        if (unitPrice < BigDecimal.ZERO) throw IllegalStateException("Unit price cannot be signed")

        val token = currency.token.uppercase()

        if(currencies.contains(token)) {
            currencies[token] = currencies[token]!! + currency
        } else {
            currencies[token] = currency
        }

        val transaction = Transaction(currency.amount, unitPrice.abs(), TransactionType.BUY, currency.token)
        transactionHistory.add(transaction)
        return transaction
    }

    fun amountForCurrency(token: String): BigDecimal {
        return currencies[token.uppercase()]?.amount ?: BigDecimal.ZERO
    }

    fun tokensBought(): Set<String> {
        return currencies.keys.toHashSet()
    }

    fun balances(): List<Currency> {
        return currencies.values.toList()
    }

    fun transactions(): List<Transaction> {
        return transactionHistory.toList()
    }

    fun sellCrypto(toSell: Currency, unitPrice: BigDecimal): Transaction {
        val token = toSell.token.uppercase()

        if (amountForCurrency(token) < toSell.amount) {
            throw BalanceIsTooSmallException()
        }

        currencies[token] = currencies[token]!! - toSell

        val transaction =  Transaction(toSell, unitPrice, TransactionType.SELL)
        transactionHistory.add(transaction)
        return transaction
    }


}
