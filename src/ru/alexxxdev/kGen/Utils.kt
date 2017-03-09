package ru.alexxxdev.kGen

/**
 * Created by alexxxdev on 09.03.17.
 */

fun check(condition: Boolean, format: String, vararg args: Any) {
    if (!condition) throw IllegalArgumentException(String.format(format, *args))
}