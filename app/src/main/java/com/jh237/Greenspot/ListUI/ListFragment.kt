package com.jh237.Greenspot.ListUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jh237.Greenspot.database.Spot
import com.jh237.Greenspot.databinding.FragmentSpotListBinding
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


/*
The ListFragment class is responsible for displaying the list of spots and the toolbar.
When a user presses on a spot, the SpotDetailFragment is navigated to.
When a user presses on the 'New Spot' button, a new Spot is created and the SpotDetailFragment is navigated to.
When a user presses on the help button, the HelpWebViewFragment is navigated to.
*/

class ListFragment : Fragment() {

    private var _binding: FragmentSpotListBinding? = null
    /*
            ViewBinding is used in this application. View binding is a feature that allows you to access views in layouts
            without using findViewById(). A binding class is generated for each layout unless specified otherwise. Each instance of
            the binding class contains a reference to the views in the layout.

            _binding is used to initialize the binding variable as it is nullable.

            checkNotNull is used to throw an exception if _binding is null, otherwise it returns the value of _binding to binding.
    */

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    /*
    ViewModels are used to store and manage UI-related data to avoid recreating it when it is not needed, for example, after a state change.
    This is because the view model caches state and persists.
     */

    private val ListFragmentViewModel: ListFragmentViewModel by viewModels()

    //Assigns values for the binding variable. Such as the recyclerView, the toolbar, and the help button.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpotListBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        (activity as AppCompatActivity).setSupportActionBar(binding.listToolbar)

        binding.helpButton.setOnClickListener {
            showHelp()
        }
        binding.addSpot.setOnClickListener {
            showNewSpot()
        }
        return binding.root
    }

    //This function is called when the view is created.
    // It is used to initialize the _spots flow with the value of spotRepo.getSpots().
    // It assigns the adapter to the recyclerView.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ListFragmentViewModel.spots.collect { spots ->
                    binding.recyclerView.adapter = SpotAdapter(spots) { spotId ->
                        findNavController().navigate(ListFragmentDirections.showSpotDetails(spotId))
                    }

                }
            }
        }

    }

    private fun showHelp() {
        findNavController().navigate(ListFragmentDirections.showWebView())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //showNewSpot defines an empty Spot and navigates to the SpotDetailFragment instance associated with it.

    private fun showNewSpot() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newSpot = Spot(
                id = UUID.randomUUID(),
                title = "",
                date = Date(),
                place = "",
                latitude = 0.0,
                longitude = 0.0
            )
            ListFragmentViewModel.addSpot(newSpot)
            findNavController().navigate(ListFragmentDirections.showSpotDetails(newSpot.id))
        }
    }

}