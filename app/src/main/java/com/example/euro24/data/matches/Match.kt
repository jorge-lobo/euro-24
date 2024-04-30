package com.example.euro24.data.matches

import com.google.gson.annotations.SerializedName

data class Match(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("phase") var phase: String? = null,
    @SerializedName("dayWeek") var dayWeek: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("time") var time: String? = null,
    @SerializedName("broadcastPT") var broadcastPT: String? = null,
    @SerializedName("venueId") var venueId: Int? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("team1") var team1: String? = null,
    @SerializedName("team1Id") var team1Id: Int? = null,
    @SerializedName("team2") var team2: String? = null,
    @SerializedName("team2Id") var team2Id: Int? = null,
    @SerializedName("resultTeam1") var resultTeam1: Int? = null,
    @SerializedName("resultTeam2") var resultTeam2: Int? = null,
    @SerializedName("extraTimeTeam1") var extraTimeTeam1: String? = null,
    @SerializedName("extraTimeTeam2") var extraTimeTeam2: String? = null,
    @SerializedName("penaltiesTeam1") var penaltiesTeam1: String? = null,
    @SerializedName("penaltiesTeam2") var penaltiesTeam2: String? = null
)