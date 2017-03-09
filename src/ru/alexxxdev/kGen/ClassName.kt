package ru.alexxxdev.kGen

import kotlin.reflect.KClass

/**
 * Created by alexxxdev on 02.03.17.
 */
class ClassName private constructor(internal val canonicalName: String, internal val name: String) {

    companion object {
        fun get(packageName: String, name: String): ClassName {
            return ClassName(packageName, name)
        }

        fun get(kclass: KClass<out Any>): ClassName {
            return ClassName(kclass.qualifiedName!!, kclass.simpleName!!)
        }

        fun get(jclass: Class<out Any>): ClassName {
            return ClassName(jclass.canonicalName, jclass.simpleName)
        }
    }

}