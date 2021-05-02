package pl.edu.pja.p01.model

data class Expense(
        val place: String,
        val amount: Double,
        val category: Long,
        val date: Long
)
