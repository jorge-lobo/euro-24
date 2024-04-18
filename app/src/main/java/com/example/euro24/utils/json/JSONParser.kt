package com.example.euro24.utils.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.threeten.bp.LocalDateTime
import java.io.Reader

class JSONParser<T> {

    companion object {
        fun getGson(): Gson {
            val gsonBuilder = GsonBuilder()

            gsonBuilder.registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            //gsonBuilder.registerTypeAdapter(Boolean::class.java, NumberToBooleanConverter())

            return gsonBuilder.create()
        }
    }

    fun deserialize(json: Reader, target: Class<T>): T {
        val gson = getGson()

        return gson.fromJson(
            json,
            target
        )
    }

    fun deserialize(json: String, target: Class<T>): T {
        val gson = getGson()

        return gson.fromJson(
            json,
            target
        )
    }
}