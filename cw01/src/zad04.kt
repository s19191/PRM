fun main() {
    val months = """Styczeń, Luty, Marzec, Kwiecień, Maj, Czerwiec,
    |Lipiec, Sierpień, Wrzesień, Październik, Listopad,
    |Grudzień""".trimMargin()
    println(months)

    val monthList = months.split(""",\s+""".toRegex())
    println(monthList)

    for (i in 0..monthList.size -1) {
        print("${monthList[i]}")
    }
    println()

    for (i in 0 until monthList.size) {
        print("${monthList[i]}")
    }
    println()

    for (i in monthList.indices) {
        print("${monthList[i]}")
    }
    println()

    for (m in monthList) {
        if (m.startsWith("L")) print("$m")
    }
    println()

    monthList.forEachIndexed {index, m ->
        if (index % 2 == 0) print("$m")
    }
    println()

    val iter = monthList.iterator()
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
    rec(monthList)
    println()

    println(monthList.joinToString(".",";",";",3))

    monthList.filter { !it.startsWith("P") }
        .map { it.replace('e','_') }
        .sortedBy { it[0] }
        .forEach { print("$it") }
}