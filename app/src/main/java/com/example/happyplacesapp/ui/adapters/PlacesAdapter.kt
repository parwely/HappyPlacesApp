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

class PlacesAdapter(
    private val onPlaceClick: (HappyPlace) -> Unit
) : ListAdapter<HappyPlace, PlacesAdapter.PlaceViewHolder>(DiffCallback()) {

    private var selectedPlaceId: Long? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

    fun getPlaceAt(position: Int): HappyPlace {
        return getItem(position)
    }

    fun setSelectedPlace(place: HappyPlace) {
        val previousSelectedId = selectedPlaceId
        selectedPlaceId = place.id

        // Aktualisiere vorherige Auswahl
        if (previousSelectedId != null) {
            val previousIndex = currentList.indexOfFirst { it.id == previousSelectedId }
            if (previousIndex != -1) {
                notifyItemChanged(previousIndex)
            }
        }

        // Aktualisiere neue Auswahl
        val newIndex = currentList.indexOfFirst { it.id == place.id }
        if (newIndex != -1) {
            notifyItemChanged(newIndex)
        }
    }

    fun clearSelection() {
        val previousSelectedId = selectedPlaceId
        selectedPlaceId = null

        if (previousSelectedId != null) {
            val previousIndex = currentList.indexOfFirst { it.id == previousSelectedId }
            if (previousIndex != -1) {
                notifyItemChanged(previousIndex)
            }
        }
    }

    inner class PlaceViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(place: HappyPlace) {
            binding.apply {
                tvPlaceTitle.text = place.title
                tvPlaceDescription.text = place.description
                tvPlaceLocation.text = place.location.ifEmpty {
                    "Lat: ${String.format("%.4f", place.latitude)}, Lng: ${String.format("%.4f", place.longitude)}"
                }

                // Kategorie anzeigen
                if (place.category.isNotEmpty()) {
                    tvPlaceDescription.text = "${place.category} • ${place.description}"
                }

                // Bild laden
                if (place.imagePath.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(place.imagePath)
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .centerCrop()
                        .into(ivPlaceImage)
                } else {
                    ivPlaceImage.setImageResource(R.drawable.ic_image_placeholder)
                }

                // Auswahlstatus
                val isSelected = selectedPlaceId == place.id
                root.isSelected = isSelected
                root.alpha = if (isSelected) 0.8f else 1.0f

                // Click Listener
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