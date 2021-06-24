package pl.edu.pja.p03a

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pl.edu.pja.p03a.adapter.FavAdapter
import pl.edu.pja.p03a.databinding.ActivityFavBinding
import pl.edu.pja.p03a.model.News
import pl.edu.pja.p03a.model.NewsToDatabase
import java.time.Instant
import java.time.LocalDateTime
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
            .child("articles")
            .addValueEventListener(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val genericTypeIndicator : GenericTypeIndicator<Map<String, NewsToDatabase>> =
                        object : GenericTypeIndicator<Map<String, NewsToDatabase>>() {}
                    val value = dataSnapshot.getValue(genericTypeIndicator)
                    var newses: MutableList<News> = mutableListOf()
                    value?.forEach {
                        if (it.value.fav) {
                            newses.add(
                                News(
                                    it.key,
                                    it.value.newsTitle,
                                    it.value.description,
                                    it.value.link,
                                    it.value.photo.toUri(),
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(it.value.date),
                                        TimeZone.getDefault().toZoneId()
                                    ),
                                    it.value.read
                                )
                            )
                        }
                    }
                    newses.sortByDescending {
                        it.date
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