package com.github.gerard.solutions

import com.github.gerard.solutions.core.Onnx
import org.opencv.core.Size
import java.io.File
import java.io.FileOutputStream
import kotlin.io.path.createTempDirectory

class OnnxModel(val prefix: String) : Model(prefix) {

    override fun predict(imageBytes: ByteArray): Boolean {
        val imagesDir = createTempDirectory("hcaptcha-%s".format(System.currentTimeMillis())).toFile()
        val imageFile = File(imagesDir, "%s.jpg".format(System.currentTimeMillis()))

        FileOutputStream(imageFile).use { writer ->
            writer.write(imageBytes)
        }

        val result = Onnx.getResult(this.modelPath, imageFile, Size(64.0, 64.0))
        return result.minLoc.x > result.maxLoc.x
    }
}