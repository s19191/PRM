package pl.edu.pja.p03.adapter

import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.p03.databinding.ItemNewsBinding
import pl.edu.pja.p03.model.News

class NewsItem(private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(news: News) {
        binding.apply {
            newsTitle.text = news.newsTitle
            description.text = news.description
//            photo.setImageDrawable(news.photo)
        }
    }
}