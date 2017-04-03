package ru.alexxxdev.kGen

import kotlin.reflect.KClass


/**
 * Created by alexxxdev on 09.03.17.
 */

fun kotlinFile(`package`: String, name: String? = null, init: KotlinFile.() -> Unit): KotlinFile {
    val kotlinFile = KotlinFile(`package`, name)
    kotlinFile.init()
    kotlinFile.build()
    return kotlinFile
}

fun KotlinFile.import(name: String) = ClassName.get(name)
fun KotlinFile.import(className: ClassName) = className
fun KotlinFile.import(jclass: Class<out Any>) = ClassName.get(jclass)
fun KotlinFile.import(kclass: KClass<out Any>) = ClassName.get(kclass)
