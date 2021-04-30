package pl.edu.pja.p01.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            category.text = expense.category

            calendar.timeInMillis = expense.date
            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
            date.text = dateFormatter.format(calendar.time)
        }
    }
}

class ExpenseAdapter(): RecyclerView.Adapter<ExpenseItem>() {
    var expenses: List<Expense> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseItem {
        val binding = ItemExpenseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return ExpenseItem(binding)
    }

    override fun onBindViewHolder(holder: ExpenseItem, position: Int) {
        holder.bind(expenses[position])
    }

    override fun getItemCount(): Int = expenses.size
}