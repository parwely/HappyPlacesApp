package com.example.happyplacesapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.databinding.ItemPlaceBinding

class PlacesAdapter(private val onItemClick: (HappyPlace) -> Unit) :
    ListAdapter<HappyPlace, PlacesAdapter.PlaceViewHolder>(PlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaceViewHolder(private val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(place: HappyPlace) {
            binding.apply {
                textViewTitle.text = place.title
                textViewDescription.text = place.description
                textViewLocation.text = place.location
                textViewCategory.text = place.category

                root.setOnClickListener {
                    onItemClick(place)
                }
            }
        }
    }
}

class PlaceDiffCallback : DiffUtil.ItemCallback<HappyPlace>() {
    override fun areItemsTheSame(oldItem: HappyPlace, newItem: HappyPlace): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HappyPlace, newItem: HappyPlace): Boolean {
        return oldItem == newItem
    }
}