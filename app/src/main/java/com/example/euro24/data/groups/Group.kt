package com.example.euro24.data.groups

import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("groupName") var groupName: String? = null,
    @SerializedName("team1") var team1: String? = null,
    @SerializedName("team2") var team2: String? = null,
    @SerializedName("team3") var team3: String? = null,
    @SerializedName("team4") var team4: String? = null,
    @SerializedName("teamsId") var teamsId: ArrayList<Int> = arrayListOf()
)