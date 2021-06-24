package pl.edu.pja.p03a

import android.app.Activity
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.edu.pja.p03a.adapter.NewsAdapter
import pl.edu.pja.p03a.api.ApiClient
import pl.edu.pja.p03a.databinding.ActivityMainBinding
import pl.edu.pja.p03a.model.News
import pl.edu.pja.p03a.model.NewsToDatabase
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

const val LOG_REQ = 1

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private val newsAdapter by lazy { NewsAdapter() }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupNewsList()

        if (auth.currentUser == null) {
            startActivityForResult(Intent(this, LogInActivity::class.java), LOG_REQ)
        }
    }

    private fun setupNewsList() {
        binding.newsList.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        executeCall()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun executeCall() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = ApiClient.apiService.getItems()
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    val p = Pattern.compile("<img src=.+>(.+)")
                    content?.channel?.item?.forEach { it ->
                        var ifExists = false
                        FirebaseDatabase
                            .getInstance()
                            .getReference(auth.uid!!)
                            .child("articles")
                            .orderByChild("newsTitle")
                            .equalTo(it.title)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val genericTypeIndicator : GenericTypeIndicator<Map<String, NewsToDatabase>> =
                                        object : GenericTypeIndicator<Map<String, NewsToDatabase>>() {}
                                    val value = dataSnapshot.getValue(genericTypeIndicator)
                                    value?.forEach { it2 ->
                                        if (it2.value.newsTitle == it.title) {
                                            ifExists = true
                                        }
                                    }
                                    if (!ifExists) {
                                        val m = p.matcher(it.description)
                                        var description = it.description
                                        if (m.matches()) {
                                            description = m.group(1)
                                        }
                                        val dTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                                        val date = LocalDateTime.parse(it.date!!, dTF).atZone(ZoneId.systemDefault()).toEpochSecond()
                                        FirebaseDatabase.getInstance()
                                            .getReference(auth.uid!!)
                                            .child("articles")
                                            .push()
                                            .setValue(
                                                NewsToDatabase(
                                                    it.title!!,
                                                    description!!,
                                                    it.link!!,
                                                    it.enclosure?.url!!,
                                                    date,
                                                    read = false,
                                                    fav = false
                                                )
                                            )
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Error: ${error.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
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
                                newses.sortByDescending {
                                    it.date
                                }
                                newsAdapter.newses = newses
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error: ${error.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun signOut(view: View) {
        auth.signOut()
        startActivity(Intent(this, LogInActivity::class.java))
    }

    fun goToFav(view: View) {
        startActivity(Intent(this, FavActivity::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOG_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                executeCall()
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}