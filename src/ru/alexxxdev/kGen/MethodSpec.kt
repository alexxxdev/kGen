package ru.alexxxdev.kGen

/**
 * Created by alexxxdev on 09.03.17.
 */
class MethodSpec private constructor(builder: Builder) : IAppendable {

    private var name: String

    init {
        this.name = builder.name
    }

    class Builder internal constructor(internal val name: String) {
        fun build(): MethodSpec {
            return MethodSpec(this)
        }
    }

    override fun writeTo(out: Appendable?) {

    }
}