package org.example.dependent.kts.lib.function.applicaiton

import kotlin.reflect.KClass

interface DependentFunction {
    val isDependencyFulfilled: Boolean
    fun <TIn : Any, TOut : Any>execute(inputClass: KClass<TIn>, outputClass: KClass<TOut>, input: TIn): TOut
}

inline fun <reified TIn : Any, reified TOut : Any>DependentFunction.execute(input: TIn): TOut =
    execute(TIn::class, TOut::class, input)