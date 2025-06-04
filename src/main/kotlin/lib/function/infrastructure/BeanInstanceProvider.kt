package org.example.dependent.kts.lib.function.infrastructure

import org.example.dependent.kts.lib.function.infrastructure.base.InstanceProvider
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.stereotype.Service
import kotlin.reflect.KClass
import kotlin.reflect.cast

@Service
class BeanInstanceProvider(private val beanFactory: AutowireCapableBeanFactory) : InstanceProvider {
    override fun <T : Any> instance(type: KClass<T>): T = type.cast(beanFactory.autowire(type.java, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, false))
}