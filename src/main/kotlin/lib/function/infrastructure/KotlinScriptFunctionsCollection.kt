package org.example.dependent.kts.lib.function.infrastructure

import org.example.dependent.kts.lib.function.applicaiton.DependentFunction
import org.example.dependent.kts.lib.function.applicaiton.DependentFunctionsCollection
import org.example.dependent.kts.lib.function.infrastructure.utilities.compiledScript
import org.example.dependent.kts.lib.function.infrastructure.utilities.execute
import org.springframework.stereotype.Service
import java.nio.file.Path
import kotlin.io.path.pathString
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.host.toScriptSource

@KotlinScript
abstract class KotlinScriptInput(val factory: FunctionFactory)

@Service
class KotlinScriptFunctionsCollection(private val functionFactory: FunctionFactory) : DependentFunctionsCollection {
    override fun function(name: String) =
        scriptContent(name)
            .toScriptSource(name)
            .compiledScript<KotlinScriptInput>()
            .execute<DependentFunction>(functionFactory)


    private fun scriptContent(scriptName: String) =
        Path.of("/kotlin-scripts", "$scriptName.kts").pathString.let {
            this::class.java.getResource(it)?.readText()
                ?: throw Exception("Kotlin script with name \"$scriptName\" doesn't exist")
        }
}