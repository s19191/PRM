package pl.edu.pja.p01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pja.p01.adapter.ExpenseAdapter
import pl.edu.pja.p01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val expenseAdapter by lazy { ExpenseAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
//            startActivityForResult(Intent(this, AddActivity::class.java), REQ)
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
    }
}