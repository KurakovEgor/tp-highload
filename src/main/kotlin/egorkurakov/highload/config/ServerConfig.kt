package egorkurakov.highload.config

import java.io.File
import java.io.FileNotFoundException

/**
 * Created by egor on 20.02.18.
 */
class ServerConfig(configFile: File = defaultConfigFile) {

    val config = HashMap<String, String>()

    companion object {
        val defaultConfigFile = File("./httpd.conf")
//        val defaultConfigFile = File("/etc/httpd.conf")
    }

    init {
        try {
            configFile.readLines().forEach { val line = it.split(" "); config[line[0]] = line[1] }
        } catch (ex: ClassCastException) {
            throw InvalidConfig("Class cast exception")
        } catch (ex: FileNotFoundException) {
            throw InvalidConfig("File not find")
        }
        if (config["document_root"] == null ) {
            config["document_root"] = System.getProperty("user.dir")
        }
        if (!isValid()) {
            throw InvalidConfig("Not valid config file", configFile)
        }
    }

    private fun isValid() : Boolean {
        return config["listen"] != null &&
                config["cpu_limit"] != null &&
                config["document_root"] != null
    }

    class InvalidConfig(val description: String? = null, val configFile: File? = null) : RuntimeException() {

    }
}