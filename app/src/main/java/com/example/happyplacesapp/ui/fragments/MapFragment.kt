package com.example.happyplacesapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.happyplacesapp.R
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDatabase
import com.example.happyplacesapp.data.repository.HappyPlaceRepository
import com.example.happyplacesapp.databinding.FragmentMapBinding
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModel
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var gestureDetector: GestureDetector

    private val viewModel: HappyPlaceViewModel by viewModels {
        HappyPlaceViewModelFactory(
            HappyPlaceRepository(
                HappyPlaceDatabase.getDatabase(requireContext()).happyPlaceDao()
            )
        )
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            enableMyLocation()
        } else {
            Toast.makeText(requireContext(), "Standort-Berechtigung wurde verweigert", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // OSMDroid Konfiguration
        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )

        setupMap()
        setupFABs()
        observePlaces()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun setupMap() {
        mapView = binding.osmMapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Setze initiale Kameraposition (Deutschland - Köln)
        val startPoint = GeoPoint(50.9375, 6.9603) // Köln, Deutschland
        mapView.controller.setZoom(10.0)
        mapView.controller.setCenter(startPoint)

        // My Location Overlay
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        myLocationOverlay.enableMyLocation()
        mapView.overlays.add(myLocationOverlay)

        // Gesture Detector für Long Press
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                val projection = mapView.projection
                val geoPoint = projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                showAddPlaceDialog(geoPoint.latitude, geoPoint.longitude)
            }
        })

        mapView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
    }

    private fun setupFABs() {
        binding.fabAddPlaceMap.setOnClickListener {
            getCurrentLocationAndAddPlace()
        }

        binding.fabMyLocation.setOnClickListener {
            goToMyLocation()
        }
    }

    private fun observePlaces() {
        viewModel.allPlaces.observe(viewLifecycleOwner) { places ->
            displayPlacesOnMap(places)
        }
    }

    private fun displayPlacesOnMap(places: List<HappyPlace>) {
        // Entferne alte Marker (außer MyLocation)
        val markersToRemove = mapView.overlays.filterIsInstance<Marker>()
        mapView.overlays.removeAll(markersToRemove)

        // Füge neue Marker hinzu
        places.forEach { place ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(place.latitude, place.longitude)
                title = place.title
                snippet = "${place.description}\nKategorie: ${place.category}"

                // Setze Marker Icon basierend auf Kategorie
                when (place.category) {
                    "Natur" -> setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_nature))
                    "Essen" -> setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_restaurant))
                    "Sport" -> setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_sports))
                    "Kultur" -> setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_culture))
                    else -> setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_place))
                }

                setOnMarkerClickListener { _, _ ->
                    showPlaceDetails(place)
                    true
                }
            }

            mapView.overlays.add(marker)
        }

        mapView.invalidate()
    }

    private fun showPlaceDetails(place: HappyPlace) {
        // Navigiere zum EditMode des AddPlaceFragment
        findNavController().navigate(
            R.id.addPlaceFragment,
            bundleOf(
                "latitude" to place.latitude.toFloat(),
                "longitude" to place.longitude.toFloat(),
                "placeId" to place.id
            )
        )
    }

    private fun getCurrentLocationAndAddPlace() {
        if (hasLocationPermission()) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        showAddPlaceDialog(location.latitude, location.longitude)
                    } else {
                        Toast.makeText(requireContext(), "Standort konnte nicht ermittelt werden", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: SecurityException) {
                requestLocationPermission()
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun showAddPlaceDialog(latitude: Double, longitude: Double) {
        findNavController().navigate(
            R.id.addPlaceFragment,
            bundleOf(
                "latitude" to latitude.toFloat(),
                "longitude" to longitude.toFloat(),
                "placeId" to -1L
            )
        )
    }

    private fun goToMyLocation() {
        if (hasLocationPermission()) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val geoPoint = GeoPoint(location.latitude, location.longitude)
                        mapView.controller.animateTo(geoPoint)
                        mapView.controller.setZoom(15.0)
                    } else {
                        Toast.makeText(requireContext(), "Standort konnte nicht ermittelt werden", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: SecurityException) {
                requestLocationPermission()
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun enableMyLocation() {
        if (hasLocationPermission()) {
            myLocationOverlay.enableMyLocation()
            myLocationOverlay.enableFollowLocation()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}