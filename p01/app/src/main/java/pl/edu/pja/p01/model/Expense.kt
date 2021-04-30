package pl.edu.pja.p01.model

import java.util.*


data class Expense(
        val place: String,
        val amount: Double,
        val category: String,
        val date: Long
)
