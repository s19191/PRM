package zad01

data class Person (
    val name: String,
    val surname: String,
    val sex: Sex
) {
    override fun toString(): String = "$name;$surname;$sex"
}