package com.xstore.tms.taskunline

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type


class AbstractTaskAdapter : JsonSerializer<Any>, JsonDeserializer<Any> {

    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, type: Type,
                             jsonDeserializationContext: JsonDeserializationContext): Any {
        val jsonObj = jsonElement.asJsonObject
        val className = jsonObj.get(CLASS_META_KEY).asString
        try {
            val clz = Class.forName(className)
            return jsonDeserializationContext.deserialize(jsonElement, clz)
        } catch (e: ClassNotFoundException) {
            throw JsonParseException(e)
        }

    }

    override fun serialize(`object`: Any, type: Type,
                           jsonSerializationContext: JsonSerializationContext): JsonElement {
        val jsonEle = jsonSerializationContext.serialize(`object`, `object`.javaClass)
        jsonEle.asJsonObject.addProperty(CLASS_META_KEY,
                `object`.javaClass.canonicalName)
        return jsonEle
    }

    companion object {

        private val CLASS_META_KEY = "CLASS_META_KEY"
    }

}
