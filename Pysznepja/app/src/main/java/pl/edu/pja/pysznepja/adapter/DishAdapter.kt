package pl.edu.pja.pysznepja.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pja.pysznepja.databinding.ItemDishBinding
import pl.edu.pja.pysznepja.model.Dish

class DishItem(val binding: ItemDishBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(dish: Dish) {
        binding.apply {
            name.text = dish.name
            ingredients.text = dish.ingredients.joinToString()
            photo.setImageDrawable(dish.drawable)
        }
    }
}

class DishAdapter(): RecyclerView.Adapter<DishItem>() {
    var dishes: List<Dish> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishItem {
        val binding = ItemDishBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DishItem(binding)
    }

    override fun onBindViewHolder(holder: DishItem, position: Int) {
        holder.bind(dishes[position])
    }

    override fun getItemCount(): Int = dishes.size
}