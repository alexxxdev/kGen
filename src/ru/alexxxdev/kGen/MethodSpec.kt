package ru.alexxxdev.kGen

import javax.lang.model.SourceVersion

/**
 * Created by alexxxdev on 09.03.17.
 */
class MethodSpec(val name: String) : IAppendable {
    private var imports = mutableListOf<ClassName>()
    private var body: StringBuffer? = null
    private var ret: Returns? = null

    private var modifiers: Array<Modifier> = emptyArray()

    internal data class Returns(val value: String, val type: ClassName?)

    val listImports get() = this.imports.distinctBy { it.canonicalName }

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

    fun MethodSpec.returns(className: ClassName? = null, init: () -> String) {
        ret = Returns(init(), className)
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
                codeWriter.out(": ${it.type?.name}")
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
                codeWriter.out(" = ${ret?.value}")
            } else {
                codeWriter.out(" { }")
            }
        }
    }
}
