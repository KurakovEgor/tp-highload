package egorkurakov.highload.config

import java.io.File
import java.io.FileNotFoundException

/**
 * Created by egor on 20.02.18.
 */
class ServerConfig(configFile: File = defaultConfigFile) {

    val config = HashMap<String, String>()

    companion object {
        val defaultConfigFile = File("./server.conf")
    }

    init {
        try {
            configFile.readLines().forEach { val line = it.split("="); config[line[0]] = line[1] }
        } catch (ex: ClassCastException) {
            throw InvalidConfig()
        } catch (ex: FileNotFoundException) {
            throw InvalidConfig()
        }
        if (config["directory"] == null ) {
            config["directory"] = System.getProperty("user.dir")
        }
        if (!isValid()) {
            throw InvalidConfig()
        }
    }

    private fun isValid() : Boolean {
        return config["port"] != null &&
                config["threads"] != null &&
                config["directory"] != null
    }

    class InvalidConfig : RuntimeException() {

    }
}