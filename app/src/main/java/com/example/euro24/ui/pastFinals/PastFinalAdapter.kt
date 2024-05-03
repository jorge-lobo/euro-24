package com.example.euro24.ui.pastFinals

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.databinding.RvPastFinalsBinding

class PastFinalAdapter : RecyclerView.Adapter<PastFinalAdapter.ViewHolder>() {

    private var items: List<PastFinalBindingItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvPastFinalsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<PastFinalBindingItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RvPastFinalsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PastFinalBindingItem) {
            binding.pastFinal = item.pastFinal

        }
    }
}