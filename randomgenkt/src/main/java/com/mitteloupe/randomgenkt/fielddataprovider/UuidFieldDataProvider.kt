package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import com.mitteloupe.randomgenkt.UuidGenerator

class UuidFieldDataProvider<OUTPUT_TYPE>(
    private val uuidGenerator: UuidGenerator
) : FieldDataProvider<OUTPUT_TYPE, String>() {
    override fun invoke(instance: OUTPUT_TYPE?) = uuidGenerator.randomUUID()
}
