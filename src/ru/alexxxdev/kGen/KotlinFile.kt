package ru.alexxxdev.kGen

import java.io.File
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path
import javax.lang.model.SourceVersion

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
    internal var classes = mutableListOf<ClassSpec>()

    init {
        this.packageName = builder.packageName
        this.fileName = builder.name
        this.classes = builder.classes
    }

    class Builder internal constructor(internal val packageName: String, internal val name: String?) {
        internal val classes = mutableListOf<ClassSpec>()

        fun addClass(cls: ClassSpec): Builder {
            classes.add(cls)
            return this
        }

        fun build(): KotlinFile {
            return KotlinFile(this)
        }
    }

    override fun writeTo(out: Appendable?) {
        out?.append("package $packageName\n\n")

        classes.forEach {
            it.writeTo(out)
        }
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
            check(fileName.isNotBlank(), "not a valid empty file name")
            check(SourceVersion.isName(fileName), "not a valid name: %s", fileName)

            val outputPath = outputDirectory.resolve("$fileName.kt")
            OutputStreamWriter(Files.newOutputStream(outputPath), Charsets.UTF_8).use({ writer -> writeTo(writer) })
        } else {
            check(classes.isNotEmpty(), "No classes or file name found")
            check(SourceVersion.isName(classes[0].name), "not a valid name: %s", classes[0].name)

            val outputPath = outputDirectory.resolve("${classes[0].name}.kt")
            OutputStreamWriter(Files.newOutputStream(outputPath), Charsets.UTF_8).use({ writer -> writeTo(writer) })
        }
    }
}