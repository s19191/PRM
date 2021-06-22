package pl.edu.pja.p03a

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.edu.pja.p03a.adapter.NewsAdapter
import pl.edu.pja.p03a.api.ApiClient
import pl.edu.pja.p03a.databinding.ActivityMainBinding
import pl.edu.pja.p03a.model.News
import pl.edu.pja.p03a.shared.Shared
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private val newsAdapter by lazy { NewsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        executeCall()

        setupNewsList()
    }

    private fun setupNewsList() {
        binding.newsList.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        newsAdapter.newses = Shared.newsList
    }

    private fun executeCall() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = ApiClient.apiService.getItems()
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    val p = Pattern.compile("<img src=.+>(.+)")
                    content?.channel?.item?.forEach {
                        val m = p.matcher(it.description)
                        if (m.matches()) {
                            Shared.newsList.add(
                                News
                                    (
                                    it.title,
                                    m.group(1),
                                    it.link,
                                    Uri.parse(it.enclosure?.url)
                                )
                            )
                        } else {
                            Shared.newsList.add(
                                News
                                    (
                                    it.title,
                                    m.group(1),
                                    it.link,
                                    Uri.parse(it.enclosure?.url)
                                )
                            )
                        }
                    }
                    runOnUiThread {
                        newsAdapter.notifyDataSetChanged()
                    }
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
}