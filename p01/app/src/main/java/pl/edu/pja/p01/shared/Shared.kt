package pl.edu.pja.p01.shared

import pl.edu.pja.p01.model.Expense
import java.util.*

object Shared {
    val expenseList = mutableListOf<Expense>(
        Expense("PJATK", -1500.0, 1L,  Calendar.getInstance().also { calendar -> calendar.set(2021, 2, 4) }.timeInMillis),
        Expense("PJATK", 3000.0, 1L,  Calendar.getInstance().also { calendar -> calendar.set(2021, 3, 4) }.timeInMillis),
        Expense("Praca", -2000.0, 1L,  Calendar.getInstance().also { calendar -> calendar.set(2021, 3, 10) }.timeInMillis)
    )
}