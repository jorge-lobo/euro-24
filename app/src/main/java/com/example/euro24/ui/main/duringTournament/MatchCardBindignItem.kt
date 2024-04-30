package com.example.euro24.ui.main.duringTournament

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

class MatchCardBindingItem(val match: Match) : AbstractItem<MatchCardBindingItem.ViewHolder>() {

    /**
     * defines the type defining this item. must be unique, preferably an id
     *
     * @return the type
     */
    override val type: Int
        get() = R.id.fastadapter_id

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    override val layoutRes: Int
        get() = R.layout.rv_match_card_large

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param holder the viewHolder of this item
     */
    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        holder.city.text = match.city
        holder.date.text = DateUtils.formatDateShort(match.date ?: "")
        holder.time.text = DateUtils.convertToTimeZone(match.time ?: "", "HH:mm", TimeZone.getDefault())
        holder.team1Name.text = match.team1   //need to change to team1Id
        holder.team2Name.text = match.team2   //need to change to team2Id
        holder.team1Score.text = match.resultTeam1.toString()
        holder.team2Score.text = match.resultTeam2.toString()
        holder.team1Penalties.text = match.penaltiesTeam1.toString()
        holder.team2Penalties.text = match.penaltiesTeam2.toString()

        val team1FlagResId = match.team1?.let { getTeamFlagResourceId(it) }
        holder.team1Flag.setImageResource(team1FlagResId ?: R.drawable.default_flag)

        val team2FlagResId = match.team2?.let { getTeamFlagResourceId(it) }
        holder.team2Flag.setImageResource(team2FlagResId ?: R.drawable.default_flag)

        val tvIconResId = match.broadcastPT?.let { getTvIconResourceId(it) }
        holder.tvIcon.setImageResource(tvIconResId ?: R.drawable.default_tv)
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

    /**
     * our ViewHolder
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
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

    fun getTeamFlagResourceId(team: String): Int {
        return ImagesResourceMap.flagResourceMapByName[team] ?: R.drawable.default_flag
    }

    fun getTvIconResourceId(broadcastPT: String): Int {
        return ImagesResourceMap.tvIconResourceMap[broadcastPT] ?: R.drawable.default_tv
    }
}