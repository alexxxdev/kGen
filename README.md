# kGen
kGen is a Kotlin DSL for generating .kt source files


example:
```kotlin
val file = File("src")
kotlinFile("ru.alexxxdev.sample", "Test") {
    indent("\t")

    import(String::class)

    field("field1", FieldSpec.MUTABLE, FieldSpec.NULLABLE) {
        className(ClassName.get(File::class))
        init { "null" }
    }

    field("field2", FieldSpec.READONLY, FieldSpec.NOTNULL) {
        init { "0" }
    }

    kotlinClass("Class1") {
        modifiers(Modifier.OPEN)

        field("field1", FieldSpec.MUTABLE, FieldSpec.NULLABLE) {
            className(ClassName.get(File::class))
            init { "null" }
        }

        field("field2", FieldSpec.MUTABLE, FieldSpec.NOTNULL) {
            className(ClassName.get(File::class))
            init { "File(\"test.txt\")" }
        }

        method("fun1") {
            modifiers(Modifier.OPEN)
            returns("0", ClassName.get(Int::class))
        }
    }

    kotlinInterface("Interface1") {
        modifiers(Modifier.ABSTRACT)
        method("fun1") {
            returns(".05f", ClassName.get(Float::class))
        }
        method("fun2") {}
    }

    kotlinObject("Object1") {
        modifiers(Modifier.PRIVATE)
        method("fun1") {}
    }

    method("fun1") {
        returns("\"test\"", ClassName.get(String::class))
    }
    method("fun2") {}

}.writeTo(file)
