package ru.alexxxdev.kGen

import ru.alexxxdev.kGen.FieldSpec.PropertyType.MUTABLE
import ru.alexxxdev.kGen.FieldSpec.PropertyType.READONLY
import ru.alexxxdev.kGen.FieldSpec.ValueType.NOTNULL
import ru.alexxxdev.kGen.FieldSpec.ValueType.NULLABLE

/**
 * Created by alexxxdev on 09.03.17.
 */
class FieldSpec(val name: String, val propertyType: PropertyType = READONLY, val valueType: ValueType = NOTNULL) : IAppendable {

    enum class PropertyType { READONLY, MUTABLE }
    enum class ValueType { NOTNULL, NULLABLE }

    private var imports = mutableListOf<TypeName>()
    private var modifiers: Array<Modifier> = emptyArray()

    var className: TypeName? = null
    var initializer: String? = null

    val listImports get() = this.imports.filter { it is ru.alexxxdev.kGen.ClassName }.distinctBy { (it as ClassName).canonicalName }

    operator fun Modifier.unaryPlus() {
        modifiers = modifiers.plus(this)
    }

    fun build() {
        className?.let { imports.add(it) }
    }

    override fun writeTo(codeWriter: CodeWriter) {
        modifiers.forEach {
            when (it) {
                Modifier.DEFAULT -> {
                }
                else -> {
                    codeWriter.out("${it.name.toLowerCase()} ")
                }
            }
        }

        when (propertyType) {
            MUTABLE -> {
                codeWriter.out("var $name")
            }
            READONLY -> {
                codeWriter.out("val $name")
            }
        }

        when (className) {
            is ClassName -> {
                codeWriter.out(": ${(className as ClassName).name}")
            }
            is ParameterizedName -> {
                codeWriter.out(": ${(className as ParameterizedName).name}")
            }
        }

        if (valueType == NULLABLE) codeWriter.out("?")

        initializer?.let { codeWriter.out(" = $it") }
    }
}