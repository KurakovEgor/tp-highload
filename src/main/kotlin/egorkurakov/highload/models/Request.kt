package egorkurakov.highload.models

import java.net.URLDecoder

/**
 * Created by egor on 25.02.18.
 */
class Request(requestLine: String) {
    val method: Method
    var path: String
    val isIndex: Boolean

    init {
        val requestList = requestLine.split(" ")
        method = when(requestList[0]) {
            ("GET") -> Method.GET
            ("HEAD") -> Method.HEAD
            else -> throw Exceptions.MethodNotAllowed()
        }
        path = URLDecoder.decode(requestList[1], "UTF-8")
        path = path.substringBefore("?")
        if (path.endsWith("/")) {
            path += "index.html"
            isIndex = true
        } else {
            isIndex = false
        }
    }

    enum class Method {
        GET,
        HEAD
    }
}