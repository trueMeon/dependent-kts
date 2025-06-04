package org.example.dependent.kts.lib.function.infrastructure.base

import kotlin.reflect.KClass

interface InstanceProvider {
    fun <T : Any>instance(type: KClass<T>): T
}