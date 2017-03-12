package ru.alexxxdev.kGen

/**
 * Created by alexxxdev on 09.03.17.
 */
interface IAppendable {
    fun writeTo(tab: String = "", out: Appendable?)
}