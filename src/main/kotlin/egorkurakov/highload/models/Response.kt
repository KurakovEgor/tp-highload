package egorkurakov.highload.models

import com.sun.javafx.util.Logging
import java.io.File
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import java.util.logging.Logger

/**
 * Created by egor on 25.02.18.
 */
class Response private constructor(private val code : Int,
                                   private val status : String,
                                   private val needToSendFile: Boolean = false,
                                   private val file : File? = null ) {

    companion object {
        val log = Logger.getLogger(this::class.java.name)
    }

    constructor(file: File, needToSendFile: Boolean = true) : this(200, "OK", needToSendFile, file) { }

    constructor(clientErrorException: Exceptions.ClientErrorException) : this(clientErrorException.code, clientErrorException.status) { }

    fun send(socket: Socket) {
        if (code != 200) {
            log.info(code.toString() + " " + status + "\r\n")
            if (needToSendFile)
                log.info(file?.path + "\r\n")
        }
        socket.getOutputStream().write(getHeader().toByteArray())
        if (needToSendFile) {
                socket.getOutputStream().write(file?.readBytes())
        }


    }

    private fun getHeader() : String {
        return "HTTP/1.1 ${code} ${status}\r\n" +
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