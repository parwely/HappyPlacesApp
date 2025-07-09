package com.example.happyplacesapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDatabase
import com.example.happyplacesapp.data.repository.HappyPlaceRepository
import com.example.happyplacesapp.databinding.FragmentPlacesListBinding
import com.example.happyplacesapp.ui.adapters.PlacesAdapter
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModel
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class PlacesListFragment : Fragment() {

    private var _binding: FragmentPlacesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var placesAdapter: PlacesAdapter
    private var selectedPlace: HappyPlace? = null

    private val viewModel: HappyPlaceViewModel by viewModels {
        HappyPlaceViewModelFactory(
            HappyPlaceRepository(
                HappyPlaceDatabase.getDatabase(requireContext()).happyPlaceDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlacesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFABs()
        observePlaces()
    }

    private fun setupRecyclerView() {
        placesAdapter = PlacesAdapter { place ->
            onPlaceClick(place)
        }

        binding.recyclerViewPlaces.apply {
            adapter = placesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Swipe to delete functionality
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val place = placesAdapter.getPlaceAt(position)
                    showDeleteConfirmationDialog(place)
                } else {
                    // Fallback falls Position ungültig ist
                    placesAdapter.notifyDataSetChanged()
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerViewPlaces)
    }

    private fun setupFABs() {
        binding.fabAddPlace.setOnClickListener {
            navigateToAddPlace()
        }

        binding.fabEditPlace.setOnClickListener {
            selectedPlace?.let { place ->
                navigateToEditPlace(place.id)
            } ?: run {
                Toast.makeText(requireContext(), "Bitte wählen Sie einen Ort zum Bearbeiten", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fabDeletePlace.setOnClickListener {
            selectedPlace?.let { place ->
                showDeleteConfirmationDialog(place)
            } ?: run {
                Toast.makeText(requireContext(), "Bitte wählen Sie einen Ort zum Löschen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observePlaces() {
        viewModel.allPlaces.observe(viewLifecycleOwner) { places ->
            if (places.isEmpty()) {
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.recyclerViewPlaces.visibility = View.GONE
            } else {
                binding.textViewEmpty.visibility = View.GONE
                binding.recyclerViewPlaces.visibility = View.VISIBLE
                placesAdapter.submitList(places)
            }
        }
    }

    private fun onPlaceClick(place: HappyPlace) {
        selectedPlace = place
        placesAdapter.setSelectedPlace(place)

        // Zeige Toast zur Bestätigung der Auswahl
        Toast.makeText(requireContext(), "'${place.title}' ausgewählt", Toast.LENGTH_SHORT).show()

        // NICHT automatisch zur Karte navigieren
        // Benutzer kann jetzt Edit/Delete-Buttons verwenden
    }

    private fun navigateToAddPlace() {
        val action = PlacesListFragmentDirections.actionPlacesListFragmentToAddPlaceFragment(
            placeId = -1L,
            latitude = 0.0f,
            longitude = 0.0f
        )
        findNavController().navigate(action)
    }

    private fun navigateToEditPlace(placeId: Long) {
        val action = PlacesListFragmentDirections.actionPlacesListFragmentToAddPlaceFragment(
            placeId = placeId,
            latitude = 0.0f,
            longitude = 0.0f
        )
        findNavController().navigate(action)
    }

    private fun showDeleteConfirmationDialog(place: HappyPlace) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Ort löschen")
            .setMessage("Möchten Sie '${place.title}' wirklich löschen?")
            .setPositiveButton("Löschen") { _, _ ->
                deletePlace(place)
            }
            .setNegativeButton("Abbrechen") { dialog, _ ->
                dialog.dismiss()
                placesAdapter.notifyDataSetChanged() // Refresh to undo swipe
            }
            .show()
    }

    private fun deletePlace(place: HappyPlace) {
        lifecycleScope.launch {
            try {
                viewModel.deletePlace(place)
                Toast.makeText(requireContext(), "'${place.title}' wurde gelöscht", Toast.LENGTH_SHORT).show()

                // Clear selection if deleted place was selected
                if (selectedPlace?.id == place.id) {
                    selectedPlace = null
                    placesAdapter.clearSelection()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Fehler beim Löschen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}