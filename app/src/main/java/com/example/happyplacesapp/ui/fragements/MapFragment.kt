package com.example.happyplacesapp.ui.fragements

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.happyplacesapp.databinding.FragmentMapBinding
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // OSMDroid Konfiguration
        org.osmdroid.config.Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
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
        // TODO: Implementierung für das Laden der Places
    }

    private fun navigateToAddPlace(latitude: Double, longitude: Double) {
        // TODO: Navigation zur AddPlace Activity/Fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}