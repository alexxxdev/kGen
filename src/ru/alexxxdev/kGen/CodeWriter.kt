package ru.alexxxdev.kGen

/**
 * Created by alexxxdev on 14.03.17.
 */
class CodeWriter(private val out: Appendable?, private var indent: String) {
    private var indentLevel = 0
    private var trailingNewline = false

    fun out(s: String) {
        emitAndIndent(s)
    }

    private fun emitAndIndent(s: String) {
        var first = true
        s.split("\n").forEach {
            if (!first) {
                out?.append("\n")
                trailingNewline = true
            }
            first = false

            if (it.isNotEmpty()) {
                if (trailingNewline) {
                    emitIndentation()
                }
                out?.append(it)
                trailingNewline = false
            }
        }
    }

    private fun emitIndentation() {
        (0..indentLevel - 1).forEach {
            out?.append(indent)
        }
    }

    fun indent() {
        indentLevel++
    }

    fun unindent() {
        indentLevel--
    }
}