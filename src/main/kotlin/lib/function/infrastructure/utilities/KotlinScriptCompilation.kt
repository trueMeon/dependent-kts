package org.example.dependent.kts.lib.function.infrastructure.utilities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.StringScriptSource
import kotlin.script.experimental.host.createCompilationConfigurationFromTemplate
import kotlin.script.experimental.host.createEvaluationConfigurationFromTemplate
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.defaultJvmScriptingHostConfiguration
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

val scriptingHost = BasicJvmScriptingHost()

data class TypedCompiledScript(val compiledScript: ResultWithDiagnostics<CompiledScript>, var inputKClass: KClass<*>)

inline fun <reified TInput> SourceCode.compiledScript() =
    runBlocking(Dispatchers.Unconfined) {
        scriptingHost.compiler.invoke(
            this@compiledScript,
            compilationConfiguration(TInput::class))
    }.let {
        TypedCompiledScript(it, TInput::class)
    }

inline fun <reified TOutput> TypedCompiledScript.execute(vararg arguments: Any): TOutput =
    runBlocking(Dispatchers.Unconfined) {
        scriptingHost
            .evaluator(
                this@execute.compiledScript.valueOrThrow(),
                evaluationConfiguration(this@execute.inputKClass, *arguments))
            .valueOrThrow()
            .returnValue
    }.run {
        when (this) {
            is ResultValue.Error -> throw error
            is ResultValue.NotEvaluated -> error("Script was not evaluated")
            is ResultValue.Unit -> error("Script returned Unit")
            is ResultValue.Value -> value as TOutput
        }
    }

fun compilationConfiguration(kClass: KClass<*>) = createCompilationConfigurationFromTemplate(
    KotlinType(kClass),
    defaultJvmScriptingHostConfiguration,
    ScriptCompilationConfiguration::class,
) {
    jvm { dependenciesFromCurrentContext(wholeClasspath = true, unpackJarCollections = true) }
    compilerOptions.append("-jvm-target=17")
}

fun evaluationConfiguration(kClass: KClass<*>, vararg arguments: Any) = createEvaluationConfigurationFromTemplate(
    KotlinType(kClass),
    defaultJvmScriptingHostConfiguration,
    ScriptEvaluationConfiguration::class,
) {
    constructorArgs(*arguments)
}
