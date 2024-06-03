package com.example.euro24.ui.matches.matchEditor.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentMatchEditorConfirmationBinding
import com.example.euro24.ui.bottomNav.BottomNavActivity
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.home.HomeFragment
import com.example.euro24.ui.matches.matchEditor.MatchEditorFragment

class MatchEditorConfirmationFragment : BaseFragment() {

    private lateinit var binding: FragmentMatchEditorConfirmationBinding
    private val mMatchEditorConfirmationViewModel by lazy { ViewModelProvider(this)[MatchEditorConfirmationViewModel::class.java] }
    private lateinit var textTitle: TextView
    private lateinit var buttonYes: Button
    private lateinit var buttonNo: Button

    private var isSave = false
    private val matchId: Int
        get() = arguments?.getInt(ARG_MATCH_ID) ?: 0

    companion object {
        private const val ARG_IS_SAVE = "is_save"
        private const val ARG_MATCH_ID = "match_id"
        private const val ARG_TEAM1_ID = "team1_id"
        private const val ARG_TEAM2_ID = "team2_id"
        private const val ARG_TEAM1_SCORE = "team1_score"
        private const val ARG_TEAM2_SCORE = "team2_score"
        private const val ARG_TEAM1_EXTRA_TIME = "team1_extra_time"
        private const val ARG_TEAM2_EXTRA_TIME = "team2_extra_time"
        private const val ARG_TEAM1_PENALTIES = "team1_penalties"
        private const val ARG_TEAM2_PENALTIES = "team2_penalties"

        fun newInstance(
            isSave: Boolean,
            matchId: Int,
            team1Id: Int,
            team2Id: Int,
            team1Score: Int,
            team2Score: Int,
            team1ExtraTime: Int,
            team2ExtraTime: Int,
            team1Penalties: Int,
            team2Penalties: Int
        ): MatchEditorConfirmationFragment {
            val fragment = MatchEditorConfirmationFragment()
            val args = Bundle()
            args.putBoolean(ARG_IS_SAVE, isSave)
            args.putInt(ARG_MATCH_ID, matchId)
            args.putInt(ARG_TEAM1_ID, team1Id)
            args.putInt(ARG_TEAM2_ID, team2Id)
            args.putInt(ARG_TEAM1_SCORE, team1Score)
            args.putInt(ARG_TEAM2_SCORE, team2Score)
            args.putInt(ARG_TEAM1_EXTRA_TIME, team1ExtraTime)
            args.putInt(ARG_TEAM2_EXTRA_TIME, team2ExtraTime)
            args.putInt(ARG_TEAM1_PENALTIES, team1Penalties)
            args.putInt(ARG_TEAM2_PENALTIES, team2Penalties)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_match_editor_confirmation,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = mMatchEditorConfirmationViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        mMatchEditorConfirmationViewModel.apply {
            initialize(matchId)
        }

        setupViews()
        checkArguments()
        setupObservers()
        setupListeners()
    }

    private fun setupViews() {
        textTitle = binding.textDialogTitle
        buttonYes = binding.buttonCtaDialogYes
        buttonNo = binding.buttonCtaDialogNo

        val team1Score = arguments?.getInt(ARG_TEAM1_SCORE) ?: 0
        val team2Score = arguments?.getInt(ARG_TEAM2_SCORE) ?: 0
        val team1ExtraTime = arguments?.getInt(ARG_TEAM1_EXTRA_TIME) ?: 0
        val team2ExtraTime = arguments?.getInt(ARG_TEAM2_EXTRA_TIME) ?: 0
        val team1Penalties = arguments?.getInt(ARG_TEAM1_PENALTIES) ?: 0
        val team2Penalties = arguments?.getInt(ARG_TEAM2_PENALTIES) ?: 0
        with(binding) {
            if (team1ExtraTime >= 0) {
                textTeam1ScoreSave.text = team1ExtraTime.toString()
                textTeam2ScoreSave.text = team2ExtraTime.toString()
            } else {
                textTeam1ScoreSave.text = team1Score.toString()
                textTeam2ScoreSave.text = team2Score.toString()
            }
            textTeam1PenaltiesSave.text = team1Penalties.toString()
            textTeam2PenaltiesSave.text = team2Penalties.toString()

            if (team1ExtraTime >= 0 && team2ExtraTime >= 0 && team1Penalties == 0 && team2Penalties == 0) {
                showExtraTimeMessage()
            } else if (team1Penalties > 0 || team2Penalties > 0) {
                showPenaltiesScore()
                showPenaltiesMessage()
            } else {
                binding.textExtraMessageSave.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupObservers() {
        with(mMatchEditorConfirmationViewModel) {
            team1ImageResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.imageTeam1FlagSave.setImageResource(resourceId)
            }

            team2ImageResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.imageTeam2FlagSave.setImageResource(resourceId)
            }
        }
    }

    private fun setupListeners() {
        buttonYes.setOnClickListener {
            if (isSave) {
                setupSaveMatch()
                openHomeFragment()
            } else {
                openHomeFragment()
            }
        }

        buttonNo.setOnClickListener {
            val matchId = arguments?.getInt(ARG_MATCH_ID) ?: 0
            returnToEditor(matchId)
        }
    }

    private fun checkArguments() {
        arguments?.let {
            isSave = it.getBoolean(ARG_IS_SAVE)

            if (isSave) {
                setupSaveUI()
            } else {
                setupCancelUI()
            }
        }
    }

    private fun setupCancelUI() {
        textTitle.text = getString(R.string.match_editor_title_confirm_cancel)

        val cancelConfirmationContainer = binding.dialogCancelConfirmationContainer
        cancelConfirmationContainer.visibility = View.VISIBLE
    }

    private fun setupSaveUI() {
        textTitle.text = getString(R.string.match_editor_title_confirm_changes)

        val saveConfirmationContainer = binding.dialogSaveConfirmationContainer
        saveConfirmationContainer.visibility = View.VISIBLE
    }

    private fun openHomeFragment() {
        (activity as? BottomNavActivity)?.apply {
            openFragment(HomeFragment())
            hideMatchEditorFragmentContainer()
        }
    }

    private fun setupSaveMatch() {
        val matchId = arguments?.getInt(ARG_MATCH_ID) ?: return
        val team1Score = arguments?.getInt(ARG_TEAM1_SCORE) ?: return
        val team2Score = arguments?.getInt(ARG_TEAM2_SCORE) ?: return
        val team1ExtraTime = arguments?.getInt(ARG_TEAM1_EXTRA_TIME) ?: return
        val team2ExtraTime = arguments?.getInt(ARG_TEAM2_EXTRA_TIME) ?: return
        val team1Penalties = arguments?.getInt(ARG_TEAM1_PENALTIES) ?: return
        val team2Penalties = arguments?.getInt(ARG_TEAM2_PENALTIES) ?: return

        with(mMatchEditorConfirmationViewModel) {
        saveMatchResults(
            matchId,
            team1Score,
            team2Score,
            team1ExtraTime,
            team2ExtraTime,
            team1Penalties,
            team2Penalties
        )
            saveTeamStats(matchId)
        }
    }

    private fun returnToEditor(matchId: Int) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragment = MatchEditorFragment.newInstance(
            matchId
        )

        fragmentManager.beginTransaction().apply {
            replace(R.id.match_editor_fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
        hideConfirmationContainer()
    }

    private fun hideConfirmationContainer() {
        val confirmationContainer = binding.dialogContainer
        confirmationContainer.visibility = View.GONE
    }

    private fun showExtraTimeMessage() {
        with(binding.textExtraMessageSave) {
            text = context.getString(R.string.matches_after_extra_time)
            visibility = View.VISIBLE
        }
    }

    private fun showPenaltiesMessage() {
        with(binding.textExtraMessageSave) {
            text = context.getString(R.string.matches_after_penalties)
            visibility = View.VISIBLE
        }
    }

    private fun showPenaltiesScore() {
        with(binding) {
            textTeam1PenaltiesSave.visibility = View.VISIBLE
            textTeam2PenaltiesSave.visibility = View.VISIBLE
        }
    }
}