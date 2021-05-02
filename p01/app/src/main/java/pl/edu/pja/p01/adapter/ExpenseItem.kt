package pl.edu.pja.p01.adapter

import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.p01.R
import pl.edu.pja.p01.databinding.ItemExpenseBinding
import pl.edu.pja.p01.model.Expense
import java.text.DateFormat
import java.util.*

class ExpenseItem(private val binding: ItemExpenseBinding): RecyclerView.ViewHolder(binding.root) {
    private val calendar = Calendar.getInstance()

    fun bind(expense: Expense) {
        binding.apply {
            place.text = expense.place
            amount.text = expense.amount.toString()

            category.text = root.resources.getStringArray(R.array.categories_array)[expense.category.toInt()]

            calendar.timeInMillis = expense.date
            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
            date.text = dateFormatter.format(calendar.time)
        }
    }
}