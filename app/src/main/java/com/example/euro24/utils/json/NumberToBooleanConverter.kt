package com.example.euro24.utils.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import java.lang.reflect.Type

internal class NumberToBooleanConverter : JsonDeserializer<Boolean?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement, typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Boolean? {
        if ((json as JsonPrimitive).isBoolean)
            return json.getAsBoolean()
        if (json.isString) {
            val jsonValue = json.getAsString()
            return if (jsonValue.equals("true", ignoreCase = true)) {
                true
            } else if (jsonValue.equals("false", ignoreCase = true)) {
                false
            } else {
                null
            }
        }
        val code = json.getAsInt()
        return if (code == 0) false else if (code == 1) true else null
    }
}