
import org.example.dependent.kts.domain.Good
import org.example.dependent.kts.lib.function.infrastructure.KotlinScriptInput
import org.example.dependent.kts.lib.function.infrastructure.function
import org.example.dependent.kts.services.SellingJournal
import java.time.Clock

val self = this as KotlinScriptInput

data class Dependency(val sellingJournal: SellingJournal, val clock: Clock)

self.factory.function<Good, Dependency, Unit> { good, deps ->
    deps.sellingJournal.logGood(good)
}