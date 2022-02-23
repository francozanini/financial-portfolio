package fz.financialportfolio.crypto.infra

import java.math.BigDecimal
import javax.persistence.*

@Entity
class CurrencyEntity(id: Long?, token: String, amount: BigDecimal) {

    constructor(): this(null, "", BigDecimal.ZERO)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var token: String

    var amount: BigDecimal

    @ManyToOne
    var portfolio: CryptoPortfolioEntity? = null

    init {
        this.id = id
        this.token = token
        this.amount = amount
    }
}
