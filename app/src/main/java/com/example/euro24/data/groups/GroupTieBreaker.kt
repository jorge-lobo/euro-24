package com.example.euro24.data.groups

import com.example.euro24.data.teams.Team

data class GroupTieBreaker(
    val team: Team,
    val pointsInMatchesBetweenTeams: Int,
    val goalDifferenceInMatchesBetweenTeams: Int,
    val goalsScoredInMatchesBetweenTeams: Int
)
