package com.example.euro24.data.players

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("shortName") var shortName: String? = null,
    @SerializedName("fullName") var fullName: String? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("weight") var weight: Int? = null,
    @SerializedName("club") var club: String? = null,
    @SerializedName("clubCrest") var clubCrest: String? = null,
    @SerializedName("league") var league: String? = null,
    @SerializedName("nationality") var nationality: String? = null,
    @SerializedName("teamId") var teamId: Int? = null,
    @SerializedName("captain") var captain: Boolean? = null,
    @SerializedName("position") var position: String? = null,
    @SerializedName("foot") var foot: String? = null,
    @SerializedName("caps") var caps: Int? = null,
    @SerializedName("goals") var goals: Int? = null,
    @SerializedName("debutDate") var debutDate: String? = null,
    @SerializedName("debutOpponent") var debutOpponent: String? = null,
    @SerializedName("placeBirth") var placeBirth: String? = null,
    @SerializedName("number") var number: Int? = null,
    @SerializedName("image") var image: String? = null
)