package fr.gerard.hclassifier.solutions

import java.io.DataInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Paths

abstract class OnnxModel(private val prefix: String) {

    val modelsDir = File(Paths.get("").toFile().absolutePath, "models").also { it.mkdirs() }
    val modelPath = File(modelsDir, "$prefix.onnx")

    fun update(forceUpdate: Boolean) {
        if (!forceUpdate && modelPath.exists() && modelPath.name.endsWith(".onnx")) {
            return
        }

        val connection = URL("https://github.com/QIN2DIM/hcaptcha-challenger/releases/download/model/%s.onnx".format(prefix)).openConnection() as HttpURLConnection
        connection.setRequestProperty("User-Agent", "Mozilla/5.0")

        val length = connection.getHeaderField("Content-Length").toLong()

        if (modelPath.exists() && modelPath.length() == length) {
            println("Model $prefix already exists")
            connection.inputStream.close()
            connection.disconnect()
        } else {
            println("Downloading $prefix.onnx")

            val reader = DataInputStream(connection.inputStream)
            val bytes = reader.readBytes()

            FileOutputStream(modelPath).use {
                it.write(bytes)
                it.flush()
            }
        }
    }

    abstract fun predict(imageBytes: ByteArray): Boolean
}