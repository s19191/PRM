fun main() {
    val tempK = 36.6.K
    val tempF = 25.0.F
    println("36C = ${tempK}K")
    println("25C = ${tempF}F")
}

val Double.K: Double
    get() = this + 273.15

val Double.F: Double
    get() = this * 1.8000 + 32.0
