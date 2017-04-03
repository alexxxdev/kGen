package ru.alexxxdev.kGen

import javax.lang.model.SourceVersion

/**
 * Created by alexxxdev on 09.03.17.
 */
class MethodSpec(val name: String) : IAppendable {
    private var imports = mutableListOf<TypeName>()
    private var body: StringBuffer? = null
    private var ret: Returns? = null

    private var modifiers: Array<Modifier> = emptyArray()

    internal data class Returns(var value: String?, val type: TypeName?) {
        init {
            if (value == "kotlin.Unit") value = null
        }
    }

    val listImports get() = this.imports.filter { it is ru.alexxxdev.kGen.ClassName }.map { it as ru.alexxxdev.kGen.ClassName }.distinctBy { (it as ru.alexxxdev.kGen.ClassName).canonicalName }

    operator fun Modifier.unaryPlus() {
        modifiers = modifiers.plus(this)
    }

    operator fun String.unaryPlus() {
        if (body == null) body = StringBuffer()
        body?.append(this)?.append('\n')
    }

    internal fun addModificator(it: Modifier) {
        modifiers = modifiers.plus(it)
    }

    fun MethodSpec.returns(typeName: TypeName? = null, init: () -> String) {
        ret = Returns(init(), typeName)
    }

    fun build() {
        ret?.type?.let { imports.add(it) }
    }

    override fun writeTo(codeWriter: CodeWriter) {
        check(SourceVersion.isName(name), "not a valid name: %s", name)
        if (modifiers.isEmpty()) {
            codeWriter.out("fun $name()")
        } else {
            modifiers.forEach {
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
                when (it.type) {
                    is ClassName -> {
                        codeWriter.out(": ${it.type.name}")
                    }
                    is ParameterizedName -> {
                        codeWriter.out(": ${it.type.name}")
                    }
                }
            }
            codeWriter.out(" {\n")
            codeWriter.indent()
            codeWriter.out(body.toString())

            ret?.let {
                codeWriter.out("return ${it.value}")
            }
            codeWriter.unindent()
            codeWriter.out("\n}")
        } else {
            if (ret != null) {
                if (ret?.value == null) {
                    when (ret?.type) {
                        is ClassName -> {
                            codeWriter.out(": ${(ret?.type as ClassName).name}")
                        }
                        is ParameterizedName -> {
                            codeWriter.out(": ${(ret?.type as ParameterizedName).name}")
                        }
                    }
                } else {
                    codeWriter.out(" = ${ret?.value}")
                }
            } else {
                codeWriter.out(" { }")
            }
        }
    }
}
