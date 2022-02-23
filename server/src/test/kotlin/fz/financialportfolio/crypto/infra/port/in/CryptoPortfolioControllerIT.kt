package fz.financialportfolio.crypto.infra.port.`in`


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import fz.financialportfolio.crypto.domain.Currency
import fz.financialportfolio.kernel.trader.domain.Trader
import fz.financialportfolio.kernel.trader.domain.TraderRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
internal class CryptoPortfolioControllerIT(@Autowired val mockMvc: MockMvc, @Autowired val traderRepository: TraderRepository) {
    val ow: ObjectWriter = ObjectMapper().writer()

    @Test
    fun `when buying crypto for non existing trader it returns 422 UNPROCESSABLE ENTITY code`() {
        val payload = BuyCryptoDTO(
            amount = BigDecimal.TEN,
            unitPrice = BigDecimal.ONE,
            traderId = 1L,
            token = "BTC"
        )

        this.mockMvc
            .perform(
                post("/crypto")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(ow.writeValueAsString(payload)))
            .andDo(print())
            .andExpect(status().`is`(422))
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun `when buying crypto for existing trader it returns 200 OK code`() {
        val trader = traderRepository.save(Trader())

        val payload = BuyCryptoDTO(
            amount = BigDecimal.TEN,
            unitPrice = BigDecimal.ONE,
            traderId = trader.id!!,
            token = "BTC"
        )

        this.mockMvc
            .perform(
                post("/crypto")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(ow.writeValueAsString(payload)))
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    fun `when selling crypto for non existing trader it returns 422 UNPROCESSABLE ENTITY code`() {
        val payload = BuyCryptoDTO(
            amount = BigDecimal.TEN,
            unitPrice = BigDecimal.ONE,
            traderId = 1L,
            token = "BTC"
        )

        this.mockMvc
            .perform(
                put("/crypto")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(ow.writeValueAsString(payload)))
            .andDo(print())
            .andExpect(status().`is`(422))
    }

    @Test
    fun `when selling crypto for account without balance it returns 400 BAD REQUEST code`() {
        val trader = Trader()

        trader.portfolio.buyCrypto(Currency("BTC", BigDecimal.TEN), BigDecimal.ONE)

        val traderId = traderRepository.save(trader).id!!

        val payload = SellCryptoDTO(
            amount = BigDecimal.TEN,
            unitPrice = BigDecimal.ONE,
            traderId = traderId,
            token = "BTC"
        )

        this.mockMvc
            .perform(
                put("/crypto")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(ow.writeValueAsString(payload)))
            .andDo(print())
            .andExpect(status().`is`(400))
    }

}