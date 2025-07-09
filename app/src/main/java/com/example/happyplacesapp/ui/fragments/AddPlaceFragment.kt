package com.example.happyplacesapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.happyplacesapp.R
import com.example.happyplacesapp.data.database.HappyPlace
import com.example.happyplacesapp.data.database.HappyPlaceDatabase
import com.example.happyplacesapp.data.repository.HappyPlaceRepository
import com.example.happyplacesapp.databinding.FragmentAddPlaceBinding
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModel
import com.example.happyplacesapp.ui.viewmodels.HappyPlaceViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddPlaceFragment : Fragment() {

    private var _binding: FragmentAddPlaceBinding? = null
    private val binding get() = _binding!!

    private val args: AddPlaceFragmentArgs by navArgs()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var currentPhotoPath: String = ""
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private var isEditMode = false
    private var currentPlace: HappyPlace? = null

    private val viewModel: HappyPlaceViewModel by viewModels {
        HappyPlaceViewModelFactory(
            HappyPlaceRepository(
                HappyPlaceDatabase.getDatabase(requireContext()).happyPlaceDao()
            )
        )
    }

    private val categories = listOf(
        "Allgemein", "Natur", "Essen", "Entspannung", "Unterhaltung", "Sport", "Kultur"
    )

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(requireContext(), "Kamera-Berechtigung benötigt", Toast.LENGTH_SHORT).show()
        }
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Standort-Berechtigung benötigt", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            loadImageIntoView(currentPhotoPath)
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            saveImageToInternalStorage(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupUI()
        setupClickListeners()
        handleArguments()
    }

    private fun setupUI() {
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.actvCategory.setAdapter(categoryAdapter)
        binding.actvCategory.setText(categories[0], false)
    }

    private fun setupClickListeners() {
        binding.fabSelectImage.setOnClickListener {
            showImageSelectionDialog()
        }

        binding.btnUseCurrentLocation.setOnClickListener {
            requestLocationAndUpdate()
        }

        binding.btnSave.setOnClickListener {
            savePlace()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun handleArguments() {
        currentLatitude = args.latitude.toDouble()
        currentLongitude = args.longitude.toDouble()

        if (args.placeId != -1L) {
            isEditMode = true
            loadPlaceForEdit(args.placeId)
        } else if (currentLatitude != 0.0 && currentLongitude != 0.0) {
            updateLocationInfo(currentLatitude, currentLongitude)
        }
    }

    private fun loadPlaceForEdit(placeId: Long) {
        lifecycleScope.launch {
            currentPlace = viewModel.getPlaceById(placeId)
            currentPlace?.let { place ->
                binding.etTitle.setText(place.title)
                binding.etDescription.setText(place.description)
                binding.etNotes.setText(place.notes)
                binding.actvCategory.setText(place.category, false)

                currentLatitude = place.latitude
                currentLongitude = place.longitude
                currentPhotoPath = place.imagePath

                updateLocationInfo(currentLatitude, currentLongitude)

                if (place.imagePath.isNotEmpty()) {
                    loadImageIntoView(place.imagePath)
                }

                binding.toolbarAddPlace.title = "Ort bearbeiten"
            }
        }
    }

    private fun showImageSelectionDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Bild auswählen")
            .setItems(arrayOf("Foto aufnehmen", "Aus Galerie wählen")) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndTakePicture()
                    1 -> pickImageLauncher.launch("image/*")
                }
            }
            .show()
    }

    private fun checkCameraPermissionAndTakePicture() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun dispatchTakePictureIntent() {
        try {
            val photoFile = createImageFile()
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                photoFile
            )
            takePictureLauncher.launch(photoURI)
        } catch (ex: Exception) {
            Toast.makeText(requireContext(), "Fehler beim Erstellen der Datei", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = File(requireContext().getExternalFilesDir(null), "Pictures")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val imageFile = File(storageDir, "HAPPY_PLACE_${timeStamp}.jpg")
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    private fun saveImageToInternalStorage(uri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val file = createImageFile()
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.close()

            loadImageIntoView(currentPhotoPath)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Fehler beim Speichern des Bildes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadImageIntoView(imagePath: String) {
        Glide.with(this)
            .load(imagePath)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(binding.ivPlaceImage)
    }

    private fun requestLocationAndUpdate() {
        if (hasLocationPermission()) {
            getCurrentLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun getCurrentLocation() {
        if (!hasLocationPermission()) {
            requestLocationPermission()
            return
        }

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    updateLocationInfo(currentLatitude, currentLongitude)
                    Toast.makeText(requireContext(), "Standort erfolgreich ermittelt", Toast.LENGTH_SHORT).show()
                } else {
                    // Fallback: Versuche aktuellen Standort anzufordern
                    requestCurrentLocationUpdate()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Fehler beim Abrufen des Standorts: ${exception.message}", Toast.LENGTH_LONG).show()
                // Verwende Köln-Koordinaten als Fallback
                currentLatitude = 50.9375
                currentLongitude = 6.9603
                updateLocationInfo(currentLatitude, currentLongitude)
            }
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Standort-Berechtigung fehlt", Toast.LENGTH_SHORT).show()
            requestLocationPermission()
        }
    }

    private fun requestCurrentLocationUpdate() {
        if (!hasLocationPermission()) {
            requestLocationPermission()
            return
        }

        try {
            val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                10000
            ).apply {
                setMaxUpdates(1)
                setWaitForAccurateLocation(false)
            }.build()

            val locationCallback = object : com.google.android.gms.location.LocationCallback() {
                override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        currentLatitude = location.latitude
                        currentLongitude = location.longitude
                        updateLocationInfo(currentLatitude, currentLongitude)
                        Toast.makeText(requireContext(), "Standort erfolgreich ermittelt", Toast.LENGTH_SHORT).show()
                    }
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Standort-Berechtigung fehlt", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestLocationPermission() {
        when {
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Standort-Berechtigung benötigt")
                    .setMessage("Diese App benötigt Zugriff auf Ihren Standort, um Orte automatisch zu lokalisieren.")
                    .setPositiveButton("Berechtigung erteilen") { _, _ ->
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                    .setNegativeButton("Abbrechen", null)
                    .show()
            }
            else -> {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun updateLocationInfo(latitude: Double, longitude: Double) {
        val locationText = "Lat: ${String.format("%.4f", latitude)}, Lng: ${String.format("%.4f", longitude)}"
        binding.tvLocationInfo.text = locationText

        lifecycleScope.launch {
            try {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val addressText = address.getAddressLine(0) ?: locationText
                    binding.tvLocationInfo.text = addressText
                }
            } catch (e: Exception) {
                // Fallback auf Koordinaten
            }
        }
    }

    private fun savePlace() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()
        val category = binding.actvCategory.text.toString()

        if (title.isEmpty()) {
            binding.etTitle.error = "Titel ist erforderlich"
            return
        }

        if (description.isEmpty()) {
            binding.etDescription.error = "Beschreibung ist erforderlich"
            return
        }

        if (currentLatitude == 0.0 && currentLongitude == 0.0) {
            Toast.makeText(requireContext(), "Bitte wählen Sie einen Standort", Toast.LENGTH_SHORT).show()
            return
        }

        val place = HappyPlace(
            id = if (isEditMode) currentPlace?.id ?: 0 else 0,
            title = title,
            description = description,
            latitude = currentLatitude,
            longitude = currentLongitude,
            imagePath = currentPhotoPath,
            notes = notes,
            category = category,
            location = binding.tvLocationInfo.text.toString(),
            dateAdded = if (isEditMode) currentPlace?.dateAdded ?: System.currentTimeMillis() else System.currentTimeMillis()
        )

        lifecycleScope.launch {
            try {
                if (isEditMode) {
                    viewModel.updatePlace(place)
                    Toast.makeText(requireContext(), "Ort aktualisiert", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insertPlace(place)
                    Toast.makeText(requireContext(), "Ort gespeichert", Toast.LENGTH_SHORT).show()
                }
                findNavController().navigateUp()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Fehler beim Speichern", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
