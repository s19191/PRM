val FORBIDDEN = 0
val EXECUTE = 1
val WRITE = 2
val READ = 4

fun main() {
    println(checkPermissions(EXECUTE))
    println(checkPermissions(EXECUTE or WRITE))
    println(checkPermissions(EXECUTE or READ))
    println(checkPermissions(EXECUTE or READ or WRITE))
}

fun checkPermissions(perms: Int): Boolean = perms and (READ or EXECUTE) == (READ or EXECUTE)

//    0b100
//    0b001 or
//    0b101

//    0bXXX
//    0b101 and
//    0b101