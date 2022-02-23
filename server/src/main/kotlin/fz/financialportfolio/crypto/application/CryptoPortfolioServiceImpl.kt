package fz.financialportfolio.crypto.application

import fz.financialportfolio.crypto.domain.Currency
import fz.financialportfolio.crypto.domain.Transaction
import fz.financialportfolio.kernel.trader.domain.TraderRepository
import fz.financialportfolio.kernel.trader.domain.exception.TraderNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

interface BuyCryptoUseCase {
    fun buy(traderId: Long, token: String, amount: BigDecimal, unitPrice: BigDecimal): Transaction
}

interface  SellCryptoUseCase {
    fun sell(traderId: Long, token: String, amount: BigDecimal, unitPrice: BigDecimal): Transaction
}
@Service
open class CryptoPortfolioServiceImpl(private val traderRepository: TraderRepository): BuyCryptoUseCase, SellCryptoUseCase {
    @Transactional
    override fun buy(traderId: Long, token: String, amount: BigDecimal, unitPrice: BigDecimal): Transaction {
        val coinToBuy = Currency.withUppercaseTokenAndAbsAmount(token, amount)
        val trader = traderRepository.findById(traderId)  ?: throw TraderNotFoundException()
        val portfolio = trader.portfolio

        val transaction = portfolio.buyCrypto(coinToBuy, unitPrice)

        traderRepository.save(trader)

        return transaction
    }

    @Transactional
    override fun sell(traderId: Long, token: String, amount: BigDecimal, unitPrice: BigDecimal): Transaction {
        val coinToSell = Currency.withUppercaseTokenAndAbsAmount(token, amount)
        val trader = traderRepository.findById(traderId) ?: throw TraderNotFoundException()
        val portfolio = trader.portfolio

        val transaction = portfolio.sellCrypto(coinToSell, unitPrice)

        traderRepository.save(trader)

        return transaction
    }

}