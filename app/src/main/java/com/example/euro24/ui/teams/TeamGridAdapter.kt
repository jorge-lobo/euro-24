package com.example.euro24.ui.teams

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.euro24.R
import com.example.euro24.data.teams.Team
import com.example.euro24.utils.ImagesResourceMap

class TeamGridAdapter(
    private val context: Context,
    private val teamList: List<Team>,
    private val itemClickListener: OnItemClickListener
) : BaseAdapter() {

    interface OnItemClickListener {
        fun onItemClick(team: Team)
    }

    override fun getCount(): Int {
        return teamList.size
    }

    override fun getItem(position: Int): Any {
        return teamList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.grid_item_team, parent, false)

        val team = teamList[position]
        val viewHolder = ViewHolder(itemView)
        viewHolder.textView.text = team.name
        viewHolder.imageView.setImageResource(
            ImagesResourceMap.flagResourceMapById[team.id] ?: R.drawable.default_flag
        )

        itemView.setOnClickListener {
            itemClickListener.onItemClick(team)
        }

        return itemView
    }

    private class ViewHolder(itemView: View) {
        val imageView: ImageView = itemView.findViewById(R.id.image_grid_team_flag)
        val textView: TextView = itemView.findViewById(R.id.text_grid_team_name)
    }

}