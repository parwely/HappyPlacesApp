package com.example.happyplacesapp.ui.fragements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplacesapp.R
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDatabase
import com.example.happyplacesapp.data.repository.HappyPlaceRepository
import com.example.happyplacesapp.databinding.FragmentPlacesListBinding
import com.example.happyplacesapp.ui.adapters.PlacesAdapter
import com.example.happyplacesapp.ui.viewmodels.PlacesViewModel
import com.example.happyplacesapp.ui.viewmodels.PlacesViewModelFactory
import kotlinx.coroutines.launch

class PlacesListFragment : Fragment() {

    private var _binding: FragmentPlacesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var placesAdapter: PlacesAdapter

    private val viewModel: PlacesViewModel by viewModels {
        val database = HappyPlaceDatabase.getDatabase(requireContext())
        val repository = HappyPlaceRepository(database.happyPlaceDao())
        PlacesViewModelFactory(repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlacesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        placesAdapter = PlacesAdapter { place ->
            onPlaceClick(place)
        }

        binding.recyclerViewPlaces.apply {
            adapter = placesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.fabAddPlace.setOnClickListener {
            findNavController().navigate(R.id.action_placesListFragment_to_addPlaceActivity)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.places.collect { places ->
                placesAdapter.submitList(places)
                binding.textViewEmpty.visibility = if (places.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun onPlaceClick(place: HappyPlace) {
        // TODO: Navigation zu PlaceDetail oder EditPlace
        findNavController().navigate(R.id.action_placesListFragment_to_mapFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}