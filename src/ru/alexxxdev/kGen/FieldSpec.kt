package ru.alexxxdev.kGen

/**
 * Created by alexxxdev on 09.03.17.
 */
class FieldSpec private constructor(builder: Builder) : IAppendable {

    private var imports = mutableListOf<ClassName>()

    val listImports get() = this.imports.distinctBy { it.canonicalName }

    internal var name: String
    private var type: Int
    private var typeValue: Int
    private var initializer: String? = null
    private var className: ClassName? = null

    companion object {
        const val NOTNULL = 0
        const val NULLABLE = 1
        const val MUTABLE = 2
        const val READONLY = 3
    }

    init {
        this.name = builder.name
        this.type = builder.type
        this.typeValue = builder.typeValue
        this.initializer = builder.initializer
        this.className = builder.className
        className?.let { imports.add(it) }
    }

    class Builder internal constructor(internal val name: String, internal val type: Int, internal val typeValue: Int) {
        internal var initializer: String? = null
        internal var className: ClassName? = null

        fun initializer(init: String): Builder {
            this.initializer = init
            return this
        }

        fun className(className: ClassName): Builder {
            this.className = className
            return this
        }

        fun build(): FieldSpec {
            return FieldSpec(this)
        }
    }

    override fun writeTo(codeWriter: CodeWriter) {
        when (type) {
            MUTABLE -> {
                codeWriter.out("var $name")
            }
            READONLY -> {
                codeWriter.out("val $name")
            }
        }

        className?.let { codeWriter.out(":${it.name}") }

        if (typeValue == NULLABLE) codeWriter.out("?")

        initializer?.let { codeWriter.out(" = $it") }
    }
}