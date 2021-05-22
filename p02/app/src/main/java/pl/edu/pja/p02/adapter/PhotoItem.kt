package pl.edu.pja.p02.adapter

import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.p02.databinding.ItemPhotoBinding
import pl.edu.pja.p02.model.Traveler
import pl.edu.pja.p02.model.TravelerDto

class PhotoItem(private val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(traveler: Traveler) {
        binding.apply {
            photoView.setImageBitmap(traveler.photoBitmap)
        }
    }
} 