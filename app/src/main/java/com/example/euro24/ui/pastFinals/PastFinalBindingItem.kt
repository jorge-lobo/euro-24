package com.example.euro24.ui.pastFinals

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.R
import com.example.euro24.data.pastFinals.PastFinal
import com.mikepenz.fastadapter.items.AbstractItem

class PastFinalBindingItem(val pastFinal: PastFinal) :
    AbstractItem<PastFinalBindingItem.ViewHolder>() {

    override val type: Int
        get() = R.id.fastadapter_id

    override val layoutRes: Int
        get() = R.layout.rv_past_finals

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        holder.year.text = pastFinal.year.toString()
        holder.host.text = pastFinal.host
        holder.winner.text = pastFinal.winners
        holder.runnerUp.text = pastFinal.runnersUp
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        holder.year.text = null
        holder.host.text = null
        holder.winner.text = null
        holder.runnerUp.text = null
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var year: TextView = view.findViewById(R.id.item_text_year)
        var host: TextView = view.findViewById(R.id.item_text_host)
        var winner: TextView = view.findViewById(R.id.item_text_winner)
        var runnerUp: TextView = view.findViewById(R.id.item_text_runner_up)
    }
}