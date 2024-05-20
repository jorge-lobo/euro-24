package com.example.euro24.ui.matches.matchesGroupStage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.databinding.RvGroupTableBinding

class GroupTableAdapter : RecyclerView.Adapter<GroupTableAdapter.ViewHolder>() {

    private var items: List<GroupTableBindingItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvGroupTableBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position + 1)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<GroupTableBindingItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RvGroupTableBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GroupTableBindingItem, position: Int) {
            binding.team = item.team
            binding.position = position
            binding.executePendingBindings()
        }
    }
}