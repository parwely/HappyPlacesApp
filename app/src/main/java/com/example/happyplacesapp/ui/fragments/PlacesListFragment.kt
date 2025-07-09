package com.example.happyplacesapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplacesapp.R
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDatabase
import com.example.happyplacesapp.data.repository.HappyPlaceRepository
import com.example.happyplacesapp.databinding.FragmentPlacesListBinding
import com.example.happyplacesapp.ui.adapters.PlacesAdapter
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModelFactory
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModel

class PlacesListFragment : Fragment() {

    private var _binding: FragmentPlacesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var placesAdapter: PlacesAdapter

    private val viewModel: HappyPlaceViewModel by viewModels {
        val database = HappyPlaceDatabase.getDatabase(requireContext())
        val repository = HappyPlaceRepository(database.happyPlaceDao())
        HappyPlaceViewModelFactory(requireActivity().application)
    }

    private var selectedPlace: HappyPlace? = null

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
            selectedPlace = place
            onPlaceClick(place)
        }
        binding.recyclerViewPlaces.apply {
            adapter = placesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.fabAddPlace.setOnClickListener {
            findNavController().navigate(R.id.action_placesListFragment_to_addPlaceFragment)
        }
        binding.fabEditPlace.setOnClickListener {
            selectedPlace?.let { place ->
                val action = PlacesListFragmentDirections.actionPlacesListFragmentToAddPlaceFragment(
                    latitude = place.latitude.toFloat(),
                    longitude = place.longitude.toFloat()
                )
                findNavController().navigate(action)
            }
        }
        binding.fabDeletePlace.setOnClickListener {
            selectedPlace?.let { place ->
                viewModel.delete(place)
                selectedPlace = null
            }
        }
    }

    private fun observeViewModel() {
        viewModel.allPlaces.observe(viewLifecycleOwner) { places ->
            placesAdapter.submitList(places)
            binding.textViewEmpty.visibility = if (places.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun onPlaceClick(place: HappyPlace) {
        findNavController().navigate(R.id.action_placesListFragment_to_mapFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}