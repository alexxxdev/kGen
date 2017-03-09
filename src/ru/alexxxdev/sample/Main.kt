package ru.alexxxdev.sample

import ru.alexxxdev.kGen.kotlinFile
import java.io.File

/**
 * Created by alexxxdev on 28.02.17.
 */
fun main(args: Array<String>) {
    println("Hello!")

    val file = File("src")
    kotlinFile("ru.alexxxdev.sample", "Test") {

    }.writeTo(file)
}