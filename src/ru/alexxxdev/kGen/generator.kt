package ru.alexxxdev.kGen

import kotlin.reflect.KClass

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

fun KotlinFile.Builder.import(name: String) {
    this.addImport(ClassName.get(name))
}

fun KotlinFile.Builder.import(jclass: Class<out Any>) {
    this.addImport(ClassName.get(jclass))
}

fun KotlinFile.Builder.import(kclass: KClass<out Any>) {
    this.addImport(ClassName.get(kclass))
}

fun KotlinFile.Builder.import(className: ClassName) {
    this.addImport(className)
}

fun KotlinFile.Builder.imports(vararg names: String) {
    this.addImports(*names.map { ClassName.get(it) }.toTypedArray())
}

fun KotlinFile.Builder.imports(vararg classNames: ClassName) {
    this.addImports(*classNames)
}

fun KotlinFile.Builder.imports(vararg jclass: Class<out Any>) {
    this.addImports(*jclass.map { ClassName.get(it) }.toTypedArray())
}

fun KotlinFile.Builder.imports(vararg kclass: KClass<out Any>) {
    this.addImports(*kclass.map { ClassName.get(it) }.toTypedArray())
}