package pl.edu.pja.p03a.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.p03a.databinding.ItemNewsBinding
import pl.edu.pja.p03a.model.News

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
//            .also { holder ->
//            binding.root.setOnLongClickListener {
//                removeItem(holder.layoutPosition, parent)
//                return@setOnLongClickListener true
//            }
//            binding.root.setOnClickListener {
//                parent.context.startActivity(Intent(parent.context, AddActivity::class.java).putExtra("position", holder.layoutPosition))
//            }
//        }
    }

    override fun onBindViewHolder(holder: NewsItem, position: Int) {
        holder.bind(newses[position])
    }

    override fun getItemCount(): Int = newses.size

//    private fun removeItem(position: Int, parent: ViewGroup) {
//        val builder = AlertDialog.Builder(parent.context)
//        builder.setMessage("Czy na pewno chcesz usunać?")
//            .setCancelable(false)
//            .setPositiveButton("Tak") { _, _ ->
//                Shared.expenseList.removeAt(position)
//                notifyDataSetChanged()
//            }
//            .setNegativeButton("Nie") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .create()
//            .show()
//    }
}