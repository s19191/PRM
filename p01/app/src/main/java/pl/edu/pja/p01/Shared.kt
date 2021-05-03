package pl.edu.pja.p01

import pl.edu.pja.p01.model.Expense
import java.util.*

object Shared {
    val expenseList = mutableListOf<Expense>(
            Expense("Praca", 1000.0, 1L,  Calendar.getInstance().also { calendar -> calendar.set(2021, 3, 3) }.timeInMillis),
            Expense("PJATK", -1500.0, 1L,  Calendar.getInstance().also { calendar -> calendar.set(2021, 3, 4) }.timeInMillis)
    )
}