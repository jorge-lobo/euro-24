package com.example.euro24.ui.matches.matchEditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentMatchEditorBinding
import com.example.euro24.ui.bottomNav.BottomNavActivity
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.home.HomeFragment

class MatchEditorFragment : BaseFragment() {

    private lateinit var binding: FragmentMatchEditorBinding
    private val mMatchEditorViewModel by lazy { ViewModelProvider(this)[MatchEditorViewModel::class.java] }
    private lateinit var buttonRTP: RadioButton
    private lateinit var buttonSIC: RadioButton
    private lateinit var buttonTVI: RadioButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var buttonClose: ImageButton

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
            dateCondition.observe(viewLifecycleOwner) { condition ->
                if (condition == MatchEditorViewModel.DateCondition.GROUP_STAGE) {
                    hideExtraTimeAndPenalties()
                }
            }

            team1ImageResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.editorImageTeam1Flag.setImageResource(resourceId)
            }

            team2ImageResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.editorImageTeam2Flag.setImageResource(resourceId)
            }

            broadcaster.observe(viewLifecycleOwner) { broadcaster ->
                when (broadcaster!!) {
                    MatchEditorViewModel.Broadcaster.RTP -> checkRadioButton(buttonRTP)
                    MatchEditorViewModel.Broadcaster.SIC -> checkRadioButton(buttonSIC)
                    MatchEditorViewModel.Broadcaster.TVI -> checkRadioButton(buttonTVI)
                    else -> uncheckRadioButtons()
                }
            }

            team1Score.observe(viewLifecycleOwner) { score ->
                updateScore(binding.team1ResultPickerScore.textScore, score)
            }

            team2Score.observe(viewLifecycleOwner) { score ->
                updateScore(binding.team2ResultPickerScore.textScore, score)
            }

            team1ResultDecreaseButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                with(binding.team1ResultPickerScore.buttonDecreaseScore) {
                    decreaseButton.isEnabled = enabled
                    decreaseIcon.imageTintList = if (enabled) ContextCompat.getColorStateList(
                        requireContext(), R.color.match_editor_score_picker_icon_active
                    ) else ContextCompat.getColorStateList(
                        requireContext(), R.color.match_editor_score_picker_icon_inactive
                    )
                }
            }

            team2ResultDecreaseButtonEnabled.observe(viewLifecycleOwner) { enabled ->
                with(binding.team2ResultPickerScore.buttonDecreaseScore) {
                    decreaseButton.isEnabled = enabled
                    decreaseIcon.imageTintList = if (enabled) ContextCompat.getColorStateList(
                        requireContext(), R.color.match_editor_score_picker_icon_active
                    ) else ContextCompat.getColorStateList(
                        requireContext(), R.color.match_editor_score_picker_icon_inactive
                    )
                }
            }

            resultsTied.observe(viewLifecycleOwner) { resultsTied ->
                val visibility = if (resultsTied) View.VISIBLE else View.GONE
                updateExtraTimeVisibility(visibility)
            }
        }
    }

    private fun setupViews() {
        buttonRTP = binding.rbTvRtpButton
        buttonSIC = binding.rbTvSicButton
        buttonTVI = binding.rbTvTviButton
        buttonDelete = binding.rbDelete
        buttonClose = binding.buttonClose
    }

    private fun setupListeners() {
        buttonClose.setOnClickListener {
            openHomeFragment()
        }

        buttonRTP.setOnClickListener {
            checkRadioButton(buttonRTP)
        }

        buttonSIC.setOnClickListener {
            checkRadioButton(buttonSIC)
        }

        buttonTVI.setOnClickListener {
            checkRadioButton(buttonTVI)
        }

        buttonDelete.setOnClickListener {
            uncheckRadioButtons()
        }

        with(binding.team1ResultPickerScore) {
            buttonIncreaseScore.increaseButton.setOnClickListener {
                mMatchEditorViewModel.increaseTeam1Score()
            }

            buttonDecreaseScore.decreaseButton.setOnClickListener {
                mMatchEditorViewModel.decreaseTeam1Score()
            }
        }

        with(binding.team2ResultPickerScore) {
            buttonIncreaseScore.increaseButton.setOnClickListener {
                mMatchEditorViewModel.increaseTeam2Score()
            }

            buttonDecreaseScore.decreaseButton.setOnClickListener {
                mMatchEditorViewModel.decreaseTeam2Score()
            }
        }
    }

    private fun openHomeFragment() {
        (activity as? BottomNavActivity)?.apply {
            openFragment(HomeFragment())
            hideMatchEditorFragmentContainer()
        }
    }

    private fun hideExtraTimeAndPenalties() {
        with(binding) {
            team1ExtraTimeContainer.visibility = View.GONE
            team1PenaltiesContainer.visibility = View.GONE
            team2ExtraTimeContainer.visibility = View.GONE
            team2PenaltiesContainer.visibility = View.GONE
            adjustTextViewMarginTop()
        }
    }

    private fun adjustTextViewMarginTop() {
        val textView = binding.labelBroadcastSelector
        val layoutParams = textView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = 164
        textView.layoutParams = layoutParams
    }

    private fun checkRadioButton(selectedButton: RadioButton) {
        buttonRTP.isChecked = selectedButton == buttonRTP
        buttonSIC.isChecked = selectedButton == buttonSIC
        buttonTVI.isChecked = selectedButton == buttonTVI
        buttonDelete.isEnabled = true
    }

    private fun uncheckRadioButtons() {
        buttonRTP.isChecked = false
        buttonSIC.isChecked = false
        buttonTVI.isChecked = false
        buttonDelete.isEnabled = false
    }

    private fun updateScore(textView: TextView, score: Int) {
        textView.text = score.toString()
        textView.visibility = if (score < 0) View.INVISIBLE else View.VISIBLE
    }

    private fun updateExtraTimeVisibility(visibility: Int) {
        with(binding) {
            team1ExtraTimeContainer.visibility = visibility
            team2ExtraTimeContainer.visibility = visibility
        }
    }
}