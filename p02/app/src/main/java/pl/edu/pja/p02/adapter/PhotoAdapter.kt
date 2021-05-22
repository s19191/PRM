package pl.edu.pja.p02.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.p02.DescriptionActivity
import pl.edu.pja.p02.databinding.ItemPhotoBinding
import pl.edu.pja.p02.model.Traveler

class PhotoAdapter() : RecyclerView.Adapter<PhotoItem>() {
    private val handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var travelers: List<Traveler> = emptyList()
        set(value) {
            field = value
            handler.post {
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItem {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoItem(binding)
            .also { holder ->
            binding.root.setOnLongClickListener {
                removeItem(holder.layoutPosition, parent)
                return@setOnLongClickListener true
            }
            binding.root.setOnClickListener {
                parent.context.startActivity(Intent(parent.context, DescriptionActivity::class.java).putExtra("position", holder.layoutPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: PhotoItem, position: Int) {
        holder.bind(travelers[position])
    }

    override fun getItemCount(): Int = travelers.size

    private fun removeItem(position: Int, parent: ViewGroup) {
        val builder = AlertDialog.Builder(parent.context)
        builder.setMessage("Czy na pewno chcesz usunać?")
            .setCancelable(false)
            .setPositiveButton("Tak") { _, _ ->
//                Shared.expenseList.removeAt(position)
                notifyDataSetChanged()
            }
            .setNegativeButton("Nie") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}