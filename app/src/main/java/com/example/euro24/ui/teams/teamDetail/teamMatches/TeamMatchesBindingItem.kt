package com.example.euro24.ui.teams.teamDetail.teamMatches

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.R
import com.example.euro24.data.matches.Match
import com.example.euro24.utils.DateUtils
import com.example.euro24.utils.ImagesResourceMap
import com.mikepenz.fastadapter.items.AbstractItem
import java.util.TimeZone

class TeamMatchesBindingItem(val match: Match) : AbstractItem<TeamMatchesBindingItem.ViewHolder>() {

    private val defaultFlag = R.drawable.default_flag

    override val type: Int
        get() = R.id.fastadapter_id

    override val layoutRes: Int
        get() = R.layout.rv_match_card_narrow

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        holder.dateNarrow.text = DateUtils.formatDateShort(match.date ?: "")
        holder.cityNarrow.text = match.venueId.toString()
        holder.phaseNarrow.text = match.phase
        holder.timeNarrow.text =
            DateUtils.convertToTimeZone(match.time ?: "", "HH:mm", TimeZone.getDefault())
        holder.team1NameNarrow.text = match.team1   //need to change to team1Id
        holder.team2NameNarrow.text = match.team2   //need to change to team2Id
        holder.team1ScoreNarrow.text = match.resultTeam1.toString()
        holder.team2ScoreNarrow.text = match.resultTeam2.toString()
        holder.team1PenaltiesNarrow.text = match.penaltiesTeam1.toString()
        holder.team2PenaltiesNarrow.text = match.penaltiesTeam2.toString()

        val team1FlagResId = match.team1?.let { getTeamFlagResourceId(it) } ?: defaultFlag
        holder.team1FlagNarrow.setImageResource(team1FlagResId)

        val team2FlagResId = match.team2?.let { getTeamFlagResourceId(it) } ?: defaultFlag
        holder.team2FlagNarrow.setImageResource(team2FlagResId)
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.dateNarrow.text = null
        holder.cityNarrow.text = null
        holder.phaseNarrow.text = null
        holder.timeNarrow.text = null
        holder.team1NameNarrow.text = null
        holder.team2NameNarrow.text = null
        holder.team1ScoreNarrow.text = null
        holder.team2ScoreNarrow.text = null
        holder.team1PenaltiesNarrow.text = null
        holder.team2PenaltiesNarrow.text = null
        holder.team1FlagNarrow.setImageResource(0)
        holder.team2FlagNarrow.setImageResource(0)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dateNarrow: TextView = view.findViewById(R.id.item_text_match_date_narrow)
        var cityNarrow: TextView = view.findViewById(R.id.item_text_match_city_narrow)
        var phaseNarrow: TextView = view.findViewById(R.id.item_text_phase_narrow)
        var timeNarrow: TextView = view.findViewById(R.id.item_text_match_time_narrow)
        var team1NameNarrow: TextView = view.findViewById(R.id.item_text_team1_name_narrow)
        var team2NameNarrow: TextView = view.findViewById(R.id.item_text_team2_name_narrow)
        var team1ScoreNarrow: TextView = view.findViewById(R.id.item_text_match_score_team1_narrow)
        var team2ScoreNarrow: TextView = view.findViewById(R.id.item_text_match_score_team2_narrow)
        var team1PenaltiesNarrow: TextView =
            view.findViewById(R.id.item_text_match_penalties_team1_narrow)
        var team2PenaltiesNarrow: TextView =
            view.findViewById(R.id.item_text_match_penalties_team2_narrow)
        var team1FlagNarrow: ImageView = view.findViewById(R.id.item_image_team1_flag_narrow)
        var team2FlagNarrow: ImageView = view.findViewById(R.id.item_image_team2_flag_narrow)
    }

    fun getTeamFlagResourceId(team: String): Int {
        return ImagesResourceMap.flagResourceMapByName[team] ?: defaultFlag
    }
}