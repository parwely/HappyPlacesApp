package com.example.happyplacesapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.happyplacesapp.R
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.databinding.FragmentMapBinding
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private val viewModel: HappyPlaceViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.fabAddPlaceMap.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_addPlaceFragment)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val germany = LatLng(51.1657, 10.4515)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(germany, 6f))
        observePlacesAndAddMarkers()
        map.setOnMapClickListener { latLng ->
            showAddPlaceDialog(latLng)
        }
    }

    private fun showAddPlaceDialog(latLng: LatLng) {
        val context = requireContext()
        val inputTitle = EditText(context)
        inputTitle.hint = "Name"
        val inputDesc = EditText(context)
        inputDesc.hint = "Beschreibung"
        val layout = android.widget.LinearLayout(context)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.addView(inputTitle)
        layout.addView(inputDesc)
        AlertDialog.Builder(context)
            .setTitle("Neuen Ort hinzufügen")
            .setView(layout)
            .setPositiveButton("Speichern") { _, _ ->
                val title = inputTitle.text.toString().trim()
                val desc = inputDesc.text.toString().trim()
                if (title.isNotEmpty()) {
                    val place = HappyPlace(
                        id = 0,
                        title = title,
                        description = desc,
                        location = "",
                        latitude = latLng.latitude,
                        longitude = latLng.longitude,
                        imagePath = "",
                        notes = "",
                        category = ""
                    )
                    viewModel.insert(place)
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    private fun observePlacesAndAddMarkers() {
        viewModel.allPlaces.observe(viewLifecycleOwner) { places ->
            googleMap?.clear()
            places.forEach { place ->
                val latLng = LatLng(place.latitude, place.longitude)
                googleMap?.addMarker(
                    MarkerOptions().position(latLng).title(place.title).snippet(place.description)
                )
            }
        }
    }

    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
    override fun onDestroyView() { super.onDestroyView(); mapView.onDestroy(); _binding = null }
    override fun onLowMemory() { super.onLowMemory(); mapView.onLowMemory() }
}