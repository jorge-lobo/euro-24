package com.example.euro24.ui.teams.teamDetail.teamSquad

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.euro24.R
import com.example.euro24.databinding.RvSquadItemCardBinding
import com.example.euro24.ui.player.PlayerFragment
import java.util.Stack

class TeamSquadAdapter(private val fragmentStack: Stack<Fragment>, private val teamId: Int) :
    RecyclerView.Adapter<TeamSquadAdapter.ViewHolder>() {

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
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            fragmentStack.push(
                (v?.context as AppCompatActivity).supportFragmentManager.findFragmentById(
                    R.id.fragment_container
                )
            )

            val player = items[absoluteAdapterPosition].player
            player?.id?.let {
                val fragment = PlayerFragment.newInstance(it, teamId)
                val transaction =
                    (v.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.apply {
                    replace(R.id.fragment_container, fragment)
                    addToBackStack(null)
                    commit()
                }
            }
        }

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