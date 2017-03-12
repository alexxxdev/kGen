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
    private var imports = mutableListOf<ClassName>()
    val listImports get() = this.imports.distinctBy { it.canonicalName }

    init {
        this.name = builder.name
        this.kind = builder.kind
        this.methods = builder.methods

        methods.forEach {
            imports.addAll(it.listImports)
        }
    }

    class Builder internal constructor(internal val kind: Kind, internal val name: String) {
        internal var methods = mutableListOf<MethodSpec>()

        fun addMethod(methodSpec: MethodSpec): ClassSpec.Builder {
            methods.add(methodSpec)
            return this
        }

        fun build(): ClassSpec {
            return ClassSpec(this)
        }
    }

    override fun writeTo(tab: String, out: Appendable?) {
        when (kind) {
            ClassSpec.Kind.CLASS ->
                out?.append("class $name")
            ClassSpec.Kind.INTERFACE ->
                out?.append("interface $name")
            ClassSpec.Kind.OBJECT ->
                out?.append("object $name")
        }

        out?.append("{")

        out?.append("\n")

        methods.sortedBy { it.name }
                .forEachIndexed { index, methodSpec ->
                    methodSpec.writeTo("\t", out)
                    if (index < methods.size - 1) out?.append("\n")
                }

        out?.append("}\n")
    }
}