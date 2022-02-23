package fz.financialportfolio.crypto.infra

import fz.financialportfolio.crypto.domain.TransactionType
import java.math.BigDecimal
import javax.persistence.*

@Entity
class TransactionEntity(
    id: Long?,
    type: TransactionType?,
    amountBought: BigDecimal,
    unitPriceWhenBought: BigDecimal,
    token: String?) {

    constructor(): this(null, null, BigDecimal.ZERO, BigDecimal.ZERO, null)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "crypto_portfolio_id")
    lateinit var portfolio: CryptoPortfolioEntity

    @Enumerated(EnumType.STRING)
    var type: TransactionType?

    var amount: BigDecimal

    var unitPriceWhenBought: BigDecimal

    var token: String?

    init {
        this.id = id
        this.type = type
        this.unitPriceWhenBought = unitPriceWhenBought
        this.amount = amountBought
        this.token = token
    }
}
