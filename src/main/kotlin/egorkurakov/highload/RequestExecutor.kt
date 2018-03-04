package egorkurakov.highload

import egorkurakov.highload.models.Exceptions
import egorkurakov.highload.models.Request
import egorkurakov.highload.models.Response
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.Socket

/**
 * Created by egor on 25.02.18.
 */
class RequestExecutor(val config: Map<String, String>) {
    fun executeRequest(socketAccept: Socket) {
        val request : Request
        val response : Response
        try {
            request = Request(BufferedReader(InputStreamReader(socketAccept.getInputStream())).readLine())
        } catch (ex: Exceptions.MethodNotAllowed) {
            response = Response(Exceptions.MethodNotAllowed())
            response.send(socketAccept)
            return
        }
        if (!isValid(request.path)) {
            response = Response(Exceptions.Forbidden())
            response.send(socketAccept)
            return
        }
        val file = File(config["document_root"] + request.path)
        response = if (!file.isFile()) {
            if (request.isIndex) {
                Response(Exceptions.Forbidden())
            } else {
                Response(Exceptions.NotFound())
            }
        } else {
            if (request.method == Request.Method.HEAD) {
                Response(file, false)
            } else {
                Response(file)
            }
        }
        response.send(socketAccept)
    }

    private fun isValid(path: String) : Boolean {
        if(path.contains("../")) {
            return false
        }
        return true
    }
}