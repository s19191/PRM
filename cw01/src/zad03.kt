import java.util.*

fun main() {
    val orginal = "kot"
    var refs: String = orginal
    println(orginal == refs)
    println(orginal === refs)

    val sc = Scanner(System.`in`)
    refs = sc.next()
    println(orginal == refs)
    println(orginal === refs)
}