package com.jh237.Greenspot.ItemUI

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.LocationServices
import com.jh237.Greenspot.MenuItems.DatePickerFragment
import com.jh237.Greenspot.database.Spot
import com.jh237.Greenspot.databinding.FragmentSpotDetailBinding
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class SpotDetailFragment : Fragment() {

    private val locationPermissionCode = 2
    private var _binding: FragmentSpotDetailBinding? = null
    private val binding get() = checkNotNull(_binding) { "Cannot access binding because it is null. Is the view visible?" }
    private val args: SpotDetailFragmentArgs by navArgs()
    private val SpotDetailViewModel: SpotDetailViewModel by viewModels {
        SpotDetailViewModelFactory(
            args.spotId
        )
    }


    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                SpotDetailViewModel.updateSpot {
                    it.copy(photo = result.toString())

                }
            }
        }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun savePhoto(uri: Uri) {
        takePhotoLauncher.launch(uri)
    }

    private fun takePhoto() {
        val file = createImageFile()
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "com.jh237.Greenspot.provider",
            file
        )
        savePhoto(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpotDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            spotTitle.doOnTextChanged { text, _, _, _ ->
                SpotDetailViewModel.updateSpot { it.copy(title = text.toString()) }
            }

            spotPlace.doOnTextChanged { text, _, _, _ ->
                SpotDetailViewModel.updateSpot { it.copy(place = text.toString()) }
            }

            setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) { _, bundle ->
                val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
                SpotDetailViewModel.updateSpot { it.copy(date = newDate) }
            }

            photoButton.setOnClickListener {
                takePhoto()
            }

            getLocationButton.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        locationPermissionCode
                    )
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    SpotDetailViewModel.spot.collect { spot ->
                        spot?.let {
                            updateUi(it)
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //This is updates the view inside the detail fragment

    //TODO Change the data structures so that the coordinates are stored better | Maybe done, reconsider
    //TODO Implement taking a photo of the spot | Taking photo complete, work out how to correctly save it.
    //TODO Implement delete button
    //TODO Implement a share button

    private fun updateUi(spot: Spot) {
        binding.apply {
            if (spotTitle.text.toString() != spot.title) {
                spotTitle.setText(spot.title)
            }
            spotDate.text = spot.date.toString()
            spotDate.setOnClickListener {
                findNavController().navigate(SpotDetailFragmentDirections.selectDate(spot.date))
            }
            if (spotPlace.text.toString() != spot.place) {
                spotPlace.setText(spot.place)
            }

            if (spotLocation.text.toString() != spot.latitude.toString() + ", " + spot.longitude.toString()) {
                spotLocation.text = spot.latitude.toString() + ", " + spot.longitude.toString()
            }

            spotPhoto.setImageURI(spot.photo.toUri())

            showMapButton.setOnClickListener {
                showMap(spot)
            }
        }
    }

    private fun showMap(spot: Spot) {
        findNavController().navigate(
            SpotDetailFragmentDirections.showMap(
                spot.latitude.toFloat(), spot.longitude.toFloat(), spot.place
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                location.let {
                    //Update the location on the screen
                    binding.spotLocation.text = String.format(
                        "%1\$s, %2\$s", location?.latitude, location?.longitude
                    )
                    //Take what is on the screen and update the database with it
                    SpotDetailViewModel.updateSpot {
                        it.copy(
                            latitude = location?.latitude ?: 0.0,
                            longitude = location?.longitude ?: 0.0
                        )
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }
}