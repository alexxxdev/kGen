# kGen
kGen is a Kotlin DSL for generating .kt source files


example:
```kotlin
val file = File("src")
kotlinFile("ru.alexxxdev.sample", "Test") {
        indent = "\t"

        +import(String::class)
        +import(ClassName.get(File::class))

        field("field1", MUTABLE, NULLABLE) {
            className = ClassName.get(File::class)
            "null"
        }

        field("field2") {
            +Modifier.PRIVATE
            "0"
        }

        method("fun2") {
            +import(String::class)
            +"val s = \"123\""
            returns(ClassName.get(String::class)){ "s"  }
        }

        method("fun1", Modifier.INTERNAL) {
            returns{ "\"test\""	}
        }

        kotlinClass("Class1") {
            +Modifier.PRIVATE
            +import(String::class)

            field("field11", MUTABLE, NULLABLE) {
                className = ClassName.get(File::class)
                "null"
            }

            method("fun11") {
                +Modifier.INTERNAL
                returns{ "\"test\""	}
            }
        }

        kotlinInterface("Interface1") {
        }

        kotlinObject("Object1") {
        }

    }.writeTo(file)
