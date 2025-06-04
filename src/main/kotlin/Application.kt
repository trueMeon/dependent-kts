package org.example.dependent.kts

import org.example.dependent.kts.domain.Good
import org.example.dependent.kts.lib.function.applicaiton.DependentFunctionsCollection
import org.example.dependent.kts.lib.function.applicaiton.execute
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class DemoApplication {
    @Bean
    fun commandLineRunner(collection: DependentFunctionsCollection): CommandLineRunner {
        return CommandLineRunner { _: Array<String?>? ->
            val loadedFunctions = listOf(
                "pure-price-modifier",
                "dependent-good-checker",
                "dependent-good-delivery"
            ).associateWith { collection.function(it) }

            println("Dependencies checking")
            loadedFunctions.forEach { name, function ->
                println("Dependencies of $name function is fulfilled: ${function.isDependencyFulfilled}")
            }

            println()

            val originalPrice = 30
            println("Original price: $originalPrice")

            val modifiedPrice = loadedFunctions["pure-price-modifier"]!!
                .execute<Int, Int>(originalPrice)
            println("Modified price: $modifiedPrice")

            val good = Good("Phone", modifiedPrice)

            val goodIsOkay = loadedFunctions["dependent-good-checker"]!!
                .execute<Good, Boolean>(good)
            println("$good is okay: $goodIsOkay")

            try {
                loadedFunctions["dependent-good-delivery"]!!
                    .execute<Good, Unit>(good)
            } catch(e: Exception) {
                println("Delivery hasn't been executed, since its dependency wasn't fulfilled")
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
