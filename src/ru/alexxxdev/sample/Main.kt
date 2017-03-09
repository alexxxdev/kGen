package ru.alexxxdev.sample

import ru.alexxxdev.kGen.kotlinClass
import ru.alexxxdev.kGen.kotlinFile
import ru.alexxxdev.kGen.kotlinInterface
import ru.alexxxdev.kGen.kotlinObject
import java.io.File

/**
 * Created by alexxxdev on 28.02.17.
 */
fun main(args: Array<String>) {
    println("Hello!")

    val file = File("src")
    kotlinFile("ru.alexxxdev.sample", "Test") {
        kotlinClass("Test1") {}

        kotlinInterface("Test2") {}

        kotlinObject("Test3") {}
    }.writeTo(file)
}