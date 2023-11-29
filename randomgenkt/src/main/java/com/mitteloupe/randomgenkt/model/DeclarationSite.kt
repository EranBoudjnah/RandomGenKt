package com.mitteloupe.randomgenkt.model

sealed interface DeclarationSite {
    val constructor: Boolean
    val classBody: Boolean

    data object Constructor : DeclarationSite {
        override val constructor: Boolean = true
        override val classBody: Boolean = false
    }

    data object ClassBody : DeclarationSite {
        override val constructor: Boolean = false
        override val classBody: Boolean = true
    }

    data object AllSites : DeclarationSite {
        override val constructor: Boolean = true
        override val classBody: Boolean = true
    }
}
