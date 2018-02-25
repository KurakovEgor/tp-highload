package egorkurakov.highload.models

import java.io.File
import java.net.Socket
import java.util.*

/**
 * Created by egor on 25.02.18.
 */
class Response private constructor(private val code : Int,
                                   private val status : String,
                                   private val needToSendFile: Boolean = false,
                                   private val file : File? = null ) {

    constructor(file: File, needToSendFile: Boolean = true) : this(200, "OK", needToSendFile, file) { }

    constructor(clientErrorException: Exceptions.BadRequest) : this(400, "Bad Request") { }

    constructor(clientErrorException: Exceptions.NotFound) : this(404, "Not Found") { }

    constructor(clientErrorException: Exceptions.MethodNotAllowed) : this(405, "Method Not Allowed") { }

    fun send(socket: Socket) {
        socket.getOutputStream().write(getHeader().toByteArray())
        if (needToSendFile) {
            socket.getOutputStream().write(file?.readBytes())
        }


    }

    private fun getHeader() : String {
        return "HTTP/1.1 ${code} ${status} \r\n" +
                "Date: ${Date()} \r\n" +
                "Server: highload \r\n" +
                "Connection: Close \r\n" +
                "Content-Length: ${file?.length()}\r\n" +
                "Content-Type: ${getContentType()}\r\n\r\n"
    }

    private fun getContentType(): String? =
        when (file?.extension) {
            "txt" -> "text/plain"
            "html" -> "text/html"
            "css" -> "text/css"
            "js" -> "text/javascript"
            "jpg" -> "image/jpeg"
            "jpeg" -> "image/jpeg"
            "gif" -> "image/gif"
            "png" -> "image/png"
            "swf" -> "application/x-shockwave-flash"
            else -> null
    }
}