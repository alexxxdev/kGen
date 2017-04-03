package ru.alexxxdev.kGen

import ru.alexxxdev.kGen.FieldSpec.PropertyType
import ru.alexxxdev.kGen.FieldSpec.PropertyType.READONLY
import ru.alexxxdev.kGen.FieldSpec.ValueType
import ru.alexxxdev.kGen.FieldSpec.ValueType.NOTNULL

/**
 * Created by alexxxdev on 28.02.17.
 */
class ClassSpec(val kind: Kind, internal val name: String) : IAppendable {

    enum class Kind { CLASS, INTERFACE, OBJECT }

    private var imports = mutableListOf<TypeName>()
    private var fields = mutableListOf<FieldSpec>()
    private var methods = mutableListOf<MethodSpec>()
    private var parameterized = mutableListOf<ParameterizedName>()

    private var modifiers: Array<Modifier> = emptyArray()

    val listImports get() = this.imports.distinctBy { (it as ru.alexxxdev.kGen.ClassName).canonicalName }

    operator fun Modifier.unaryPlus() {
        modifiers = modifiers.plus(this)
    }

    operator fun ParameterizedName.unaryPlus() {
        parameterized.add(this)
    }

    fun field(name: String, propertyType: PropertyType = READONLY, valueType: ValueType = NOTNULL, init: FieldSpec.() -> String) = addField(FieldSpec(name, propertyType, valueType), init)
    fun method(name: String, vararg mods: Modifier, init: MethodSpec.() -> Unit) = addMethod(MethodSpec(name), mods = *mods, init = init)

    private fun addMethod(methodSpec: MethodSpec, vararg mods: Modifier, init: MethodSpec.() -> Unit): MethodSpec {
        methodSpec.init()
        mods.forEach { methodSpec.addModificator(it) }
        methodSpec.build()
        methods.add(methodSpec)
        return methodSpec
    }

    private fun addField(fieldSpec: FieldSpec, init: FieldSpec.() -> String): FieldSpec {
        fieldSpec.initializer = fieldSpec.init()
        fieldSpec.build()
        fields.add(fieldSpec)
        return fieldSpec
    }

    fun build() {
        methods.forEach {
            imports.addAll(it.listImports)
        }

        fields.forEach {
            imports.addAll(it.listImports)
        }
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

        when (kind) {
            ClassSpec.Kind.CLASS ->
                codeWriter.out("class $name")
            ClassSpec.Kind.INTERFACE ->
                codeWriter.out("interface $name")
            ClassSpec.Kind.OBJECT ->
                codeWriter.out("object $name")
        }

        if (parameterized.isNotEmpty()) {
            codeWriter.out("<")
            parameterized.forEachIndexed { index, parameterizedName ->
                if (index > 0) codeWriter.out(",")
                if (parameterizedName.name == null) {
                    parameterizedName.className?.let {
                        codeWriter.out(it.name)
                    }
                } else {
                    codeWriter.out(parameterizedName.name)
                    parameterizedName.className?.let {
                        codeWriter.out(": ${it.name}")
                    }
                }
            }
            codeWriter.out(">")
        }

        codeWriter.out("{")

        codeWriter.indent()

        fields.sortedBy { it.name }
                .forEach {
                    codeWriter.out("\n")
                    it.writeTo(codeWriter)
                }


        methods.sortedBy { it.name }
                .forEach {
                    codeWriter.out("\n\n")
                    it.writeTo(codeWriter)
                }

        codeWriter.unindent()
        codeWriter.out("\n")
        codeWriter.out("}")
    }
}

