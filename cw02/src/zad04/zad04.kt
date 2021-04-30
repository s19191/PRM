package zad04

fun main() {
    var p = Permission()
    println(p)

    p++
    println(p)

    p--
    println(p)

    p += EXECUTE
    p += READ
    println(p)

    p -= EXECUTE
    println(p)

    println(READ in p)
    try {
        p += 123
    } catch (e: IllegalArgumentException) {
        System.err.println(e)
    }
}