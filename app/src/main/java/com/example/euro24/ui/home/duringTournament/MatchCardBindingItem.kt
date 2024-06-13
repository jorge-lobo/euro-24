package com.example.euro24.ui.home.duringTournament

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.R
import com.example.euro24.data.matches.Match
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.utils.DateUtils
import com.example.euro24.utils.ImagesResourceMap
import com.mikepenz.fastadapter.items.AbstractItem
import java.util.TimeZone

class MatchCardBindingItem(val match: Match, private val teamRepository: TeamRepository) :
    AbstractItem<MatchCardBindingItem.ViewHolder>() {

    private val defaultFlag = R.drawable.default_flag
    private val defaultTv = R.drawable.default_tv

    override val type: Int
        get() = R.id.fastadapter_id

    override val layoutRes: Int
        get() = R.layout.rv_match_card_large

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        holder.city.text = match.city
        holder.date.text = DateUtils.formatDateShort(match.date ?: "")
        holder.time.text =
            DateUtils.convertToTimeZone(match.time ?: "", "HH:mm", TimeZone.getDefault())

        val team1 = teamRepository.getTeamById(match.team1Id ?: 0)
        val team2 = teamRepository.getTeamById(match.team2Id ?: 0)

        holder.team1Name.text = team1?.name ?: "Unknown"
        holder.team2Name.text = team2?.name ?: "Unknown"
        holder.team1Score.text = match.resultTeam1.toString()
        holder.team2Score.text = match.resultTeam2.toString()
        holder.team1Penalties.text = match.penaltiesTeam1.toString()
        holder.team2Penalties.text = match.penaltiesTeam2.toString()

        val team1FlagResId =
            team1?.id?.let { ImagesResourceMap.flagResourceMapById[it] } ?: defaultFlag
        holder.team1Flag.setImageResource(team1FlagResId)

        val team2FlagResId =
            team2?.id?.let { ImagesResourceMap.flagResourceMapById[it] } ?: defaultFlag
        holder.team2Flag.setImageResource(team2FlagResId)

        val tvIconResId = match.broadcastPT?.let { getTvIconResourceId(it) }
        holder.tvIcon.setImageResource(tvIconResId ?: defaultTv)
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.city.text = null
        holder.date.text = null
        holder.time.text = null
        holder.team1Name.text = null
        holder.team2Name.text = null
        holder.team1Score.text = null
        holder.team2Score.text = null
        holder.team1Flag.setImageResource(0)
        holder.team2Flag.setImageResource(0)
        holder.tvIcon.setImageResource(0)
        holder.team1Penalties.text = null
        holder.team2Penalties.text = null
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var city: TextView = view.findViewById(R.id.item_text_match_city)
        var date: TextView = view.findViewById(R.id.item_text_match_date)
        var time: TextView = view.findViewById(R.id.item_text_match_time)
        var team1Name: TextView = view.findViewById(R.id.item_text_team1_name)
        var team2Name: TextView = view.findViewById(R.id.item_text_team2_name)
        var team1Score: TextView = view.findViewById(R.id.item_text_match_score_team1)
        var team2Score: TextView = view.findViewById(R.id.item_text_match_score_team2)
        var team1Flag: ImageView = view.findViewById(R.id.item_image_team1_flag)
        var team2Flag: ImageView = view.findViewById(R.id.item_image_team2_flag)
        var tvIcon: ImageView = view.findViewById(R.id.item_image_tv_icon)
        var team1Penalties: TextView = view.findViewById(R.id.item_text_match_penalties_team1)
        var team2Penalties: TextView = view.findViewById(R.id.item_text_match_penalties_team2)
    }

    fun getTvIconResourceId(broadcastPT: String): Int {
        return ImagesResourceMap.tvIconResourceMap[broadcastPT] ?: defaultTv
    }
}