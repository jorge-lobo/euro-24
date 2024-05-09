package com.example.euro24.data.venues

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Venue(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("cityEN") var cityEN: String? = null,
    @SerializedName("cityDE") var cityDE: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("stadiumName") var stadiumName: String? = null,
    @SerializedName("stadiumAlias") var stadiumAlias: String? = null,
    @SerializedName("capacity") var capacity: Int? = null,
    @SerializedName("matchesId") var matchesId: ArrayList<Int> = arrayListOf(),
    @SerializedName("population") var population: Int? = null,
    @SerializedName("elevation") var elevation: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readArrayList(Int::class.java.classLoader) as ArrayList<Int>,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(cityEN)
        parcel.writeString(cityDE)
        parcel.writeString(state)
        parcel.writeString(stadiumName)
        parcel.writeString(stadiumAlias)
        parcel.writeValue(capacity)
        parcel.writeList(matchesId)
        parcel.writeValue(population)
        parcel.writeValue(elevation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Venue> {
        override fun createFromParcel(parcel: Parcel): Venue {
            return Venue(parcel)
        }

        override fun newArray(size: Int): Array<Venue?> {
            return arrayOfNulls(size)
        }
    }
}
