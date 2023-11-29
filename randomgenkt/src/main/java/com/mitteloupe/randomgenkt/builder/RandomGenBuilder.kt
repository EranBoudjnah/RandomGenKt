package com.mitteloupe.randomgenkt.builder

import com.mitteloupe.randomgenkt.FieldDataProviderFactory
import com.mitteloupe.randomgenkt.SimpleFieldDataProviderFactory
import com.mitteloupe.randomgenkt.model.InstanceProvider
import java.util.Random
import kotlin.reflect.KClass

class RandomGenBuilder<GENERATED_INSTANCE : Any> {
    inline fun <reified CLASS_TYPE : GENERATED_INSTANCE> ofJavaClass():
        IncompleteBuilderField<GENERATED_INSTANCE> =
        @Suppress("UNCHECKED_CAST")
        ofJavaClass(CLASS_TYPE::class.java as Class<GENERATED_INSTANCE>)

    inline fun <reified CLASS_TYPE : GENERATED_INSTANCE> ofKotlinClass():
        IncompleteBuilderField<GENERATED_INSTANCE> =
        @Suppress("UNCHECKED_CAST")
        ofKotlinClass(CLASS_TYPE::class as KClass<GENERATED_INSTANCE>)

    fun ofJavaClass(
        generatedInstanceClass: Class<GENERATED_INSTANCE>
    ): IncompleteBuilderField<GENERATED_INSTANCE> = IncompleteBuilderField(
        generatedInstanceClass,
        DefaultFieldDataProviderFactory()
    )

    fun ofKotlinClass(
        generatedInstanceClass: KClass<GENERATED_INSTANCE>
    ): IncompleteBuilderField<GENERATED_INSTANCE> = IncompleteBuilderField(
        generatedInstanceClass,
        DefaultFieldDataProviderFactory()
    )

    internal inline fun <reified CLASS_TYPE : GENERATED_INSTANCE> ofJavaClassWithFactory(
        factory: FieldDataProviderFactory<GENERATED_INSTANCE>
    ) = @Suppress("UNCHECKED_CAST")
    (
        IncompleteBuilderField(
            CLASS_TYPE::class.java as Class<GENERATED_INSTANCE>,
            factory
        )
        )

    internal inline fun <reified CLASS_TYPE : GENERATED_INSTANCE> ofKotlinClassWithFactory(
        factory: FieldDataProviderFactory<GENERATED_INSTANCE>
    ) = @Suppress("UNCHECKED_CAST")
    (
        IncompleteBuilderField(
            CLASS_TYPE::class as KClass<GENERATED_INSTANCE>,
            factory
        )
        )

    fun withProvider(
        instanceProvider: InstanceProvider<GENERATED_INSTANCE>
    ): IncompleteBuilderField<GENERATED_INSTANCE> = IncompleteBuilderField(
        instanceProvider,
        DefaultFieldDataProviderFactory()
    )

    internal fun withFieldFactoryAndProvider(
        factory: FieldDataProviderFactory<GENERATED_INSTANCE>,
        instanceProvider: InstanceProvider<GENERATED_INSTANCE>
    ) = IncompleteBuilderField(instanceProvider, factory)

    private class DefaultFieldDataProviderFactory<GENERATED_INSTANCE> :
        SimpleFieldDataProviderFactory<GENERATED_INSTANCE>(Random())
}
