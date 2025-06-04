import org.example.dependent.kts.lib.function.infrastructure.KotlinScriptInput
import org.example.dependent.kts.lib.function.infrastructure.function

val self = this as KotlinScriptInput

self.factory.function<Int, Int> { originalPrice ->
    originalPrice * 10
}