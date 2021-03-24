package zad04

val FORBIDDEN = 0
val EXECUTE = 1
val WRITE = 2
val READ = 4

class Permission(private var permissions: Int = FORBIDDEN) {
    private val allowed = listOf(FORBIDDEN, EXECUTE, WRITE, READ)

    operator fun inc(): Permission = apply { this += WRITE }

    operator fun dec(): Permission = apply { this -= WRITE }

    operator fun contains(perm: Int): Boolean {
        requireAllowed(perm)
        return permissions and perm == perm
    }

    operator fun plusAssign(perm: Int) {
        requireAllowed(perm)
        permissions = permissions or perm
    }

    operator fun minusAssign(perm: Int) {
        requireAllowed(perm)
        if (perm in this) permissions = permissions xor perm
    }

    private fun requireAllowed(perm: Int) {
        if (perm !in allowed) throw IllegalArgumentException("Permission not allowed")
    }

    override fun toString(): String {
        return permissions.toString()
    }
}