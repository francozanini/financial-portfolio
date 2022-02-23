package fz.financialportfolio.crypto.domain

import java.math.BigDecimal

data class Transaction(var id: Long?, val amount: BigDecimal, val unitPrice: BigDecimal, val type: TransactionType, val token: String) {
    constructor(amount: BigDecimal, unitPrice: BigDecimal, type: TransactionType, token: String): this(null, amount, unitPrice, type, token)
    constructor(currency: Currency, unitPrice: BigDecimal, type: TransactionType): this(null, currency.amount, unitPrice, type, currency.token)
}

enum class TransactionType {
    BUY,
    SELL
}
