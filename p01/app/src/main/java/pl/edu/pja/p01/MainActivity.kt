package pl.edu.pja.p01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pja.p01.adapter.ExpenseAdapter
import pl.edu.pja.p01.databinding.ActivityMainBinding
import java.util.*

const val REQ = 1

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val expenseAdapter by lazy { ExpenseAdapter() }
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addButton.setOnClickListener {
//            startActivity(Intent(this, AddActivity::class.java))
            startActivityForResult(Intent(this, AddActivity::class.java), REQ)
        }


        setupExpenseList()
    }

    private fun setupExpenseList() {
        binding.expenseList.apply {
            adapter = expenseAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        expenseAdapter.expenses = Shared.expenseList
        updateBalance()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        updateBalance()
    }

    fun updateBalance() {
        var sum = 0.0;
        for (i in Shared.expenseList) {
            calendar.timeInMillis = i.date
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
            val expenseDateMonth = calendar.get(Calendar.MONTH) + 1
            if (currentMonth == expenseDateMonth) {
                sum += i.amount
            }
        }
        binding.balance.text = sum.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ) {
            if (resultCode == Activity.RESULT_OK) {
                updateBalance()
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}