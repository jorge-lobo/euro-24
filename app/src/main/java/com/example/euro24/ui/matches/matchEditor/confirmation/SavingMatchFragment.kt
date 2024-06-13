package com.example.euro24.ui.matches.matchEditor.confirmation

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.euro24.R
import com.example.euro24.databinding.FragmentSavingMatchBinding
import com.example.euro24.ui.bottomNav.BottomNavActivity
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.home.HomeFragment

class SavingMatchFragment : BaseFragment() {

    private lateinit var binding: FragmentSavingMatchBinding
    private lateinit var btnTryAgain: Button
    private lateinit var textTitle: TextView
    private lateinit var textMessage: TextView
    private lateinit var icon: ImageView
    private lateinit var container: View

    companion object {
        private const val ARG_IS_SUCCESS = "is_success"
        private const val DELAY_MILLIS = 2000L

        fun newInstance(isSuccess: Boolean): SavingMatchFragment {
            val fragment = SavingMatchFragment()
            fragment.arguments = bundleOf(ARG_IS_SUCCESS to isSuccess)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_saving_match,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
        handleArguments()
    }

    private fun setupViews() {
        with(binding) {
            btnTryAgain = buttonTryAgain
            textTitle = textSaveMatchTitle
            textMessage = textSaveMatchMessage
            icon = imageIcon
            container = root.findViewById(R.id.dialog_background)
        }
    }

    private fun setupListeners() {
        btnTryAgain.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleArguments() {
        val isSuccess = arguments?.getBoolean(ARG_IS_SUCCESS) ?: false
        if (isSuccess) {
            showSuccessUI()
            Handler(Looper.getMainLooper()).postDelayed({
                openHomeFragment()
            }, DELAY_MILLIS)
        } else {
            showFailureUI()
        }
    }

    private fun showSuccessUI() {
        textTitle.text = getString(R.string.match_editor_title_success)
        textMessage.text = getString(R.string.match_editor_message_success)
        icon.setImageResource(R.drawable.icon_success)
        btnTryAgain.visibility = View.INVISIBLE
        setDialogBackgroundColor(R.color.dialog_success_background)
    }

    private fun showFailureUI() {
        textTitle.text = getString(R.string.match_editor_title_failure)
        textMessage.text = getString(R.string.match_editor_message_failure)
        icon.setImageResource(R.drawable.icon_failure)
        btnTryAgain.visibility = View.VISIBLE
        setDialogBackgroundColor(R.color.dialog_background)
    }

    private fun setDialogBackgroundColor(colorResId: Int) {
        val background = container.background as? GradientDrawable
        val color = resources.getColor(colorResId, null)
        background?.setColor(color)
    }

    private fun openHomeFragment() {
        (activity as? BottomNavActivity)?.apply {
            openFragment(HomeFragment())
            hideMatchEditorFragmentContainer()
        }
    }
}