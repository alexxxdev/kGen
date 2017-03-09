package ru.alexxxdev.kGen

/**
 * Created by alexxxdev on 09.03.17.
 */

fun kotlinFile(`package`: String, name: String? = null, init: KotlinFile.Builder.() -> Unit): KotlinFile {
    val kf = KotlinFile.builder(`package`, name)
    kf.init()
    return kf.build()
}