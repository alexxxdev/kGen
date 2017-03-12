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

        import(String::class)

        kotlinClass("Test1") {
            modifiers(Modifier.OPEN)
            method("ww1") {
                modifiers(Modifier.OPEN)
                returns("0", ClassName.get(Int::class))
            }
        }

        kotlinInterface("Test2") {
            modifiers(Modifier.ABSTRACT)
            method("ww1") {
                returns(".05f", ClassName.get(Float::class))
            }
            method("ww2") {}
        }

        kotlinObject("Test3") {
            modifiers(Modifier.PRIVATE)
            method("ww1") {}
        }

        method("ww1") {
            returns("\"test\"", ClassName.get(String::class))
        }
        method("ww2") {}

    }.writeTo(file)
}