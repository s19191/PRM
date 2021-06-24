package pl.edu.pja.p03a.adapter

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pl.edu.pja.p03a.databinding.ItemNewsBinding
import pl.edu.pja.p03a.model.News

class NewsItem(private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(itemNews: News) {
        binding.apply {
            newsTitle.text = itemNews.newsTitle
            description.text = itemNews.description
            Picasso.get().load(itemNews.photo).into(photo)
            if (itemNews.read) {
                news.setBackgroundColor(Color.parseColor("#CCCCCC"))
                    //TODO: Czemu tak?
            } else {
                news.setBackgroundColor(Color.WHITE)
            }
        }
    }
}