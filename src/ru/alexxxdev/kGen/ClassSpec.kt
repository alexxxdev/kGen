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

    var name: String
    var kind: Kind

    init {
        this.name = builder.name
        this.kind = builder.kind
    }

    class Builder internal constructor(internal val kind: Kind, internal val name: String) {

        fun build(): ClassSpec {
            return ClassSpec(this)
        }
    }

    override fun writeTo(out: Appendable?) {
        when (kind) {
            ClassSpec.Kind.CLASS ->
                out?.append("class $name")
            ClassSpec.Kind.INTERFACE ->
                out?.append("interface $name")
            ClassSpec.Kind.OBJECT ->
                out?.append("object $name")
        }

        out?.append("\n")
    }
}