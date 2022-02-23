package fz.financialportfolio.kernel.trader.infra

import fz.financialportfolio.crypto.infra.CryptoPortfolioEntity
import javax.persistence.*

@Table(name = "trader")
@Entity
class TraderEntity(id: Long?, cryptoPortfolioEntity: CryptoPortfolioEntity) {

    constructor(): this(null, CryptoPortfolioEntity())

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "crypto_portfolio_id", referencedColumnName = "id")
    var cryptoPortfolio: CryptoPortfolioEntity? = null

    init {
        this.id = id
        this.cryptoPortfolio = cryptoPortfolioEntity
    }

}