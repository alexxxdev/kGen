package ru.alexxxdev.kGen

/**
 * Created by alexxxdev on 28.02.17.
 */
class ClassSpec private constructor(builder: Builder) : IAppendable {

    enum class Kind { CLASS, INTERFACE, OBJECT }

    companion object {
        fun classBuilder(className: String): Builder {
            return Builder(Kind.CLASS, className)
        }

        fun interfaceBuilder(className: String): Builder {
            return Builder(Kind.INTERFACE, className)
        }

        fun objectBuilder(className: String): Builder {
            return Builder(Kind.OBJECT, className)
        }
    }

    internal var name: String
    internal var kind: Kind
    internal var methods = mutableListOf<MethodSpec>()
    internal var mods: Array<out Modifier> = emptyArray()
    private var imports = mutableListOf<ClassName>()
    val listImports get() = this.imports.distinctBy { it.canonicalName }

    init {
        this.name = builder.name
        this.kind = builder.kind
        this.methods = builder.methods
        this.mods = builder.mods

        methods.forEach {
            imports.addAll(it.listImports)
        }
    }

    class Builder internal constructor(internal val kind: Kind, internal val name: String) {
        internal var methods = mutableListOf<MethodSpec>()
        internal var mods: Array<out Modifier> = emptyArray()

        fun addMethod(methodSpec: MethodSpec): ClassSpec.Builder {
            methods.add(methodSpec)
            return this
        }

        fun addModifiers(vararg modifiers: Modifier) {
            mods = modifiers
        }

        fun build(): ClassSpec {
            return ClassSpec(this)
        }
    }

    override fun writeTo(codeWriter: CodeWriter) {
        mods.forEach {
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

        codeWriter.out("{\n")

        codeWriter.indent()

        methods.sortedBy { it.name }
                .forEach {
                    codeWriter.out("\n")
                    it.writeTo(codeWriter)
                }

        codeWriter.unindent()

        codeWriter.out("}")
    }
}