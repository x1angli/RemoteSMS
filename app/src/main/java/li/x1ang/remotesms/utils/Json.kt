package li.x1ang.remotesms.utils

import com.google.gson.GsonBuilder

private val gson = GsonBuilder().create()

fun toJson(data: Any?): String {
    return gson.toJson(data)
}