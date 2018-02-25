package egorkurakov.highload.models

/**
 * Created by egor on 25.02.18.
 */
class Request(requestLine: String) {
    val method: Method
    val path: String

    init {
        val requestList = requestLine.split(" ")
        when(requestList[0]) {
            ("GET") -> method = Method.GET
            ("HEAD") -> method = Method.HEAD
            else -> throw Exceptions.MethodNotAllowed()
        }
        path = requestList[1]
    }

    enum class Method {
        GET,
        HEAD
    }
}