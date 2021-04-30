fun main() {
    val int = 12
    val int2 = 0x12
    val int3 = 0b1010
    val int4 = -3

    val byte: Byte = 12
    val byte2: Byte = -12

    val short: Short = 13

    val long = 1L
    val long2 = 100_000_000_000_000_000

    val float = 13.5F
    val float2 = 13.5f

    val double = 12.0
    val double2 = .1
    val double3 = .1e10

    val char = 'a'
    val char2 = '\n'
    val char3 = '\u0010'

//    val double0: Double = int
//    val long0: Long = int
//    val char0: Char = int

    val double0: Double = int.toDouble()
    val long0: Long = int.toLong()
    val char0: Char = int.toChar()
}