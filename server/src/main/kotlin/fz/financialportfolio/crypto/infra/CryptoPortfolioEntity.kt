package fz.financialportfolio.crypto.infra

import fz.financialportfolio.kernel.trader.infra.TraderEntity
import javax.persistence.*

@Entity
@Table(name = "cryptoPortfolio")
class CryptoPortfolioEntity(
    id: Long?,
    currencies: MutableList<CurrencyEntity>,
    transactions: MutableList<TransactionEntity>) {

    constructor(): this(null, arrayListOf(), arrayListOf())

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @OneToOne(mappedBy = "cryptoPortfolio")
    var trader: TraderEntity? = null

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, mappedBy = "portfolio")
    var currencies: MutableList<CurrencyEntity> = ArrayList()

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, mappedBy = "portfolio")
    var transactions: MutableList<TransactionEntity> = ArrayList()

    init {
        this.id = id
        this.currencies = currencies
        this.transactions = transactions
    }
}