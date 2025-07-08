package com.example.happyplacesapp.ui.fragements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.happyplacesapp.R
import com.example.happyplacesapp.databinding.FragmentMapBinding
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private val viewModel: HappyPlaceViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // OSMDroid Konfiguration (deprecated PreferenceManager entfernt)
        org.osmdroid.config.Configuration.getInstance().load(
            requireContext(),
            requireContext().getSharedPreferences("osmdroid", android.content.Context.MODE_PRIVATE)
        )

        mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Karte auf Deutschland zentrieren
        val mapController = mapView.controller
        mapController.setZoom(6.0)
        val startPoint = GeoPoint(51.1657, 10.4515) // Deutschland Zentrum
        mapController.setCenter(startPoint)

        setupMapClickListener()
        loadPlacesOnMap()
    }

    private fun setupMapClickListener() {
        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    navigateToAddPlace(it.latitude, it.longitude)
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }

        val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
        mapView.overlays.add(mapEventsOverlay)
    }

    private fun loadPlacesOnMap() {
        viewModel.allPlaces.observe(viewLifecycleOwner, Observer { places ->
            // Alle vorhandenen Marker entfernen
            mapView.overlays.removeAll { it is Marker }

            // Für jeden Place einen Marker hinzufügen
            places.forEach { place ->
                val marker = Marker(mapView)
                marker.position = GeoPoint(place.latitude, place.longitude)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = place.title
                marker.snippet = place.description

                // Marker zur Karte hinzufügen
                mapView.overlays.add(marker)
            }

            // Karte aktualisieren
            mapView.invalidate()
        })
    }

    private fun navigateToAddPlace(latitude: Double, longitude: Double) {
        // Einfache Navigation ohne Safe Args
        findNavController().navigate(R.id.action_mapFragment_to_addPlaceFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}