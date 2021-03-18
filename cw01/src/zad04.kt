fun main() {
    val months = """Styczeń, Luty, Marzec, Kwiecień, Maj, Czerwiec,
    |Lipiec, Sierpień, Wrzesień, Październik, Listopad,
    |Grudzień""".trimMargin()
    println(months)

    val monthsList = months.split(""",\s+""".toRegex())
    println(monthsList)

    for (i in 0..monthsList.size -1) {
        print("${monthsList[i]}")
    }
    println()

    for (i in 0 until monthsList.size) {
        print("${monthsList[i]}")
    }
    println()

    for (i in monthsList.indices) {
        print("${monthsList[i]}")
    }
    println()

    for (m in monthsList) {
        if (m.startsWith("L")) print("$m")
    }
    println()

    monthsList.forEachIndexed { index, m ->
        if (index % 2 == 0) print("$m")
    }
    println()

    val iter = monthsList.iterator()
    while (iter.hasNext()) {
        print("${iter.next()}")
    }
    println()

    fun rec(mounth: List<String>) {
        tailrec fun rec(i: Int) {
            print("${mounth[i]}")
            val nextI = i + 1
            if (nextI < mounth.size)  rec(nextI)
        }
        rec(0)
    }
    rec(monthsList)
    println()

    println(monthsList.joinToString(".",";",";",3))

    monthsList.filter { !it.startsWith("P") }
        .map { it.replace('e','_') }
        .sortedBy { it[0] }
        .forEach { print("$it") }
}