package com.example.euro24.ui.matches

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.R
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.databinding.RvMatchCardNarrowBinding
import com.example.euro24.ui.matches.matchEditor.MatchEditorFragment
import com.example.euro24.ui.matches.matchEditor.MatchEditorFragmentContainer
import com.example.euro24.utils.ImagesResourceMap

class MatchNarrowCardAdapter(
    private val teamRepository: TeamRepository,
    private val parentFragment: MatchEditorFragmentContainer
) :
    RecyclerView.Adapter<MatchNarrowCardAdapter.ViewHolder>() {

    private var items: List<MatchNarrowCardBindingItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvMatchCardNarrowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<MatchNarrowCardBindingItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RvMatchCardNarrowBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val activity = v?.context as? AppCompatActivity
            if (activity != null) {
                val fragmentManager = activity.supportFragmentManager
                val fragment = MatchEditorFragment.newInstance(
                    items[absoluteAdapterPosition].match.id ?: return
                )

                fragmentManager.beginTransaction().apply {
                    replace(R.id.match_editor_fragment_container, fragment)
                    addToBackStack(null)
                    commit()
                }

                parentFragment.showMatchEditorFragmentContainer()
            }
        }

        fun bind(item: MatchNarrowCardBindingItem) {
            binding.match = item.match

            with(binding) {
                val team1 = teamRepository.getTeamById(item.match.team1Id ?: 0)
                val team2 = teamRepository.getTeamById(item.match.team2Id ?: 0)

                itemTextTeam1NameNarrow.text = team1?.name ?: "Unknown"
                itemTextTeam2NameNarrow.text = team2?.name ?: "Unknown"

                val team1FlagResId = team1?.id?.let { ImagesResourceMap.flagResourceMapById[it] }
                    ?: R.drawable.default_flag
                itemImageTeam1FlagNarrow.setImageResource(team1FlagResId)

                val team2FlagResId = team2?.id?.let { ImagesResourceMap.flagResourceMapById[it] }
                    ?: R.drawable.default_flag
                itemImageTeam2FlagNarrow.setImageResource(team2FlagResId)


                itemTextPhaseNarrow.visibility = View.INVISIBLE
                itemTextMatchCityNarrow.visibility = View.VISIBLE
            }

            with(item.match) {
                val scoreTeam1Narrow =
                    binding.root.findViewById<TextView>(R.id.item_text_match_score_team1_narrow)
                if (extraTimeTeam1 == null) {
                    scoreTeam1Narrow.text = resultTeam1.toString()
                } else {
                    scoreTeam1Narrow.text = extraTimeTeam1.toString()
                }

                val scoreTeam2Narrow =
                    binding.root.findViewById<TextView>(R.id.item_text_match_score_team2_narrow)
                if (extraTimeTeam2 == null) {
                    scoreTeam2Narrow.text = resultTeam2.toString()
                } else {
                    scoreTeam2Narrow.text = extraTimeTeam2.toString()
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
                    binding.textExtraResultMessageNarrow.visibility = View.INVISIBLE
                }
            }

            binding.executePendingBindings()
        }

        private fun showPreMatchContainer() {
            with(binding) {
                preMatchContainerNarrow.visibility = View.VISIBLE
                postMatchContainerNarrow.visibility = View.INVISIBLE
            }
        }

        private fun showPostMatchContainer() {
            with(binding) {
                preMatchContainerNarrow.visibility = View.INVISIBLE
                postMatchContainerNarrow.visibility = View.VISIBLE
            }
        }

        private fun showExtraTimeMessage() {
            with(binding.textExtraResultMessageNarrow) {
                text = context.getString(R.string.matches_after_extra_time)
                visibility = View.VISIBLE
            }
        }

        private fun showPenaltiesMessage() {
            with(binding.textExtraResultMessageNarrow) {
                text = context.getString(R.string.matches_after_penalties)
                visibility = View.VISIBLE
            }
        }
    }
}