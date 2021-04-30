import java.util.*
import kotlin.random.Random

fun main() {
    val months = """Styczeń, Luty, Marzec, Kwiecień, Maj, Czerwiec,
    |Lipiec, Sierpień, Wrzesień, Październik, Listopad,
    |Grudzień""".trimMargin()
    val monthsList = months.split(""",\s+""".toRegex())

    val randomMonth = monthsList.random().also { print("+ $it: ") }
    val ferie = listOf("luty", "lipiec", "sierpień", "wrzesień")

    fun getMonthType(monthName: String) =
        when (monthName.toLowerCase(Locale.forLanguageTag("PL"))) {
            in ferie -> "Ferie"
            "październik", "listopad", "grudzień", "styczeń" -> "Semestr zimowy"
            "marzec", "kwiecień", "maj", "czerwiec" -> "Semestr letni"
            else -> "Nie ma takiego miesiąca"
        }
    println(getMonthType(randomMonth))

    val randomMonths = Array(Random.nextInt(monthsList.size)) { monthsList.random() }
        .also { println("+ ${it.joinToString()}") }

    //fun getMonthsTypes(vararg monthNames: String) = monthNames.associateWith { getMonthType(it) }
    fun getMonthsTypes(vararg monthNames: String) = monthNames.associateWith(::getMonthType)
    println(getMonthsTypes(*randomMonths))
}