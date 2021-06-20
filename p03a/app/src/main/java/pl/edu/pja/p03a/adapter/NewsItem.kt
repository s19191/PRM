package pl.edu.pja.p03a.adapter

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pl.edu.pja.p03a.databinding.ItemNewsBinding
import pl.edu.pja.p03a.model.News

class NewsItem(private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(news: News) {
        binding.apply {
            newsTitle.text = news.newsTitle
            description.text = news.description
            Picasso.get().load(news.photo).into(photo)
        }
    }
}