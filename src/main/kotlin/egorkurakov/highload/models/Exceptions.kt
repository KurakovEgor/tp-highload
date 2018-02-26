package egorkurakov.highload.models

/**
 * Created by egor on 25.02.18.
 */
class Exceptions {
    open class ClientErrorException : RuntimeException()
    class MethodNotAllowed : Exceptions.ClientErrorException()
    class NotFound : Exceptions.ClientErrorException()
    class Forbidden : Exceptions.ClientErrorException()
}