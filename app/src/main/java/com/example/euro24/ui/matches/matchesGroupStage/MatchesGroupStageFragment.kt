package com.example.euro24.ui.matches.matchesGroupStage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.data.groups.Group
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.databinding.FragmentMatchesGroupStageBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.matches.MatchNarrowCardAdapter

class MatchesGroupStageFragment : BaseFragment(), GroupListAdapter.OnItemClickListener {

    private lateinit var binding: FragmentMatchesGroupStageBinding
    private val mMatchesGroupStageViewModel by lazy { ViewModelProvider(this)[MatchesGroupStageViewModel::class.java] }
    private lateinit var teamRepository: TeamRepository
    private lateinit var groupListAdapter: GroupListAdapter
    private lateinit var groupTableAdapter: GroupTableAdapter
    private lateinit var groupMatchesAdapter: MatchNarrowCardAdapter
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

        teamRepository = TeamRepository(requireContext())
        groupListAdapter = GroupListAdapter()
        groupTableAdapter = GroupTableAdapter()
        groupMatchesAdapter = MatchNarrowCardAdapter(teamRepository)

        setupProgressBar()
        setupRecyclerViews()
        setupObservers()
        setupListeners()

        if (savedInstanceState == null) {
            mMatchesGroupStageViewModel.loadInitialGroup()
        }
    }

    override fun onItemClick(group: Group) {
        updateDropdownUI(group)
        mMatchesGroupStageViewModel.loadGroupData(group)
    }

    private fun setupProgressBar() {
        binding.loading.visibility = View.VISIBLE
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

        binding.rvGroupStageMatches.apply {
            adapter = groupMatchesAdapter
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
                teams?.let {
                    val teamItems = teams.mapIndexed { index, team ->
                        GroupTableBindingItem(index + 1, team)
                    }
                    groupTableAdapter.submitList(teamItems)
                }
            }

            matchesInSelectedGroup.observe(viewLifecycleOwner) { matches ->
                matches?.let {
                    groupMatchesAdapter.submitList(it)
                }
            }

            loadComplete.observe(viewLifecycleOwner) { isComplete ->
                if (isComplete == true) {
                    showUI()
                }
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

    private fun showUI() {
        with(binding) {
            whiteContainer.visibility = View.VISIBLE
            matchesGroupStageContainer.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
    }
}