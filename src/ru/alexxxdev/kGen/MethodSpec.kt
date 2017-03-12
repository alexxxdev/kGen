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

    override fun writeTo(tab: String, out: Appendable?) {
        check(SourceVersion.isName(name), "not a valid name: %s", name)
        if (mods.isEmpty()) {
            out?.append(tab)?.append("fun $name()")
        } else {
            out?.append(tab)
            mods.forEach {
                when (it) {
                    Modifier.DEFAULT -> {
                    }
                    else -> {
                        out?.append(it.name.toLowerCase())?.append(' ')
                    }
                }
            }
            out?.append("fun $name()")
        }

        if (body != null) {
            ret?.let {
                out?.append(": ${it.type.name}")
            }
            out?.append(" {\n")
            ret?.let {
                out?.append(tab)?.append("\treturn ${it.value}\n")
            }
            out?.append(tab)?.append("}\n")
        } else {
            if (ret != null) {
                out?.append(" = ${ret?.value}\n")
            } else {
                out?.append(" { }\n")
            }
        }
    }
}