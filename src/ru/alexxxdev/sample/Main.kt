package ru.alexxxdev.sample

import ru.alexxxdev.kGen.*
import java.io.File

/**
 * Created by alexxxdev on 28.02.17.
 */
fun main(args: Array<String>) {
    println("Hello!")

    val file = File("src")
    kotlinFile("ru.alexxxdev.sample", "Test") {

        import("ru.alexxxdev.sample.Test1")
        import(ClassName.get("ru.alexxxdev.sample", "Test2"))
        import(java.lang.String::class.java)
        import(String::class)


        kotlinClass("Test1") {}

        kotlinInterface("Test2") {}

        kotlinObject("Test3") {}
    }.writeTo(file)
}