package pl.edu.pja.pysznepja.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.pysznepja.databinding.ItemPhotoBinding

class Image(val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root)

class ImageAdapter(val drawables: List<Drawable>): RecyclerView.Adapter<Image>() {
    var selectedItem: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Image {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Image(binding).also { holder ->
            binding.root.setOnClickListener { changeSelection(holder.layoutPosition) }
        }
    }

    override fun onBindViewHolder(holder: Image, position: Int) {
        holder.binding.imageView.setImageDrawable(drawables[position])
        holder.binding.frame.visibility = if (selectedItem == position) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = drawables.size

    fun changeSelection(position: Int) {
        val oldOne = selectedItem
        selectedItem = position
        oldOne?.let {
            notifyItemChanged(it)
        }
        notifyItemChanged(position)
    }

}