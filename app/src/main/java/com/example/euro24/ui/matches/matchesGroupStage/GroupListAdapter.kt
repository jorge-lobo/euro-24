package com.example.euro24.ui.matches.matchesGroupStage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.data.groups.Group
import com.example.euro24.databinding.RvGroupListItemBinding

class GroupListAdapter : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(group: Group)
    }

    private var items: List<GroupListBindingItem> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvGroupListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<GroupListBindingItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: RvGroupListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val group = items[position].group
                    onItemClickListener?.onItemClick(group)
                }
            }
        }

        fun bind(item: GroupListBindingItem) {
            binding.group = item.group
        }
    }
}