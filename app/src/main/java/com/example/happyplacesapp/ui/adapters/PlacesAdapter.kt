package com.example.happyplacesapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.happyplacesapp.R
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.databinding.ItemPlaceBinding
import java.text.SimpleDateFormat
import java.util.*

class PlacesAdapter(
    private val onPlaceClick: (HappyPlace) -> Unit
) : ListAdapter<HappyPlace, PlacesAdapter.PlaceViewHolder>(DiffCallback()) {

    private var selectedPlace: HappyPlace? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place, place == selectedPlace)
    }

    fun getPlaceAt(position: Int): HappyPlace = getItem(position)

    fun setSelectedPlace(place: HappyPlace) {
        val previousSelected = selectedPlace
        selectedPlace = place

        // Refresh previous and current selection
        previousSelected?.let { prevPlace ->
            val prevIndex = currentList.indexOf(prevPlace)
            if (prevIndex != -1) notifyItemChanged(prevIndex)
        }

        val currentIndex = currentList.indexOf(place)
        if (currentIndex != -1) notifyItemChanged(currentIndex)
    }

    fun clearSelection() {
        val previousSelected = selectedPlace
        selectedPlace = null

        previousSelected?.let { prevPlace ->
            val prevIndex = currentList.indexOf(prevPlace)
            if (prevIndex != -1) notifyItemChanged(prevIndex)
        }
    }

    inner class PlaceViewHolder(
        private val binding: ItemPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(place: HappyPlace, isSelected: Boolean) {
            binding.apply {
                tvPlaceTitle.text = place.title
                tvPlaceDescription.text = place.description
                tvPlaceLocation.text = place.location.ifEmpty {
                    "Lat: ${String.format("%.4f", place.latitude)}, Lng: ${String.format("%.4f", place.longitude)}"
                }
                tvPlaceCategory.text = place.category

                // Format date
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                tvPlaceDate.text = dateFormat.format(Date(place.dateAdded))

                // Load image
                if (place.imagePath.isNotEmpty()) {
                    Glide.with(ivPlaceImage.context)
                        .load(place.imagePath)
                        .placeholder(R.drawable.ic_image_placeholder)
                        .into(ivPlaceImage)
                } else {
                    ivPlaceImage.setImageResource(R.drawable.ic_image_placeholder)
                }

                // Set category icon
                val categoryIcon = when (place.category) {
                    "Natur" -> R.drawable.ic_nature
                    "Essen" -> R.drawable.ic_restaurant
                    "Sport" -> R.drawable.ic_sports
                    "Kultur" -> R.drawable.ic_culture
                    else -> R.drawable.ic_place
                }
                ivCategoryIcon.setImageResource(categoryIcon)

                // Highlight selection
                cardPlace.isSelected = isSelected
                cardPlace.strokeWidth = if (isSelected) 4 else 1

                root.setOnClickListener {
                    onPlaceClick(place)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<HappyPlace>() {
        override fun areItemsTheSame(oldItem: HappyPlace, newItem: HappyPlace): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HappyPlace, newItem: HappyPlace): Boolean {
            return oldItem == newItem
        }
    }
}
