package ru.alexxxdev.kGen

import javax.lang.model.SourceVersion

/**
 * Created by alexxxdev on 09.03.17.
 */
class MethodSpec private constructor(builder: Builder) : IAppendable {

    private var imports = mutableListOf<ClassName>()

    internal data class Returns(val value: String, val type: ClassName)

    internal val name: String
    internal var ret: Returns? = null
    internal var body: StringBuffer? = null
    internal var mods: Array<out Modifier> = emptyArray()

    val listImports get() = this.imports.distinctBy { it.canonicalName }

    init {
        this.name = builder.name
        this.ret = builder.ret
        this.mods = builder.mods

        ret?.let { imports.add(it.type) }
    }

    class Builder internal constructor(internal val name: String) {
        internal var ret: Returns? = null
        internal var mods: Array<out Modifier> = emptyArray()

        fun addReturns(value: String, className: ClassName) {
            ret = Returns(value, className)
        }

        fun addModifiers(vararg modifiers: Modifier) {
            mods = modifiers
        }

        fun build(): MethodSpec {
            return MethodSpec(this)
        }
    }

    override fun writeTo(codeWriter: CodeWriter) {
        check(SourceVersion.isName(name), "not a valid name: %s", name)
        if (mods.isEmpty()) {
            codeWriter.out("fun $name()")
        } else {
            mods.forEach {
                when (it) {
                    Modifier.DEFAULT -> {
                    }
                    else -> {
                        codeWriter.out("${it.name.toLowerCase()} ")
                    }
                }
            }
            codeWriter.out("fun $name()")
        }

        if (body != null) {
            ret?.let {
                codeWriter.out(": ${it.type.name}")
            }
            codeWriter.out(" {\n")
            ret?.let {
                codeWriter.out("\treturn ${it.value}")
            }
            codeWriter.out("}")
        } else {
            if (ret != null) {
                codeWriter.out(" = ${ret?.value}")
            } else {
                codeWriter.out(" { }")
            }
        }
    }
}