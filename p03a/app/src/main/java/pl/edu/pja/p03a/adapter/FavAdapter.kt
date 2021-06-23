package pl.edu.pja.p03a.adapter

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.p03a.databinding.ItemNewsBinding
import pl.edu.pja.p03a.model.News
import pl.edu.pja.p03a.shared.Shared

class FavAdapter : RecyclerView.Adapter<NewsItem>(){
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
                    builder.setToolbarColor(Color.parseColor("#6200EE"))
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(
                        parent.context,
                        Uri.parse(Shared.newsList[holder.layoutPosition].link)
                    )
                    holder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"))
                }
            }
    }

    override fun onBindViewHolder(holder: NewsItem, position: Int) {
        holder.bind(newses[position])
    }

    override fun getItemCount(): Int = newses.size
}