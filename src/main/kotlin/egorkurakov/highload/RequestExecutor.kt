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
        val request = Request(BufferedReader(InputStreamReader(socketAccept.getInputStream())).readLine())
        val response : Response
        if (!isValid(request.path)) {
            response = Response(Exceptions.BadRequest())
            response.send(socketAccept)
            return
        }
        val file = File(config["directory"] + request.path)

        if (!file.isFile()) {
            response = Response(Exceptions.NotFound())
            response.send(socketAccept)
            return
        } else {
            if (request.method == Request.Method.HEAD) {
                response = Response(file, false)
            } else {
                response = Response(file)
            }
            response.send(socketAccept)
            return
        }
    }

    private fun isValid(path: String) : Boolean {
        if(path.contains("..")) {
            return false
        }
        return true
    }
}