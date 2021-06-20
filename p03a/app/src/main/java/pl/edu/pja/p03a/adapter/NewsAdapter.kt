package pl.edu.pja.p03a.adapter

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import pl.edu.pja.p03a.databinding.ItemNewsBinding
import pl.edu.pja.p03a.model.News
import pl.edu.pja.p03a.shared.Shared

class NewsAdapter : RecyclerView.Adapter<NewsItem>() {
    var newses: List<News> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItem {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsItem(binding)
            .also { holder ->
                binding.root.setOnClickListener {
                    val builder = CustomTabsIntent.Builder()
//                    builder.setToolbarColor()
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(
                        parent.context,
                        Uri.parse(Shared.newsList[holder.layoutPosition].link)
                    )
                    holder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"))
                }
                binding.root.setOnLongClickListener {
                    FirebaseDatabase.getInstance()
                        .getReference("favourites")
                        .child("user")
                        .setValue("ala")
//                    FirebaseDatabase.getInstance()
//                .getReference("users")
//                .child("${auth.currentUser?.uid}")
//                .setValue("${auth.currentUser?.email}")
//                .addOnCompleteListener {
//                    println()
//                }
                    Toast.makeText(
                        parent.context,
                        "Dodano do bazy",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnLongClickListener true
                }
            }
    }

    override fun onBindViewHolder(holder: NewsItem, position: Int) {
        holder.bind(newses[position])
    }

    override fun getItemCount(): Int = newses.size
}