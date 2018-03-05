package egorkurakov.highload

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.runBlocking
import egorkurakov.highload.config.ServerConfig
import java.net.ServerSocket

/**
 * Created by egor on 18.02.18.
 */

fun main(args: Array<String>) = runBlocking {
    val config : Map<String, String>
    try {
        config = ServerConfig().config
    } catch (ex: ServerConfig.InvalidConfig) {
        println("Invalid configuration file")
        if (ex.description != null)
            println(ex.description)
        if (ex.configFile != null)
            println(ex.configFile)
        return@runBlocking
    }
    val threadPoolContext = newFixedThreadPoolContext(config["cpu_limit"]!!.toInt(),"server-coroutines")
    val port = config["listen"]
    val serverSocket = ServerSocket(port!!.toInt())
    println("Server started on port $port")
    while (true) {
        val socketAccept = serverSocket.accept()
        launch (threadPoolContext){
            try {
                RequestExecutor(config).executeRequest(socketAccept)
            } catch (ignore : Exception) {

            } finally {
                socketAccept.close()
            }
        }
    }
}