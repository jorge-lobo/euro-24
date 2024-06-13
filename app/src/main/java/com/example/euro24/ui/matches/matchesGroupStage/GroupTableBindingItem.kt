package com.example.euro24.ui.matches.matchesGroupStage

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.R
import com.example.euro24.data.teams.Team
import com.mikepenz.fastadapter.items.AbstractItem

class GroupTableBindingItem(val position: Int, val team: Team) :
    AbstractItem<GroupTableBindingItem.ViewHolder>() {

    override val type: Int
        get() = R.id.fastadapter_id

    override val layoutRes: Int
        get() = R.layout.rv_group_table

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        val position = holder.bindingAdapterPosition + 1
        holder.tablePosition.text = position.toString()
        holder.tableTeam.text = team.name
        holder.tablePlayed.text = team.played.toString()
        holder.tableWon.text = team.won.toString()
        holder.tableDrawn.text = team.drawn.toString()
        holder.tableLost.text = team.lost.toString()
        holder.tableGoalsFor.text = team.goalsFor.toString()
        holder.tableGoalsAgainst.text = team.goalsAgainst.toString()
        holder.tableGoalDifference.text = team.goalDifference.toString()
        holder.tablePoints.text = team.points.toString()
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        holder.tablePosition.text = null
        holder.tableTeam.text = null
        holder.tablePlayed.text = null
        holder.tableWon.text = null
        holder.tableDrawn.text = null
        holder.tableLost.text = null
        holder.tableGoalsFor.text = null
        holder.tableGoalsAgainst.text = null
        holder.tableGoalDifference.text = null
        holder.tablePoints.text = null
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tablePosition: TextView = view.findViewById(R.id.table_text_position)
        var tableTeam: TextView = view.findViewById(R.id.table_text_team)
        var tablePlayed: TextView = view.findViewById(R.id.table_text_played)
        var tableWon: TextView = view.findViewById(R.id.table_text_won)
        var tableDrawn: TextView = view.findViewById(R.id.table_text_drawn)
        var tableLost: TextView = view.findViewById(R.id.table_text_lost)
        var tableGoalsFor: TextView = view.findViewById(R.id.table_text_goals_for)
        var tableGoalsAgainst: TextView = view.findViewById(R.id.table_text_goals_against)
        var tableGoalDifference: TextView = view.findViewById(R.id.table_text_goal_difference)
        var tablePoints: TextView = view.findViewById(R.id.table_text_points)
    }
}