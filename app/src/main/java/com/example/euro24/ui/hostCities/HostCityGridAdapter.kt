package com.example.euro24.ui.hostCities

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.euro24.R
import com.example.euro24.data.venues.Venue
import com.example.euro24.utils.ImagesResourceMap

class HostCityGridAdapter(
    private val context: Context,
    private val venueList: List<Venue>,
    private val itemClickListener: OnItemClickListener
) : BaseAdapter() {

    interface OnItemClickListener {
        fun onItemClick(venue: Venue)
    }

    override fun getCount(): Int {
        return venueList.size
    }

    override fun getItem(position: Int): Any {
        return venueList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.grid_item_host_city, parent, false)

        val venue = venueList[position]
        val viewHolder = ViewHolder(itemView)
        viewHolder.textView.text = venue.cityEN
        viewHolder.imageView.setImageResource(
            ImagesResourceMap.cityImageResourceMap[venue.id] ?: R.drawable.default_image
        )

        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.background_host_city_card)
        if (backgroundDrawable is GradientDrawable) {
            viewHolder.imageView.clipToOutline = true
            viewHolder.imageView.background = backgroundDrawable
        }

        itemView.setOnClickListener {
            itemClickListener.onItemClick(venue)
        }

        return itemView
    }

    private class ViewHolder(itemView: View) {
        val imageView: ImageView = itemView.findViewById(R.id.image_host_city)
        val textView: TextView = itemView.findViewById(R.id.text_host_city_name)
    }
}