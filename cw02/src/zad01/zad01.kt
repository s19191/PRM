package zad01

fun main() {
    val people = mutableListOf(
        Person("Jan", "Kwasowski", Sex.MALE),
        Person("Adam", "Jakub", Sex.MALE)
    );
    people += "Anita;Maria;FEMALE".toPerson()
    println(people)

    val (name, surname, sex) = people[0]
    println("$name -> $sex")
}

fun String.toPerson(): Person {
    val parts = this.split(";")
    return Person(parts[0], parts[1], Sex.valueOf(parts[2]))
}