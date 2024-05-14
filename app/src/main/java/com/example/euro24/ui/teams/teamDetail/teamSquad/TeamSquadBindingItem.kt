package com.example.euro24.ui.teams.teamDetail.teamSquad

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.R
import com.example.euro24.data.managers.Manager
import com.example.euro24.data.players.Player
import com.mikepenz.fastadapter.items.AbstractItem

class TeamSquadBindingItem(val player: Player? = null, val manager: Manager? = null) :
    AbstractItem<TeamSquadBindingItem.ViewHolder>() {

    override val type: Int
        get() = R.id.fastadapter_id

    override val layoutRes: Int
        get() = R.layout.rv_squad_item_card

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        holder.playerCardName.text = player?.shortName ?: ""
        holder.playerCardNumber.text = player?.number.toString()
        holder.managerCardName.text = manager?.name ?: ""

    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        holder.playerCardName.text = null
        holder.playerCardNumber.text = null
        holder.managerCardName.text = null
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var playerCardName: TextView = view.findViewById(R.id.text_player_card_name)
        var playerCardNumber: TextView = view.findViewById(R.id.text_player_card_number)
        var managerCardName: TextView = view.findViewById(R.id.text_manager_card_name)
    }
}