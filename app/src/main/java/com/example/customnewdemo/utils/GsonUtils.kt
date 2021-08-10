package com.example.customnewdemo.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.lang.invoke.CallSite
import kotlin.contracts.ReturnsNotNull

class GsonUtils {

    private var gson: Gson = Gson()

    private constructor()

    companion object {
        val insance: GsonUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {

            GsonUtils()
        }
    }


    public fun toJson(any: Any): String {

        return gson.toJson(any)
    }


    public fun <T : Any> formJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz) as T
    }


    public fun <T : Any> formJsonOfList(json: String, clazz: Class<T>): List<T> {
        var list = arrayListOf<T>()
        try {
            var jsonArray: JsonArray = JsonParser().parse(json).asJsonArray

            for (jsonElement in jsonArray) {
                list.add(gson.fromJson(jsonElement, clazz))
            }


        } catch (e: Exception) {
            e.printStackTrace()

        }
        return list

    }
}