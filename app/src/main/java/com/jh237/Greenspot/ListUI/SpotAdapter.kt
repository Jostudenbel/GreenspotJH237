package com.jh237.Greenspot.ListUI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jh237.Greenspot.database.Spot
import com.jh237.Greenspot.databinding.ListItemBinding
import java.util.UUID

//SpotHolder is used to bind the data to the layout.

class SpotHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(spot: Spot, onSpotClicked: (spotId: UUID) -> Unit) {
        binding.spotTitle.text = spot.title
        binding.spotDate.text = spot.date.toString()
        binding.spotPlace.text = spot.place
        binding.root.setOnClickListener {
            onSpotClicked(spot.id)
        }
    }

}

//The SpotAdapter class inflates the layout with the data provided by SpotHolder.

class SpotAdapter(
    private val spots: List<Spot>,
    private val onSpotClicked: (spotId: UUID) -> Unit
) : RecyclerView.Adapter<SpotHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return SpotHolder(binding)
    }

    override fun onBindViewHolder(holder: SpotHolder, position: Int) {
        val spot = spots[position]
        holder.bind(spot, onSpotClicked)
    }

    override fun getItemCount() = spots.size
}
