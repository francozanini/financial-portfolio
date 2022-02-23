package fz.financialportfolio.crypto.domain

import java.math.BigDecimal

data class Currency(var id: Long?, val token: String, val amount: BigDecimal) {

    constructor(token: String, amount: BigDecimal): this(null, token, amount)

    operator fun plus(other: Currency): Currency {
        return Currency(this.token, this.amount + other.amount)
    }

    operator fun minus(other: Currency): Currency {
        return Currency(this.token, this.amount - other.amount)
    }

    init {
        if (token.isEmpty() || token.uppercase() != token) throw IllegalStateException("Token should be non-empty uppercase")
        if (amount < BigDecimal.ZERO) throw IllegalStateException("Amount can't be signed")
    }
    companion object Factory {
        fun withUppercaseTokenAndAbsAmount(token: String, amount: BigDecimal): Currency {
            return Currency(token.uppercase(), amount.abs())
        }
    }

}
