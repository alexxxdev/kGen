package ru.alexxxdev.kGen

import java.io.File
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by alexxxdev on 28.02.17.
 */
class KotlinFile private constructor(builder: Builder) : IAppendable {

    companion object {
        fun builder(packageName: String, name: String?): Builder {
            return Builder(packageName, name)
        }
    }

    internal val packageName: String
    internal val fileName: String?

    init {
        this.packageName = builder.packageName
        this.fileName = builder.name
    }

    class Builder internal constructor(internal val packageName: String, internal val name: String?) {

        fun build(): KotlinFile {
            return KotlinFile(this)
        }
    }

    override fun writeTo(out: Appendable?) {
        out?.append("package $packageName\n\n")
    }

    fun writeTo(directory: File) {
        writeTo(directory.toPath())
    }

    fun writeTo(directory: Path) {
        var outputDirectory = directory
        if (!packageName.isEmpty()) {
            for (packageComponent in packageName.split("\\.".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()) {
                outputDirectory = outputDirectory.resolve(packageComponent)
            }
            Files.createDirectories(outputDirectory)
        }

        if (fileName != null) {
            val outputPath = outputDirectory.resolve("$fileName.kt")
            OutputStreamWriter(Files.newOutputStream(outputPath), Charsets.UTF_8).use({ writer -> writeTo(writer) })
        } else {
            //TODO first class name
            //val outputPath = outputDirectory.resolve("$fileName.kt")
            //OutputStreamWriter(Files.newOutputStream(outputPath), Charsets.UTF_8).use({ writer -> writeTo(writer) })
        }
    }
}