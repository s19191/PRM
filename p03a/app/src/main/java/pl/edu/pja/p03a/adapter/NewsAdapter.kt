package pl.edu.pja.p03a.adapter

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pl.edu.pja.p03a.databinding.ItemNewsBinding
import pl.edu.pja.p03a.model.News
import pl.edu.pja.p03a.model.NewsToDatabase
import java.util.*

class NewsAdapter : RecyclerView.Adapter<NewsItem>() {
    var newses: List<News> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItem {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        auth = FirebaseAuth.getInstance()
        return NewsItem(binding)
            .also { holder ->
                binding.root.setOnClickListener {
                    FirebaseDatabase
                        .getInstance()
                        .getReference(auth.uid!!)
                        .child("articles")
                        .child(newses[holder.layoutPosition].key)
                        .child("read")
                        .setValue(true)
                    newses[holder.layoutPosition].read = true
                    val builder = CustomTabsIntent.Builder()
                    builder.setToolbarColor(Color.parseColor("#6200EE"))
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(
                        parent.context,
                        Uri.parse(newses[holder.layoutPosition].link)
                    )
                    binding.news.setBackgroundColor(Color.parseColor("#CCCCCC"))
                }
                binding.root.setOnLongClickListener {
                    FirebaseDatabase
                        .getInstance()
                        .getReference(auth.uid!!)
                        .child("articles")
                        .child(newses[holder.layoutPosition].key)
                        .child("fav")
                        .setValue(true)
                        .addOnSuccessListener {
                            Toast.makeText(
                                parent.context,
                                "Dodano do ulubionych!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    return@setOnLongClickListener true
                }
            }
    }

    override fun onBindViewHolder(holder: NewsItem, position: Int) {
        holder.bind(newses[position])
    }

    override fun getItemCount(): Int = newses.size
}