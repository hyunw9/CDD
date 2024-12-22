fun sum(x: Int, y: Int): Int = x + y

val sumLambda: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
fun main() {
    val persons = listOf(Person(1, "baby"), Person(2, "senior"))

    println(persons.maxByOrNull({ person: Person -> person.age }))
    println(persons.maxByOrNull { person: Person -> person.age })
    println(persons.maxByOrNull { person -> person.age })
    println(persons.maxByOrNull { it.age })
    println(persons.maxByOrNull(Person::age))

    val personList = listOf(
        Person(1, "baby"),
        Person(1, "baby"),
        Person(2, "father"),
        Person(2, "father"),
        Person(3, "mother"),
        Person(3, "mother"),
        Person(4, "grandfather"),
    )

    println(personList.filter { it.age > 2 })
    println(
        personList.filter { it.age > 3 }
            .map { "${it.name}" + " is here" }
    )

    println(personList.any {it.age>50})
    println(personList.all{it.age>0})
    println(personList.groupBy{it.age})
    println(personList.distinct())

    val strings = listOf("abc","def")

    println(strings.map { it.toList() })
    println(strings.map { it.toList() }
        .flatten())
    println(strings.flatMap { it.toList() })

//    타입 추론, it, 유연함
}

data class Person(
    val age: Int,
    val name: String
)
