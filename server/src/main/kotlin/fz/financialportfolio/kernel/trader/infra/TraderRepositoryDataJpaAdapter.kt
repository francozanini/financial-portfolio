package fz.financialportfolio.kernel.trader.infra

import fz.financialportfolio.crypto.domain.CryptoPortfolio
import fz.financialportfolio.crypto.domain.Currency
import fz.financialportfolio.crypto.domain.Transaction
import fz.financialportfolio.crypto.infra.CryptoPortfolioEntity
import fz.financialportfolio.crypto.infra.CurrencyEntity
import fz.financialportfolio.crypto.infra.TransactionEntity
import fz.financialportfolio.kernel.trader.domain.TraderRepository
import fz.financialportfolio.kernel.trader.domain.Trader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
open class TraderRepositoryDataJpaAdapter @Autowired constructor(var traderJpaRepository: TraderJpaRepository) :
    TraderRepository {

    @Transactional
    override fun findById(traderId: Long): Trader? {
        return traderJpaRepository.findById(traderId).map { it.toDomain() }.orElse(null)
    }

    @Transactional
    override fun save(trader: Trader): Trader {
        return traderJpaRepository.save(trader.toEntity()).toDomain()
    }

}

private fun TraderEntity.toDomain() = Trader(this.cryptoPortfolio!!.toDomain(), this.id)

private fun CryptoPortfolioEntity.toDomain() = CryptoPortfolio(
    this.id,
    this.currencies.associateBy({it.token}, {it.toDomain()}).toMutableMap(),
    this.transactions.map { it.toDomain() }.toMutableList())

private fun TransactionEntity.toDomain(): Transaction = Transaction(this.id, this.amount, this.unitPriceWhenBought, this.type!!, this.token!!)

private fun CurrencyEntity.toDomain(): Currency = Currency(this.id, this.token, this.amount)

private fun Trader.toEntity(): TraderEntity = TraderEntity(id = this.id, cryptoPortfolioEntity = this.portfolio.toEntity()).apply { cryptoPortfolio?.trader = this }

private fun Currency.toEntity(): CurrencyEntity = CurrencyEntity(this.id, this.token, this.amount)

private fun Transaction.toEntity(): TransactionEntity = TransactionEntity(this.id, this.type, this.amount, this.unitPrice, this.token)

private fun CryptoPortfolio.toEntity(): CryptoPortfolioEntity = CryptoPortfolioEntity(
    id = this.id,
    currencies = this.balances().map(Currency::toEntity).toMutableList(),
    transactions = this.transactions().map(Transaction::toEntity).toMutableList()
)
