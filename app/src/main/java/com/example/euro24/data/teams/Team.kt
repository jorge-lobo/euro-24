package com.example.euro24.data.teams

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("qualifiedAs") var qualifiedAs: String? = null,
    @SerializedName("qualificationDate") var qualificationDate: String? = null,
    @SerializedName("numberAppearances") var numberAppearances: Int? = null,
    @SerializedName("previousAppearances") var previousAppearances: String? = null,
    @SerializedName("titles") var titles: Int? = null,
    @SerializedName("bestResult") var bestResult: String? = null,
    @SerializedName("headCoachId") var headCoachId: Int? = null,
    @SerializedName("headCoach") var headCoach: String? = null,
    @SerializedName("nicknameOG") var nicknameOG: String? = null,
    @SerializedName("nicknameEN") var nicknameEN: String? = null,
    @SerializedName("fifaCode") var fifaCode: String? = null,
    @SerializedName("kitBrand") var kitBrand: String? = null,
    @SerializedName("matchesListId") var matchesListId: ArrayList<Int> = arrayListOf(),
    @SerializedName("played") var played: Int? = null,
    @SerializedName("won") var won: Int? = null,
    @SerializedName("drawn") var drawn: Int? = null,
    @SerializedName("lost") var lost: Int? = null,
    @SerializedName("goalsFor") var goalsFor: Int? = null,
    @SerializedName("goalsAgainst") var goalsAgainst: Int? = null,
    @SerializedName("goalDifference") var goalDifference: Int? = null,
    @SerializedName("points") var points: Int? = null,
    @SerializedName("crestUrl") var crestUrl: String? = null,
    @SerializedName("squadList") var squadList: ArrayList<Int> = arrayListOf()
){
    val formattedGoalDifference: String
        get() = if (goalDifference!! > 0) "+$goalDifference" else goalDifference.toString()
}