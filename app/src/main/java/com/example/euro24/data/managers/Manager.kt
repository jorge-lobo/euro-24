package com.example.euro24.data.managers

import com.google.gson.annotations.SerializedName

data class Manager(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("team") var team: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("nationality") var nationality: String? = null,
    @SerializedName("placeBirth") var placeBirth: String? = null,
    @SerializedName("image") var image: String? = null
)
