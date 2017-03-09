package ru.alexxxdev.kGen

/**
 * Created by alexxxdev on 09.03.17.
 */

fun kotlinFile(`package`: String, name: String? = null, init: KotlinFile.Builder.() -> Unit): KotlinFile {
    val kf = KotlinFile.builder(`package`, name)
    kf.init()
    return kf.build()
}

fun KotlinFile.Builder.kotlinClass(name: String, init: ClassSpec.Builder.() -> Unit = {}): ClassSpec {
    val cs: ClassSpec.Builder = ClassSpec.classBuilder(name)
    cs.init()
    val `class` = cs.build()
    addClass(`class`)
    return `class`
}

fun KotlinFile.Builder.kotlinInterface(name: String, init: ClassSpec.Builder.() -> Unit = {}): ClassSpec {
    val cs: ClassSpec.Builder = ClassSpec.interfaceBuilder(name)
    cs.init()
    val `class` = cs.build()
    addClass(`class`)
    return `class`
}

fun KotlinFile.Builder.kotlinObject(name: String, init: ClassSpec.Builder.() -> Unit = {}): ClassSpec {
    val cs: ClassSpec.Builder = ClassSpec.objectBuilder(name)
    cs.init()
    val `class` = cs.build()
    addClass(`class`)
    return `class`
}