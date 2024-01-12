package com.jh237.Greenspot.MenuItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jh237.Greenspot.databinding.FragmentMapViewBinding


class MapViewFragment : Fragment() {

    private val args: MapViewFragmentArgs by navArgs()

    private var _binding: FragmentMapViewBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(binding.mapFragment.id) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            val target = LatLng(args.latitude.toDouble(), args.longitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(target).title(args.placeName))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 15f))

        }
    }


}