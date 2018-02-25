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
        print("Invalid configuration file")
        return@runBlocking
    }
    val threadPoolContext = newFixedThreadPoolContext(config["threads"]!!.toInt(),"server-coroutines")
    val serverSocket = ServerSocket(config["port"]!!.toInt())
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