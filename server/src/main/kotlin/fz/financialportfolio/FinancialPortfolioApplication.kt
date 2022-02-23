package fz.financialportfolio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class FinancialPortfolioApplication

fun main(args: Array<String>) {
	runApplication<FinancialPortfolioApplication>(*args)
}