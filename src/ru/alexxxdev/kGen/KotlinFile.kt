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
    internal var imports = mutableListOf<ClassName>()
    internal var methods = mutableListOf<MethodSpec>()

    init {
        this.packageName = builder.packageName
        this.fileName = builder.name
        this.classes = builder.classes
        this.imports = builder.imports
        this.methods = builder.methods

        classes.forEach {
            imports.addAll(it.listImports)
        }

        methods.forEach {
            imports.addAll(it.listImports)
        }
    }

    class Builder internal constructor(internal val packageName: String, internal val name: String?) {
        internal val classes = mutableListOf<ClassSpec>()
        internal var imports = mutableListOf<ClassName>()
        internal var methods = mutableListOf<MethodSpec>()

        fun addClass(cls: ClassSpec): Builder {
            classes.add(cls)
            return this
        }

        fun addImport(cln: ClassName): Builder {
            imports.add(cln)
            return this
        }

        fun addImports(vararg cln: ClassName): Builder {
            imports.addAll(cln)
            return this
        }

        fun addMethod(methodSpec: MethodSpec): Builder {
            methods.add(methodSpec)
            return this
        }

        fun build(): KotlinFile {
            return KotlinFile(this)
        }
    }

    override fun writeTo(tab: String, out: Appendable?) {
        out?.append("package $packageName\n\n")

        //TODO add aliases
        imports.distinctBy { it.canonicalName }
                .sortedBy { it.canonicalName }
                .forEach {
                    check(SourceVersion.isName(it.canonicalName), "not a valid name: %s", it.canonicalName)
                    out?.append("import ${it.canonicalName}\n")
                }

        out?.append('\n')

        classes.forEach {
            it.writeTo(out = out)
            out?.append("\n")
        }

        out?.append('\n')

        methods.sortedBy { it.name }
                .forEachIndexed { index, methodSpec ->
                    methodSpec.writeTo(out = out)
                    if (index < methods.size - 1) out?.append("\n")
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
            OutputStreamWriter(Files.newOutputStream(outputPath), Charsets.UTF_8).use({ writer -> writeTo(out = writer) })
        } else {
            check(classes.isNotEmpty(), "No classes or file name found")
            check(SourceVersion.isName(classes[0].name), "not a valid name: %s", classes[0].name)

            val outputPath = outputDirectory.resolve("${classes[0].name}.kt")
            OutputStreamWriter(Files.newOutputStream(outputPath), Charsets.UTF_8).use({ writer -> writeTo(out = writer) })
        }
    }
}