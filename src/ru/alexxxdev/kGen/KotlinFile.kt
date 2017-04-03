package ru.alexxxdev.kGen

import ru.alexxxdev.kGen.ClassSpec.Kind
import ru.alexxxdev.kGen.FieldSpec.PropertyType
import ru.alexxxdev.kGen.FieldSpec.PropertyType.READONLY
import ru.alexxxdev.kGen.FieldSpec.ValueType
import ru.alexxxdev.kGen.FieldSpec.ValueType.NOTNULL
import java.io.File
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path
import javax.lang.model.SourceVersion

/**
 * Created by alexxxdev on 28.02.17.
 */
class KotlinFile(val packageName: String, val fileName: String? = null) : IAppendable {
    private var imports = mutableListOf<TypeName>()
    private var classes = mutableListOf<ClassSpec>()
    private var fields = mutableListOf<FieldSpec>()
    private var methods = mutableListOf<MethodSpec>()

    var indent = "\t"

    operator fun ClassName.unaryPlus() {
        imports.add(this)
    }

    fun kotlinClass(className: String, init: ClassSpec.() -> Unit) = addClass(ClassSpec(Kind.CLASS, className), init)

    fun kotlinInterface(className: String, init: ClassSpec.() -> Unit) = addClass(ClassSpec(Kind.INTERFACE, className), init)

    fun kotlinObject(className: String, init: ClassSpec.() -> Unit) = addClass(ClassSpec(Kind.OBJECT, className), init)

    fun field(name: String, propertyType: PropertyType = READONLY, valueType: ValueType = NOTNULL, init: FieldSpec.() -> String) = addField(FieldSpec(name, propertyType, valueType), init)

    fun method(name: String, vararg mods: Modifier, init: MethodSpec.() -> Unit) = addMethod(MethodSpec(name), *mods, init = init)

    private fun addClass(classSpec: ClassSpec, init: ClassSpec.() -> Unit): ClassSpec {
        classSpec.init()
        classSpec.build()
        classes.add(classSpec)
        return classSpec
    }

    private fun addField(fieldSpec: FieldSpec, init: FieldSpec.() -> String): FieldSpec {
        fieldSpec.initializer = fieldSpec.init()
        fieldSpec.build()
        fields.add(fieldSpec)
        return fieldSpec
    }

    private fun addMethod(methodSpec: MethodSpec, vararg mods: Modifier, init: MethodSpec.() -> Unit): MethodSpec {
        methodSpec.init()
        mods.forEach { methodSpec.addModificator(it) }
        methodSpec.build()
        methods.add(methodSpec)
        return methodSpec
    }

    fun build() {
        classes.forEach {
            imports.addAll(it.listImports)
        }

        methods.forEach {
            imports.addAll(it.listImports)
        }

        fields.forEach {
            imports.addAll(it.listImports)
        }
    }

    override fun writeTo(codeWriter: CodeWriter) {
        codeWriter.out("package $packageName")
        codeWriter.out("\n\n")

        //TODO add aliases
        imports.map { it as ClassName }
                .distinctBy { it.canonicalName }
                .sortedBy { it.canonicalName }
                .forEach {
                    check(SourceVersion.isName(it.canonicalName), "not a valid name: %s", it.canonicalName)
                    codeWriter.out("import ${it.canonicalName}\n")
                }

        codeWriter.out("\n")

        fields.sortedBy { it.name }
                .forEach {
                    codeWriter.out("\n")
                    it.writeTo(codeWriter)
                }


        classes.forEach {
            codeWriter.out("\n\n")
            it.writeTo(codeWriter)
        }


        methods.sortedBy { it.name }
                .forEach {
                    codeWriter.out("\n\n")
                    it.writeTo(codeWriter)
                }
    }

    fun writeTo(out: Appendable?) {
        val codeWriter = CodeWriter(out, indent)
        writeTo(codeWriter)
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