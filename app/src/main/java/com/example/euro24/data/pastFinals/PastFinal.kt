package com.example.euro24.data.pastFinals

import com.google.gson.annotations.SerializedName

data class PastFinal(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("year") var year: Int? = null,
    @SerializedName("host") var host: String? = null,
    @SerializedName("winners") var winners: String? = null,
    @SerializedName("runnersUp") var runnersUp: String? = null
)
