package pl.edu.pja.p02.adapter

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.p02.MainActivity
import pl.edu.pja.p02.PhotoShowActivity
import pl.edu.pja.p02.Shared
import pl.edu.pja.p02.databinding.ItemPhotoBinding
import pl.edu.pja.p02.model.Traveler
import kotlin.concurrent.thread

class GalleryAdapter(private val mainActivity: MainActivity) : RecyclerView.Adapter<PhotoItem>() {
    private val handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var travelers: MutableList<Traveler> = mutableListOf()
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
                parent.context.startActivity(Intent(parent.context, PhotoShowActivity::class.java)
                    .putExtra("itemId", travelers[holder.layoutPosition].id)
                )
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
                thread {
                    Shared.db?.travelers?.delete(travelers[position].id)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        parent.context.contentResolver.delete(travelers[position].photoUri, mainActivity.intent.extras)
                    }
                    travelers.removeAt(position)
                    mainActivity.runOnUiThread{
                        notifyDataSetChanged()
                    }
//                    mainActivity.geofencingClient.removeGeofences()
                }
            }
            .setNegativeButton("Nie") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}