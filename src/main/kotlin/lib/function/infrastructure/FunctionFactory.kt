package org.example.dependent.kts.lib.function.infrastructure

import org.example.dependent.kts.lib.function.applicaiton.DependentFunction
import org.example.dependent.kts.lib.function.infrastructure.base.InstanceProvider
import org.springframework.stereotype.Service
import kotlin.reflect.KClass
import kotlin.reflect.cast

@Service
class FunctionFactory(private val instanceProvider: InstanceProvider) {
    fun <TIn : Any, TOut : Any> function(
        inputClass: KClass<TIn>,
        outputClass: KClass<TOut>,
        lambda: (TIn) -> TOut
    ): DependentFunction =
        NothingDependentFunctionImplementation(inputClass, outputClass, lambda)

    fun <TIn : Any, TDependency : Any, TOut : Any> function(
        inputClass: KClass<TIn>,
        dependencyClass: KClass<TDependency>,
        outputClass: KClass<TOut>,
        lambda: (TIn, TDependency) -> TOut
    ): DependentFunction =
        DependentFunctionImplementation(inputClass, dependencyClass, outputClass, instanceProvider, lambda)
}

inline fun <reified TIn : Any, reified TOut : Any> FunctionFactory.function(
    noinline logic: (TIn) -> TOut
) = function(TIn::class, TOut::class, logic)

inline fun <reified TIn : Any, reified TDependency : Any, reified TOut : Any> FunctionFactory.function(
    noinline logic: (TIn, TDependency) -> TOut
) = function(TIn::class, TDependency::class, TOut::class, logic)

private abstract class DependentFunctionBaseImplementation<TFuncIn : Any, TFuncOut : Any>(
    private val functionInputClass: KClass<TFuncIn>,
    private val functionOutputClass: KClass<TFuncOut>,
) : DependentFunction {
    override fun <TIn : Any, TOut : Any> execute(
        inputClass: KClass<TIn>,
        outputClass: KClass<TOut>,
        input: TIn,
    ): TOut {
        require(inputClass == functionInputClass)
        require(outputClass == functionOutputClass)
        return outputClass.cast(execute(functionInputClass.cast(input)))
    }

    abstract fun execute(input: TFuncIn): TFuncOut
}

private class NothingDependentFunctionImplementation<TFuncIn : Any, TFuncOut : Any>(
    functionInputClass: KClass<TFuncIn>,
    functionOutputClass: KClass<TFuncOut>,
    private val lambda: (TFuncIn) -> TFuncOut,
) : DependentFunctionBaseImplementation<TFuncIn, TFuncOut>(functionInputClass, functionOutputClass) {
    override val isDependencyFulfilled: Boolean
        get() = true

    override fun execute(input: TFuncIn): TFuncOut = lambda(input)
}

private class DependentFunctionImplementation<TFuncIn : Any, TDependency : Any, TFuncOut : Any>(
    functionInputClass: KClass<TFuncIn>,
    private val dependencyClass: KClass<TDependency>,
    functionOutputClass: KClass<TFuncOut>,
    private val instanceProvider: InstanceProvider,
    private val lambda: (TFuncIn, TDependency) -> TFuncOut,
) : DependentFunctionBaseImplementation<TFuncIn, TFuncOut>(functionInputClass, functionOutputClass) {
    override val isDependencyFulfilled: Boolean
        get() = try {
            instanceProvider.instance(dependencyClass)
            true
        } catch (e: Exception) {
            false
        }

    override fun execute(input: TFuncIn): TFuncOut = lambda(input, instanceProvider.instance(dependencyClass))
}