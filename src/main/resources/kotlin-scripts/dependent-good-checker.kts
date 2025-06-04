
import org.example.dependent.kts.domain.Good
import org.example.dependent.kts.lib.function.infrastructure.KotlinScriptInput
import org.example.dependent.kts.lib.function.infrastructure.function
import org.example.dependent.kts.services.Lawyer
import org.example.dependent.kts.services.Warehouse

val self = this as KotlinScriptInput

data class Dependency(val warehouse: Warehouse, val lawyer: Lawyer)

self.factory.function<Good, Dependency, Boolean> { good, deps ->
    val goodInStock = deps.warehouse.isGoodInStock(good)
    println("$good is in stock: $goodInStock")

    val goodIsLegal = deps.lawyer.isGoodLegal(good)
    println("$good is legal: $goodIsLegal")

    goodInStock && goodIsLegal
}
