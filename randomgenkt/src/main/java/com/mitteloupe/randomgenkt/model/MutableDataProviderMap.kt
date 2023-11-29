package com.mitteloupe.randomgenkt.model

import com.mitteloupe.randomgenkt.FieldDataProvider
import com.mitteloupe.randomgenkt.model.MutableDataProviderMap.ConfiguredFieldDataProvider
import kotlin.collections.MutableMap.MutableEntry

private typealias DataProviders<GENERATED_INSTANCE> =
MutableMap<String, ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>>

private typealias DataProvidersEntry<GENERATED_INSTANCE> =
MutableEntry<String, ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>>

class MutableDataProviderMap<GENERATED_INSTANCE : Any>(
    vararg keysValueProviders: Pair<String, ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>> =
        emptyArray()
) : DataProviderMap<GENERATED_INSTANCE>, DataProviders<GENERATED_INSTANCE> {

    constructor(dataProviderMap: DataProviderMap<GENERATED_INSTANCE>) : this(
        *dataProviderMap.entries.map { entry -> entry.key to entry.value }.toTypedArray()
    )

    private val dataProviders: DataProviders<GENERATED_INSTANCE> =
        LinkedHashMap(mapOf(*keysValueProviders))

    override val entries: MutableSet<DataProvidersEntry<GENERATED_INSTANCE>>
        get() = dataProviders.entries

    override val keys: MutableSet<String>
        get() = dataProviders.keys

    override val size: Int
        get() = dataProviders.size

    override val values: MutableCollection<ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>>
        get() = dataProviders.values

    override fun clear() {
        dataProviders.clear()
    }

    override fun isEmpty() = dataProviders.isEmpty()

    override fun get(key: String): ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>? =
        dataProviders[key]

    override fun containsValue(value: ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>) =
        dataProviders.containsValue(value)

    override fun containsKey(key: String) = dataProviders.containsKey(key)

    override fun remove(key: String): ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>? =
        dataProviders.remove(key)

    override fun putAll(
        from: Map<out String, ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>>
    ) {
        dataProviders.putAll(from)
    }

    override fun put(
        key: String,
        value: ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>
    ): ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>? = dataProviders.put(key, value)

    data class ConfiguredFieldDataProvider<in INSTANCE, out FIELD_DATA : Any>(
        val fieldDataProvider: FieldDataProvider<INSTANCE, FIELD_DATA>,
        val declarationSite: DeclarationSite
    )
}
