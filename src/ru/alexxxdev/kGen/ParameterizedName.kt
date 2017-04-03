package ru.alexxxdev.kGen

/**
 * Created by alexx on 03.04.2017.
 */
class ParameterizedName private constructor(internal val className: ClassName?, internal val name: String?) : TypeName() {
    companion object {
        fun get(name: String): ParameterizedName {
            return ParameterizedName(null, name)
        }

        fun get(name: String, className: ClassName): ParameterizedName {
            return ParameterizedName(className, name)
        }

        fun get(className: ClassName): ParameterizedName {
            return ParameterizedName(className, null)
        }
    }
}