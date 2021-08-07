package com.example.ming.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.ming.databinding.PlaceItemBinding
import com.example.ming.logic.network.Place
import com.example.ming.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val placeName: TextView = binding.placeName
        val placeAddress: TextView = binding.placeAddress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding).apply {
            itemView.setOnClickListener {
                val position = this.absoluteAdapterPosition
                val place = placeList[position]
                val activity = fragment.activity
                if (activity is WeatherActivity) {
                    activity.closeDrawers()
                    activity.viewModel.locationLng = place.location.lng
                    activity.viewModel.locationLat = place.location.lat
                    activity.viewModel.placeName = place.name
                    activity.refreshWeather()
                } else {
                    val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                        putExtra("location_lng", place.location.lng)
                        putExtra("location_lat", place.location.lat)
                        putExtra("place_name", place.name)
                    }
                    fragment.startActivity(intent)
                    activity?.finish()
                }
                fragment.viewModel.savePlace(place)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size
}