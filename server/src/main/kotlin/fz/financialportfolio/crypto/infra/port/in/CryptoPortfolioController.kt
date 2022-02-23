package fz.financialportfolio.crypto.infra.port.`in`

import fz.financialportfolio.crypto.application.BuyCryptoUseCase
import fz.financialportfolio.crypto.application.SellCryptoUseCase
import fz.financialportfolio.crypto.domain.exception.BalanceIsTooSmallException
import fz.financialportfolio.kernel.trader.domain.exception.TraderNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/crypto"])
class CryptoPortfolioController(val buyCryptoUseCase: BuyCryptoUseCase, val sellCryptoUseCase: SellCryptoUseCase) {

    @PostMapping
    fun buyCrypto(@RequestBody buyCryptoDTO: BuyCryptoDTO): ResponseEntity<Void> {
        buyCryptoUseCase.buy(
            token = buyCryptoDTO.token,
            amount = buyCryptoDTO.amount,
            unitPrice = buyCryptoDTO.unitPrice,
            traderId = buyCryptoDTO.traderId
        )

        return ResponseEntity.ok().build()
    }

    @PutMapping
    fun sellCrypto(@RequestBody sellCryptoDTO: SellCryptoDTO): ResponseEntity<Void> {
        sellCryptoUseCase.sell(
            token = sellCryptoDTO.token,
            amount = sellCryptoDTO.amount,
            unitPrice = sellCryptoDTO.unitPrice,
            traderId = sellCryptoDTO.traderId
        )

        return ResponseEntity.ok().build()
    }

    @ExceptionHandler(TraderNotFoundException::class)
    fun handleException(ex: TraderNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.message)
    }

    @ExceptionHandler(BalanceIsTooSmallException::class)
    fun handleException(ex: BalanceIsTooSmallException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

}