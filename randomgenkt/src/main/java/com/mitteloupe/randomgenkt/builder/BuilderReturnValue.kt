package com.mitteloupe.randomgenkt.builder

import com.mitteloupe.randomgenkt.FieldDataProvider
import com.mitteloupe.randomgenkt.FieldDataProviderFactory
import kotlin.reflect.KClass

class BuilderReturnValue<RETURN_TYPE : Any> internal constructor(
    private val builderField: BuilderField<RETURN_TYPE>,
    private val factory: FieldDataProviderFactory<RETURN_TYPE>
) {
    fun <VALUE_TYPE : Any> returningExplicitly(value: VALUE_TYPE) = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getExplicitFieldDataProvider(value))
    )

    fun <VALUE_TYPE : Any> returning(list: List<VALUE_TYPE>): BuilderField<RETURN_TYPE> =
        getBuilderFieldFromIncomplete(
            builderField.returning(factory.getGenericListFieldDataProvider(list))
        )

    fun returningBoolean() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.booleanFieldDataProvider)
    )

    fun returningByte() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.byteFieldDataProvider)
    )

    fun returningBytes(minimumSize: Int, maximumSize: Int = minimumSize) =
        getBuilderFieldFromIncomplete(
            builderField.returning(
                factory.getByteArrayFieldDataProvider(minimumSize, maximumSize)
            )
        )

    fun returningDouble() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getDoubleFieldDataProvider())
    )

    fun returningFloat() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getFloatFieldDataProvider())
    )

    fun returningInt() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getIntFieldDataProvider())
    )

    fun returningLong() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getLongFieldDataProvider())
    )

    fun returningShort() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getShortFieldDataProvider())
    )

    fun returning(minimum: Double, maximum: Double) = getBuilderFieldFromIncomplete(
        builderField.returning(
            factory.getDoubleFieldDataProvider(minimum, maximum)
        )
    )

    fun returning(minimum: Float, maximum: Float) = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getFloatFieldDataProvider(minimum, maximum))
    )

    fun returning(minimum: Int, maximum: Int) = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getIntFieldDataProvider(minimum, maximum))
    )

    fun returning(minimum: Long, maximum: Long) = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getLongFieldDataProvider(minimum, maximum))
    )

    fun returning(minimum: Short, maximum: Short) = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getShortFieldDataProvider(minimum, maximum))
    )

    fun returningSequentialInteger() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.sequentialIntegerFieldDataProvider)
    )

    fun returningSequentialInteger(startValue: Int) = getBuilderFieldFromIncomplete(
        builderField.returning(
            factory.getSequentialIntegerFieldDataProvider(startValue)
        )
    )

    fun returningUuid() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.uuidFieldDataProvider)
    )

    fun returningRgb(alpha: Boolean) = getBuilderFieldFromIncomplete(
        builderField.returning(factory.getRgbFieldDataProvider(alpha))
    )

    fun returningLoremIpsum() = getBuilderFieldFromIncomplete(
        builderField.returning(factory.loremIpsumFieldDataProvider)
    )

    fun returningLoremIpsum(minimumLength: Int, maximumLength: Int = minimumLength) =
        getBuilderFieldFromIncomplete(
            builderField.returning(
                factory.getLoremIpsumFieldDataProvider(
                    minimumLength,
                    maximumLength
                )
            )
        )

    fun returningLoremIpsum(minimumLength: Int, maximumLength: Int, paragraphDelimiter: String) =
        getBuilderFieldFromIncomplete(
            builderField.returning(
                factory.getLoremIpsumFieldDataProvider(
                    minimumLength,
                    maximumLength,
                    paragraphDelimiter
                )
            )
        )

    inline fun <reified ENUM_TYPE : Enum<*>> returning() = returning(ENUM_TYPE::class.java)

    fun <ENUM_TYPE : Enum<*>> returning(enumClass: Class<ENUM_TYPE>) =
        getBuilderFieldFromIncomplete(
            builderField.returning(factory.getRandomEnumFieldDataProvider(enumClass))
        )

    fun <ENUM_TYPE : Enum<*>> returning(enumClass: KClass<ENUM_TYPE>) =
        getBuilderFieldFromIncomplete(
            builderField.returning(factory.getRandomEnumFieldDataProvider(enumClass))
        )

    fun <VALUE_TYPE : Any> returning(
        fieldDataProvider: FieldDataProvider<RETURN_TYPE, VALUE_TYPE>
    ) = getBuilderFieldFromIncomplete(builderField.returning(fieldDataProvider))

    fun <VALUE_TYPE : Any> returning(
        fieldDataProvider: FieldDataProvider<RETURN_TYPE, VALUE_TYPE>,
        instances: Int
    ) = getBuilderFieldFromIncomplete(
        builderField.returning(
            factory.getCustomListFieldDataProvider(fieldDataProvider, instances)
        )
    )

    fun <VALUE_TYPE : Any> returning(
        fieldDataProvider: FieldDataProvider<RETURN_TYPE, VALUE_TYPE>,
        minimumInstances: Int,
        maximumInstances: Int
    ) = getBuilderFieldFromIncomplete(
        builderField.returning(
            factory.getCustomListFieldDataProvider(
                fieldDataProvider,
                minimumInstances = minimumInstances,
                maximumInstances = maximumInstances
            )
        )
    )

    private fun getBuilderFieldFromIncomplete(
        incompleteBuilderField: IncompleteBuilderField<RETURN_TYPE>
    ) = when (incompleteBuilderField.initializeType) {
        IncompleteBuilderField.InitializeType.WITH_JAVA_CLASS -> {
            BuilderField(
                incompleteBuilderField.generatedInstanceJavaClass,
                incompleteBuilderField
            )
        }

        IncompleteBuilderField.InitializeType.WITH_KOTLIN_CLASS -> {
            BuilderField(
                incompleteBuilderField.generatedInstanceKotlinClass,
                incompleteBuilderField
            )
        }

        IncompleteBuilderField.InitializeType.WITH_INSTANCE_PROVIDER -> {
            BuilderField(
                incompleteBuilderField.instanceProvider,
                incompleteBuilderField
            )
        }
    }
}
