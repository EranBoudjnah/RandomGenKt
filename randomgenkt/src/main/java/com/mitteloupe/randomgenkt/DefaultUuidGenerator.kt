package com.mitteloupe.randomgenkt

import java.util.UUID

object DefaultUuidGenerator : UuidGenerator {
    override fun randomUUID() = UUID.randomUUID().toString()
}
