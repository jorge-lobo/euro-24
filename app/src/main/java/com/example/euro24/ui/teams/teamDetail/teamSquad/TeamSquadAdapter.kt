package com.example.euro24.ui.teams.teamDetail.teamSquad

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.euro24.R
import com.example.euro24.databinding.RvSquadItemCardBinding

class TeamSquadAdapter : RecyclerView.Adapter<TeamSquadAdapter.ViewHolder>() {

    private var items: List<TeamSquadBindingItem> = emptyList()
    private val defaultFace = R.drawable.default_face

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvSquadItemCardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<TeamSquadBindingItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RvSquadItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TeamSquadBindingItem) {
            binding.apply {
                player = item.player
                manager = item.manager
                imagePlayerCardPhoto.clipToOutline = true
                textManagerCardName.visibility =
                    if (item.manager != null) View.VISIBLE else View.INVISIBLE
                textPlayerCardName.visibility =
                    if (item.player != null) View.VISIBLE else View.INVISIBLE
                playerCardNumberContainer.visibility =
                    if (item.player != null) View.VISIBLE else View.INVISIBLE

                Glide.with(root.context)
                    .load(item.player?.image ?: item.manager?.image)
                    .placeholder(defaultFace)
                    .error(defaultFace)
                    .into(imagePlayerCardPhoto)

                item.player?.let { player ->
                    binding.imageCaptain.visibility =
                        if (player.captain == true) View.VISIBLE else View.INVISIBLE
                }
            }

            binding.executePendingBindings()
        }
    }
}