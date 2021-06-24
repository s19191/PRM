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
                    var ifExists = false
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
//                        .addValueEventListener(object : ValueEventListener {
//                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                val genericTypeIndicator : GenericTypeIndicator<Map<String, NewsToDatabase>> =
//                                    object : GenericTypeIndicator<Map<String, NewsToDatabase>>() {}
//                                val value = dataSnapshot.getValue(genericTypeIndicator)
//                                value?.forEach {
//                                    if (it.value.newsTitle == newses[holder.layoutPosition].newsTitle!!) {
//                                        ifExists = true
//                                    }
//                                }
//                                if (!ifExists) {
//                                    FirebaseDatabase.getInstance()
//                                        .getReference(auth.uid!!)
//                                        .child("fav")
//                                        .push()
//                                        .setValue(
//                                            NewsToDatabase(
//                                                newses[holder.layoutPosition].newsTitle!!,
//                                                newses[holder.layoutPosition].description!!,
//                                                newses[holder.layoutPosition].link!!,
//                                                newses[holder.layoutPosition].photo.toString(),
//                                                System.currentTimeMillis(),
//                                                newses[holder.layoutPosition].read
//                                            )
//                                        )
//                                        .addOnSuccessListener {
//                                            Toast.makeText(
//                                                parent.context,
//                                                "Dodano do ulubionych!",
//                                                Toast.LENGTH_LONG
//                                            ).show()
//                                        }
//                                } else {
//                                    Toast.makeText(
//                                        parent.context,
//                                        "Ten news jest już w ulubionych",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                            }
//                            override fun onCancelled(error: DatabaseError) {
//                                Toast.makeText(
//                                    parent.context,
//                                    "Error: ${error.message}",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        })
                    return@setOnLongClickListener true
                }
            }
    }

    override fun onBindViewHolder(holder: NewsItem, position: Int) {
        holder.bind(newses[position])
    }

    override fun getItemCount(): Int = newses.size
}