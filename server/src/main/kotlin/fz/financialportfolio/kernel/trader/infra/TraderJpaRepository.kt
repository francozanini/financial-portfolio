package fz.financialportfolio.kernel.trader.infra

import fz.financialportfolio.kernel.trader.infra.TraderEntity
import org.springframework.data.repository.CrudRepository

interface TraderJpaRepository: CrudRepository<TraderEntity, Long> {
}