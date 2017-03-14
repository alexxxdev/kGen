# kGen
kGen is a Kotlin DSL for generating .kt source files


example:
```kotlin
val file = File("src")
    kotlinFile("ru.alexxxdev.sample", "Test") {
        indent("\t")

        import(String::class)

        field("test3", FieldSpec.MUTABLE, FieldSpec.NULLABLE) {
            className(ClassName.get(File::class))
            init { "null" }
        }
        field("test2", FieldSpec.READONLY, FieldSpec.NOTNULL) {
            init { "0" }
        }

        kotlinClass("Test1") {
            modifiers(Modifier.OPEN)

            field("test", FieldSpec.MUTABLE, FieldSpec.NULLABLE) {
                className(ClassName.get(File::class))
                init { "null" }
            }

            field("abs", FieldSpec.MUTABLE, FieldSpec.NOTNULL) {
                className(ClassName.get(File::class))
                init { "File()" }
            }

            method("ww1") {
                modifiers(Modifier.OPEN)
                returns("0", ClassName.get(Int::class))
            }
        }

        kotlinInterface("Test2") {
            modifiers(Modifier.ABSTRACT)
            method("ww1") {
                returns(".05f", ClassName.get(Float::class))
            }
            method("ww2") {}
        }

        kotlinObject("Test3") {
            modifiers(Modifier.PRIVATE)
            method("ww1") {}
        }

        method("ww1") {
            returns("\"test\"", ClassName.get(String::class))
        }
        method("ww2") {}

    }.writeTo(file)
