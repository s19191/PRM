package pl.edu.pja.p01

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pja.p01.databinding.ActivityAddBinding
import pl.edu.pja.p01.model.Expense
import java.util.*

class AddActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val spinner = binding.categoriesSpinner
        ArrayAdapter.createFromResource(
                this,
                R.array.categories_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        binding.date.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(year,month,dayOfMonth)
            binding.date.date = calendar.timeInMillis
        }
    }

    fun onSave(view: View) {
        val place = binding.place.text.toString()
        val amount = binding.amount.text.toString()?.toDouble()
        val category = binding.categoriesSpinner.selectedItem.toString()
        val date =binding.date.date
        if (place.isEmpty()) {
            Toast.makeText(this, "Nie wpisałeś miejsca", Toast.LENGTH_LONG).show()
            return
        }
        if (amount == null) {
            Toast.makeText(this, "Nie wpisałeś kwoty", Toast.LENGTH_LONG).show()
            return
        }
        if (category.isEmpty()) {
            Toast.makeText(this, "Nie wybrałeś kategorii", Toast.LENGTH_LONG).show()
            return
        }
        if (date == null) {
            Toast.makeText(this, "Nie wybrałeś daty", Toast.LENGTH_LONG).show()
            return
        }
        val expense = Expense(place, amount, category, date)
        Shared.expenseList.add(expense)
        finish()
    }
}