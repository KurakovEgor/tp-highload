package egorkurakov.highload.models

/**
 * Created by egor on 25.02.18.
 */
class Exceptions {
    open class ClientErrorException(val code: Int, val status: String) : RuntimeException()
    class MethodNotAllowed : Exceptions.ClientErrorException(405, "Method Not Allowed")
    class NotFound : Exceptions.ClientErrorException(404, "Not Found")
    class Forbidden : Exceptions.ClientErrorException(403, "Forbidden")
}