package com.mitteloupe.randomgenkt.model

import com.mitteloupe.randomgenkt.model.MutableDataProviderMap.ConfiguredFieldDataProvider

interface DataProviderMap<GENERATED_INSTANCE : Any> :
    Map<String, ConfiguredFieldDataProvider<GENERATED_INSTANCE, Any>>
