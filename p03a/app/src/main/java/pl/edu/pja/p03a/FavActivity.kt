package pl.edu.pja.p03a

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pl.edu.pja.p03a.adapter.FavAdapter
import pl.edu.pja.p03a.databinding.ActivityFavBinding
import pl.edu.pja.p03a.model.News
import pl.edu.pja.p03a.model.NewsToDatabase
import java.util.*

class FavActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFavBinding.inflate(layoutInflater) }
    private val favAdapter by lazy { FavAdapter() }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        setupFavList()
    }

    private fun setupFavList() {
        binding.newsList.apply {
            adapter = favAdapter
            layoutManager = LinearLayoutManager(context)
        }
        FirebaseDatabase
            .getInstance()
            .getReference(auth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val genericTypeIndicator : GenericTypeIndicator<Map<String, NewsToDatabase>> =
                        object : GenericTypeIndicator<Map<String, NewsToDatabase>>() {}
                    val value = dataSnapshot.child("fav").getValue(genericTypeIndicator)
                    var newses: MutableList<News> = mutableListOf()
                    value?.forEach {
                        newses.add(
                            News(
                                it.value.newsTitle,
                                it.value.description,
                                it.value.link,
                                it.value.photo.toUri()
                            )
                        )
                    }
                    favAdapter.newses = newses
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@FavActivity,
                        "Error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    fun returnToAll(view: View) {
        finish()
    }
}