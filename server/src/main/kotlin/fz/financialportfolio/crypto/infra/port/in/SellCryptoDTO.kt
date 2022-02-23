package fz.financialportfolio.crypto.infra.port.`in`

import java.math.BigDecimal

data class SellCryptoDTO(val traderId: Long, val token: String, val amount: BigDecimal, val unitPrice: BigDecimal)
