package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.exceptions.MobileLoginResponseException
import com.makentoshe.habrachan.network.request.LoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse

class MobileLoginDeserializer {

    fun error(request: LoginRequest, string: String): Result<LoginResponse> {
        val string2 = string.replace("'", "\"")
        var email : String? = null
        var password: String? = null

        if (string2.contains("form_errors_show")) {
            val json = Gson().fromJson(string2.findAllJson().firstOrNull(), Map::class.java) as Map<String, String>
            email = json["email"]
            password = json["password"]
        }

        // TODO(medium) regex doesn't works well, so this is a temporal workaround
        var other: String? = null
        if (string2.contains("show_global_notice")) {
            other = string2.split("show_global_notice(\"")[1].split("\", ")[0]
        }

        return Result.failure(MobileLoginResponseException(request, string, email, password, other))
    }


    private fun String.findAllJson(): List<String> {
        val stack: MutableList<Char> = ArrayList()
        val jsons: MutableList<String> = ArrayList()
        var temp = ""
        for (eachChar in toCharArray()) {
            if (stack.isEmpty() && eachChar == '{') {
                stack.add(eachChar)
                temp += eachChar
            } else if (stack.isNotEmpty()) {
                temp += eachChar
                if (stack[stack.size - 1] == '{' && eachChar == '}') {
                    stack.removeAt(stack.size - 1)
                    if (stack.isEmpty()) {
                        jsons.add(temp)
                        temp = ""
                    }
                } else if (eachChar == '{' || eachChar == '}') stack.add(eachChar)
            } else if (temp.isNotEmpty() && stack.isEmpty()) {
                jsons.add(temp)
                temp = ""
            }
        }
        return jsons
    }

}