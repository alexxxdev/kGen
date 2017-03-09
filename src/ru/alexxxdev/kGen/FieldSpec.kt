package ru.alexxxdev.kGen

/**
 * Created by alexxxdev on 09.03.17.
 */
class FieldSpec private constructor(builder: Builder) : IAppendable {

    private var name: String

    init {
        this.name = builder.name
    }

    class Builder internal constructor(internal val name: String) {
        fun build(): FieldSpec {
            return FieldSpec(this)
        }
    }

    override fun writeTo(out: Appendable?) {

    }
}