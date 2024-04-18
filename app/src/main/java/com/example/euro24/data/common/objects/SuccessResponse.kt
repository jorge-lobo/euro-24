package com.example.euro24.data.common.objects

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("message") val message: String,
    @SerializedName("id") val id: String?
)