package pl.edu.pja.pysznepja

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
//import androidx.activity.result.ActivityResultCallback
//import androidx.activity.result.contract.ActivityResultContract
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.result.registerForActivityResult
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pja.pysznepja.adapter.DishAdapter
import pl.edu.pja.pysznepja.databinding.ActivityMainBinding

const val REQ = 1;

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dishAdapter by lazy { DishAdapter() }

//    val callback = ActivityResultCallback<Any> {
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
//            .launch()

        setContentView(binding.root)

        Shared.dishList
        setupDishList()

        binding.button.setOnClickListener {
            println("code")
//            startActivity(Intent(this, AddActivity::class.java))
            startActivityForResult(Intent(this, AddActivity::class.java), REQ)
        }
    }

    private fun setupDishList() {
        binding.dishlist.apply {
            adapter = dishAdapter
            layoutManager = LinearLayoutManager(context)
        }
        dishAdapter.dishes = Shared.dishList
    }

    override fun onResume() {
        super.onResume()
        dishAdapter.dishes = Shared.dishList
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ) {
            if (resultCode == Activity.RESULT_OK) {
                data?.getStringExtra("name").let {
                    binding.button.text = it
                }
                dishAdapter.dishes = Shared.dishList
            } else {
                binding.button.visibility = View.GONE
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}