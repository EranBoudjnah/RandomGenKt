package com.mitteloupe.randomgenkt.model

fun interface InstanceProvider<GENERATED_INSTANCE : Any> :
    (DataProviderMap<GENERATED_INSTANCE>) -> GENERATED_INSTANCE {
    operator fun invoke() = invoke(MutableDataProviderMap())
}
