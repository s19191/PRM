package pl.edu.pja.p01.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.p01.AddActivity
import pl.edu.pja.p01.Shared
import pl.edu.pja.p01.databinding.ItemExpenseBinding
import pl.edu.pja.p01.model.Expense

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
        return ExpenseItem(binding).also { holder ->
            binding.root.setOnLongClickListener {
                removeItem(holder.layoutPosition, parent)
                return@setOnLongClickListener true
            }
            binding.root.setOnClickListener {
                parent.context.startActivity(Intent(parent.context, AddActivity::class.java).putExtra("position", holder.layoutPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: ExpenseItem, position: Int) {
        holder.bind(expenses[position])
    }

    override fun getItemCount(): Int = expenses.size

    private fun removeItem(position: Int, parent: ViewGroup) {
        val builder = AlertDialog.Builder(parent.context)
        builder.setMessage("Czy na pewno chcesz usunać?")
                .setCancelable(false)
                .setPositiveButton("Tak") { _, _ ->
                    Shared.expenseList.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("Nie") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }
}