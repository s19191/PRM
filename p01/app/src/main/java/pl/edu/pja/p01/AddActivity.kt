package pl.edu.pja.p01

import android.app.Activity
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
    var editItemPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setResult(Activity.RESULT_CANCELED)

        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
           editItemPosition = bundle.getInt("position")
        }

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

        if (editItemPosition != -1) {
            val editExpenseItem = Shared.expenseList[editItemPosition]
            binding.place.setText(editExpenseItem.place)
            binding.amount.setText(editExpenseItem.amount.toString())
            binding.date.date = editExpenseItem.date
            binding.categoriesSpinner.setSelection(editExpenseItem.category.toInt())
            binding.saveButton.text = "Zedytuj"
        }
    }

    fun onSave(view: View) {
        val place = binding.place.text.toString()
        val amount = binding.amount.text.toString()
        val category = binding.categoriesSpinner.selectedItemId
        val date = binding.date.date
        if (place.isEmpty()) {
            Toast.makeText(this, "Nie wpisałeś miejsca", Toast.LENGTH_LONG).show()
            return
        }
        if (amount.isEmpty()) {
            Toast.makeText(this, "Nie wpisałeś kwoty", Toast.LENGTH_LONG).show()
            return
        }
        if (category == null) {
            Toast.makeText(this, "Nie wybrałeś kategorii", Toast.LENGTH_LONG).show()
            return
        }
        if (date == null) {
            Toast.makeText(this, "Nie wybrałeś daty", Toast.LENGTH_LONG).show()
            return
        }
        val expense = Expense(place, amount.toDouble(), category, date)
        if (editItemPosition == -1) {
            Shared.expenseList.add(expense)
        } else {
            Shared.expenseList.removeAt(editItemPosition)
            Shared.expenseList.add(editItemPosition, expense)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}