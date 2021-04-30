package pl.edu.pja.p01.model

data class Expense(
        val place: String,
        val amount: Double,
        val category: String,
        val date: Long
)
