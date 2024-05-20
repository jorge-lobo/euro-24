package com.example.euro24.ui.matches.matchesGroupStage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.data.groups.Group
import com.example.euro24.databinding.FragmentMatchesGroupStageBinding
import com.example.euro24.ui.common.BaseFragment

class MatchesGroupStageFragment : BaseFragment(), GroupListAdapter.OnItemClickListener {

    private lateinit var binding: FragmentMatchesGroupStageBinding
    private val mMatchesGroupStageViewModel by lazy { ViewModelProvider(this)[MatchesGroupStageViewModel::class.java] }
    private val groupListAdapter = GroupListAdapter()
    private val groupTableAdapter = GroupTableAdapter()
    private var isDropdownOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_matches_group_stage,
            container,
            false
        )
        binding.viewModel = mMatchesGroupStageViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupObservers()
        setupListeners()
    }

    override fun onItemClick(group: Group) {
        updateDropdownUI(group)
        mMatchesGroupStageViewModel.loadTeamsForGroup(group)
    }

    private fun setupRecyclerViews() {
        binding.rvGroupsList.apply {
            adapter = groupListAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
        groupListAdapter.setOnItemClickListener(this)

        binding.rvGroupTable.apply {
            adapter = groupTableAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        with(mMatchesGroupStageViewModel) {
            sortedGroups.observe(viewLifecycleOwner) { groups ->
                groups?.let {
                    val groupItems = groups.map { GroupListBindingItem(it) }
                    groupListAdapter.submitList(groupItems)
                }
            }

            textDropdownTitle.observe(viewLifecycleOwner) { dropdownTitle ->
                binding.textDropdownTitle.text = dropdownTitle
            }

            teamsInSelectedGroup.observe(viewLifecycleOwner) { teams ->
                val teamItems = teams.mapIndexed { index, team ->
                    GroupTableBindingItem(index + 1, team)
                }
                groupTableAdapter.submitList(teamItems)
            }
        }
    }

    private fun setupListeners() {
        binding.imageDropdownArrow.setOnClickListener {
            setupDropdown()
        }
    }

    private fun setupDropdown() {
        isDropdownOpen = !isDropdownOpen

        val newHeight = if (isDropdownOpen) R.dimen.dropdown_open_h else R.dimen.dropdown_close_h
        val layoutParams = binding.dropdownContainer.layoutParams
        layoutParams.height = resources.getDimensionPixelSize(newHeight)
        with(binding) {
            dropdownContainer.layoutParams = layoutParams
            tableContainer.visibility = if (isDropdownOpen) View.GONE else View.VISIBLE
            imageDropdownArrow.setImageResource(
                if (isDropdownOpen) R.drawable.icon_arrow_up else R.drawable.icon_arrow_down
            )
            rvGroupsList.visibility = if (isDropdownOpen) View.VISIBLE else View.GONE
        }
    }

    private fun updateDropdownUI(group: Group) {
        with(binding) {
            textDropdownTitle.text = group.groupName
        }
        closeDropdown()
    }

    private fun closeDropdown() {
        isDropdownOpen = false

        val newHeight = R.dimen.dropdown_close_h
        val layoutParams = binding.dropdownContainer.layoutParams
        layoutParams.height = resources.getDimensionPixelSize(newHeight)
        with(binding) {
            dropdownContainer.layoutParams = layoutParams
            tableContainer.visibility = View.VISIBLE
            imageDropdownArrow.setImageResource(R.drawable.icon_arrow_down)
            rvGroupsList.visibility = View.GONE
        }
    }

}