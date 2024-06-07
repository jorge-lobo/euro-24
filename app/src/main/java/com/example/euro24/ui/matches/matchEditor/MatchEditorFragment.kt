package com.example.euro24.ui.matches.matchEditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentMatchEditorBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.matches.matchEditor.confirmation.MatchEditorConfirmationFragment
import com.example.euro24.utils.Utils

class MatchEditorFragment : BaseFragment() {

    private lateinit var binding: FragmentMatchEditorBinding
    private val mMatchEditorViewModel by lazy { ViewModelProvider(this)[MatchEditorViewModel::class.java] }
    private lateinit var buttonClose: ImageButton
    private lateinit var buttonSave: Button

    private var stage = MatchEditorViewModel.DateCondition.GROUP_STAGE

    private var isButtonSaveClicked = false

    private val matchId: Int
        get() = arguments?.getInt(ARG_MATCH_ID) ?: 0

    companion object {
        private const val ARG_MATCH_ID = "match_id"

        fun newInstance(matchId: Int): MatchEditorFragment {
            val fragment = MatchEditorFragment()
            fragment.arguments = Bundle().apply { putInt(ARG_MATCH_ID, matchId) }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_match_editor,
            container,
            false
        )
        binding.viewModel = mMatchEditorViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMatchEditorViewModel.apply {
            initialize(matchId)
            checkDateCondition()
        }

        setupObservers()
        setupViews()
        setupListeners()
    }

    private fun setupObservers() {
        with(mMatchEditorViewModel) {
            editionFinished.observe(viewLifecycleOwner) {
                if (isButtonSaveClicked) {
                    saveMatch()
                    isButtonSaveClicked = false
                }
            }

            dateCondition.observe(viewLifecycleOwner) { condition ->
                stage = condition ?: MatchEditorViewModel.DateCondition.GROUP_STAGE
            }

            team1ImageResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.editorImageTeam1Flag.setImageResource(resourceId)
            }

            team2ImageResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.editorImageTeam2Flag.setImageResource(resourceId)
            }

            observeTeamScore(
                mMatchEditorViewModel.team1Score,
                binding.team1ResultPickerScore.textScore
            )
            observeTeamScore(
                mMatchEditorViewModel.team2Score,
                binding.team2ResultPickerScore.textScore
            )
            observeTeamScore(
                mMatchEditorViewModel.team1ExtraTime,
                binding.team1ExtraTimePickerScore.textScore
            )
            observeTeamScore(
                mMatchEditorViewModel.team2ExtraTime,
                binding.team2ExtraTimePickerScore.textScore
            )
            observeTeamScore(
                mMatchEditorViewModel.team1Penalties,
                binding.team1PenaltiesPickerScore.textScore
            )
            observeTeamScore(
                mMatchEditorViewModel.team2Penalties,
                binding.team2PenaltiesPickerScore.textScore
            )

            team1ResultDecreaseButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                updatePickerButtonState(
                    binding.team1ResultPickerScore.buttonDecreaseScore.decreaseButton,
                    binding.team1ResultPickerScore.buttonDecreaseScore.decreaseIcon,
                    enabled
                )
            }

            team2ResultDecreaseButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                updatePickerButtonState(
                    binding.team2ResultPickerScore.buttonDecreaseScore.decreaseButton,
                    binding.team2ResultPickerScore.buttonDecreaseScore.decreaseIcon,
                    enabled
                )
            }

            team1ExtraTimeDecreaseButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                updatePickerButtonState(
                    binding.team1ExtraTimePickerScore.buttonDecreaseScore.decreaseButton,
                    binding.team1ExtraTimePickerScore.buttonDecreaseScore.decreaseIcon,
                    enabled
                )
            }

            team2ExtraTimeDecreaseButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                updatePickerButtonState(
                    binding.team2ExtraTimePickerScore.buttonDecreaseScore.decreaseButton,
                    binding.team2ExtraTimePickerScore.buttonDecreaseScore.decreaseIcon,
                    enabled
                )
            }

            team1PenaltiesDecreaseButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                updatePickerButtonState(
                    binding.team1PenaltiesPickerScore.buttonDecreaseScore.decreaseButton,
                    binding.team1PenaltiesPickerScore.buttonDecreaseScore.decreaseIcon,
                    enabled
                )
            }

            team2PenaltiesDecreaseButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                updatePickerButtonState(
                    binding.team2PenaltiesPickerScore.buttonDecreaseScore.decreaseButton,
                    binding.team2PenaltiesPickerScore.buttonDecreaseScore.decreaseIcon,
                    enabled
                )
            }

            resultTied.observe(viewLifecycleOwner) { resultTied ->
                val visibility = if (resultTied) View.VISIBLE else View.GONE
                updateExtraTimeVisibility(visibility)
            }

            extraTimeTied.observe(viewLifecycleOwner) { extraTimeTied ->
                val visibility = if (extraTimeTied) View.VISIBLE else View.GONE
                updatePenaltiesVisibility(visibility)
            }

            penaltiesTied.observe(viewLifecycleOwner) { penaltiesTied ->
                if (penaltiesTied) {
                    showErrorMessage()
                    buttonSave.isEnabled = false
                }
            }

            savedButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                buttonSave.isEnabled = enabled
            }
        }
    }

    private fun setupViews() {
        buttonClose = binding.buttonClose
        buttonSave = binding.buttonCtaSave
    }

    private fun setupListeners() {
        buttonClose.setOnClickListener {
            openConfirmationFragment(
                isSave = false,
                matchId = matchId,
                team1Id = mMatchEditorViewModel.team1Id.value ?: 0,
                team2Id = mMatchEditorViewModel.team2Id.value ?: 0,
                team1Score = mMatchEditorViewModel.team1Score.value ?: 0,
                team2Score = mMatchEditorViewModel.team2Score.value ?: 0,
                team1ExtraTime = mMatchEditorViewModel.team1ExtraTime.value ?: 0,
                team2ExtraTime = mMatchEditorViewModel.team2ExtraTime.value ?: 0,
                team1Penalties = mMatchEditorViewModel.team1Penalties.value ?: 0,
                team2Penalties = mMatchEditorViewModel.team2Penalties.value ?: 0
            )
        }

        buttonSave.setOnClickListener {
            isButtonSaveClicked = true
            mMatchEditorViewModel.handleSaveButton()
        }

        with(binding.team1ResultPickerScore) {
            buttonIncreaseScore.increaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(1, MatchEditorViewModel.ScoreType.RESULT, true)
            }

            buttonDecreaseScore.decreaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(1, MatchEditorViewModel.ScoreType.RESULT, false)
            }
        }

        with(binding.team2ResultPickerScore) {
            buttonIncreaseScore.increaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(2, MatchEditorViewModel.ScoreType.RESULT, true)
            }

            buttonDecreaseScore.decreaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(2, MatchEditorViewModel.ScoreType.RESULT, false)
            }
        }

        with(binding.team1ExtraTimePickerScore) {
            buttonIncreaseScore.increaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(
                    1,
                    MatchEditorViewModel.ScoreType.EXTRA_TIME,
                    true
                )
            }

            buttonDecreaseScore.decreaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(
                    1,
                    MatchEditorViewModel.ScoreType.EXTRA_TIME,
                    false
                )
            }
        }

        with(binding.team2ExtraTimePickerScore) {
            buttonIncreaseScore.increaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(
                    2,
                    MatchEditorViewModel.ScoreType.EXTRA_TIME,
                    true
                )
            }

            buttonDecreaseScore.decreaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(
                    2,
                    MatchEditorViewModel.ScoreType.EXTRA_TIME,
                    false
                )
            }
        }

        with(binding.team1PenaltiesPickerScore) {
            buttonIncreaseScore.increaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(1, MatchEditorViewModel.ScoreType.PENALTIES, true)
            }

            buttonDecreaseScore.decreaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(
                    1,
                    MatchEditorViewModel.ScoreType.PENALTIES,
                    false
                )
            }
        }

        with(binding.team2PenaltiesPickerScore) {
            buttonIncreaseScore.increaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(2, MatchEditorViewModel.ScoreType.PENALTIES, true)
            }

            buttonDecreaseScore.decreaseButton.setOnClickListener {
                mMatchEditorViewModel.changeScore(
                    2,
                    MatchEditorViewModel.ScoreType.PENALTIES,
                    false
                )
            }
        }
    }

    private fun observeTeamScore(teamScore: LiveData<Int>, scoreTextView: TextView) {
        teamScore.observe(viewLifecycleOwner) { score ->
            updateScore(scoreTextView, score)
        }
    }

    private fun openConfirmationFragment(
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
    ) {
        val fragment = MatchEditorConfirmationFragment.newInstance(
            isSave,
            matchId,
            team1Id,
            team2Id,
            team1Score,
            team2Score,
            team1ExtraTime,
            team2ExtraTime,
            team1Penalties,
            team2Penalties
        )
        parentFragmentManager.beginTransaction()
            .replace(R.id.confirmation_fragment_container, fragment)
            .commit()
        hideEditorContainer()
    }

    private fun updateScore(textView: TextView, score: Int) {
        textView.text = score.toString()
        textView.visibility = if (score < 0) View.INVISIBLE else View.VISIBLE
    }

    private fun updateExtraTimeVisibility(visibility: Int) {
        if (mMatchEditorViewModel.dateCondition.value == MatchEditorViewModel.DateCondition.KNOCKOUT) {
            with(binding) {
                team1ExtraTimeContainer.visibility = visibility
                team2ExtraTimeContainer.visibility = visibility

                val enableButtons = visibility != View.VISIBLE
                binding.apply {
                    team1ResultPickerScore.apply {
                        buttonIncreaseScore.increaseButton.isEnabled = enableButtons
                        buttonDecreaseScore.decreaseButton.isEnabled =
                            enableButtons && (mMatchEditorViewModel.team1Score.value ?: 0) > 0
                        updatePickerButtonState(
                            buttonIncreaseScore.increaseButton,
                            buttonIncreaseScore.increaseIcon,
                            enableButtons
                        )
                        updatePickerButtonState(
                            buttonDecreaseScore.decreaseButton,
                            buttonDecreaseScore.decreaseIcon,
                            enableButtons && (mMatchEditorViewModel.team1Score.value ?: 0) > 0
                        )
                    }
                    team2ResultPickerScore.apply {
                        buttonIncreaseScore.increaseButton.isEnabled = enableButtons
                        buttonDecreaseScore.decreaseButton.isEnabled =
                            enableButtons && (mMatchEditorViewModel.team2Score.value ?: 0) > 0
                        updatePickerButtonState(
                            buttonIncreaseScore.increaseButton,
                            buttonIncreaseScore.increaseIcon,
                            enableButtons
                        )
                        updatePickerButtonState(
                            buttonDecreaseScore.decreaseButton,
                            buttonDecreaseScore.decreaseIcon,
                            enableButtons && (mMatchEditorViewModel.team2Score.value ?: 0) > 0
                        )
                    }
                }
            }
        }
    }

    private fun updatePenaltiesVisibility(visibility: Int) {
        if (mMatchEditorViewModel.dateCondition.value == MatchEditorViewModel.DateCondition.KNOCKOUT) {
            with(binding) {
                team1PenaltiesContainer.visibility = visibility
                team2PenaltiesContainer.visibility = visibility

                val enableButtons = visibility != View.VISIBLE
                binding.apply {
                    team1ExtraTimePickerScore.apply {
                        buttonIncreaseScore.increaseButton.isEnabled = enableButtons
                        buttonDecreaseScore.decreaseButton.isEnabled = enableButtons
                        updatePickerButtonState(
                            buttonIncreaseScore.increaseButton,
                            buttonIncreaseScore.increaseIcon,
                            enableButtons
                        )
                        updatePickerButtonState(
                            buttonDecreaseScore.decreaseButton,
                            buttonDecreaseScore.decreaseIcon,
                            enableButtons
                        )
                    }
                    team2ExtraTimePickerScore.apply {
                        buttonIncreaseScore.increaseButton.isEnabled = enableButtons
                        buttonDecreaseScore.decreaseButton.isEnabled = enableButtons
                        updatePickerButtonState(
                            buttonIncreaseScore.increaseButton,
                            buttonIncreaseScore.increaseIcon,
                            enableButtons
                        )
                        updatePickerButtonState(
                            buttonDecreaseScore.decreaseButton,
                            buttonDecreaseScore.decreaseIcon,
                            enableButtons
                        )
                    }
                }
            }
        }
    }

    private fun updatePickerButtonState(button: Button, icon: ImageView, enabled: Boolean) {
        button.isEnabled = enabled
        icon.imageTintList = if (enabled) {
            ContextCompat.getColorStateList(
                requireContext(),
                R.color.match_editor_score_picker_icon_active
            )
        } else {
            ContextCompat.getColorStateList(
                requireContext(),
                R.color.match_editor_score_picker_icon_inactive
            )
        }
    }

    private fun saveMatch() {
        Utils.showToast(requireContext(), "Match saved successfully!")
        openConfirmationFragment(
            isSave = true,
            matchId = matchId,
            team1Id = mMatchEditorViewModel.team1Id.value ?: 0,
            team2Id = mMatchEditorViewModel.team2Id.value ?: 0,
            team1Score = mMatchEditorViewModel.team1Score.value ?: 0,
            team2Score = mMatchEditorViewModel.team2Score.value ?: 0,
            team1ExtraTime = mMatchEditorViewModel.team1ExtraTime.value ?: 0,
            team2ExtraTime = mMatchEditorViewModel.team2ExtraTime.value ?: 0,
            team1Penalties = mMatchEditorViewModel.team1Penalties.value ?: 0,
            team2Penalties = mMatchEditorViewModel.team2Penalties.value ?: 0
        )
    }

    private fun showErrorMessage() {
        Utils.showToast(requireContext(), "Penalties scores cannot be equal!")
    }

    private fun hideEditorContainer() {
        val editorContainer = view?.findViewById<RelativeLayout>(R.id.editor_container)
        editorContainer?.visibility = View.INVISIBLE
    }
}