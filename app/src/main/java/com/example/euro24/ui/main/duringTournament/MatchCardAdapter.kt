package com.example.euro24.ui.main.duringTournament

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.R
import com.example.euro24.databinding.RvMatchCardLargeBinding

class MatchCardAdapter : RecyclerView.Adapter<MatchCardAdapter.ViewHolder>() {

    private var items: List<MatchCardBindingItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvMatchCardLargeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<MatchCardBindingItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RvMatchCardLargeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchCardBindingItem) {
            binding.match = item.match

            with(binding) {
                itemImageTeam1Flag.setImageResource(
                    item.getTeamFlagResourceId(
                        item.match.team1 ?: ""
                    )
                )

                itemImageTeam2Flag.setImageResource(
                    item.getTeamFlagResourceId(
                        item.match.team2 ?: ""
                    )
                )

                itemImageTvIcon.setImageResource(
                    item.getTvIconResourceId(
                        item.match.broadcastPT ?: ""
                    )
                )
            }

            with(item.match) {
                val scoreTeam1 =
                    binding.root.findViewById<TextView>(R.id.item_text_match_score_team1)
                if (extraTimeTeam1 == null) {
                    scoreTeam1.text = resultTeam1.toString()
                } else {
                    scoreTeam1.text = extraTimeTeam1.toString()
                }

                val scoreTeam2 =
                    binding.root.findViewById<TextView>(R.id.item_text_match_score_team2)
                if (extraTimeTeam2 == null) {
                    scoreTeam2.text = resultTeam2.toString()
                } else {
                    scoreTeam2.text = extraTimeTeam2.toString()
                }

                if (resultTeam1 != null && resultTeam2 != null) {
                    showPostMatchContainer()
                } else {
                    showPreMatchContainer()
                }

                if (extraTimeTeam1 != null && extraTimeTeam2 != null && penaltiesTeam1 == null && penaltiesTeam2 == null) {
                    showExtraTimeMessage()
                } else if (penaltiesTeam1 != null && penaltiesTeam2 != null) {
                    showPenaltiesMessage()
                } else {
                    binding.textExtraResultMessage.visibility = View.INVISIBLE
                }
            }

            binding.executePendingBindings()
        }

        private fun showPreMatchContainer() {
            with(binding) {
                preMatchContainer.visibility = View.VISIBLE
                postMatchContainer.visibility = View.INVISIBLE
            }
        }

        private fun showPostMatchContainer() {
            with(binding) {
                preMatchContainer.visibility = View.INVISIBLE
                postMatchContainer.visibility = View.VISIBLE
            }
        }

        private fun showExtraTimeMessage() {
            with(binding.textExtraResultMessage) {
                text = context.getString(R.string.matches_after_extra_time)
                visibility = View.VISIBLE
            }
        }

        private fun showPenaltiesMessage() {
            with(binding.textExtraResultMessage) {
                text = context.getString(R.string.matches_after_penalties)
                visibility = View.VISIBLE
            }
        }
    }
}