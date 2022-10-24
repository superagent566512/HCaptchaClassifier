package fr.gerard.hclassifier

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import fr.gerard.hclassifier.solutions.OnnxModel
import fr.gerard.hclassifier.solutions.PluggableOnnxOnnxModel
import fr.gerard.hclassifier.solutions.YoloOnnxModel
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.net.URL


class HClassifier {

    private val labelAliases = HashMap<String, HashMap<String, String>>()
    private val yolo = YoloOnnxModel()
    private val pluggableOnnxModels = HashMap<String, PluggableOnnxOnnxModel>()

    init {
        labelAliases["en"] = mapOf(
            "airplane" to "airplane",
            "motorbus" to "bus",
            "bus" to "bus",
            "truck" to "truck",
            "motorcycle" to "motorcycle",
            "boat" to "boat",
            "bicycle" to "bicycle",
            "train" to "train",
            "car" to "car",
            "elephant" to "elephant",
            "bird" to "bird",
            "dog" to "dog",
            "canine" to "canine",
            "horse" to "horse",
            "giraffe" to "giraffe",
        ) as HashMap<String, String>

        yolo.update(false)

        BufferedReader(InputStreamReader(URL("https://raw.githubusercontent.com/QIN2DIM/hcaptcha-challenger/main/src/objects.yaml").openStream())).use { reader ->
            val text = reader.readText()
            val mapper = ObjectMapper(YAMLFactory())
            val map = mapper.readValue(text, MutableMap::class.java)
            val aliases = map["label_alias"] as Map<*, *>

            aliases.forEach { (prefix, value) ->
                val model = PluggableOnnxOnnxModel(prefix as String)
                model.update(false)
                val langs = value as Map<*, *>

                langs.forEach { (lang, value) ->
                    val alias = value as List<*>

                    for (aliasVal in alias) {
                        if (!labelAliases.containsKey(lang)) {
                            labelAliases[lang as String] = HashMap()
                        }

                        labelAliases[lang]?.set(aliasVal as String, prefix)
                    }
                }

                pluggableOnnxModels[model.prefix] = model
            }
        }
    }

    private fun switchModel(label: String, lang: String = "en"): OnnxModel {
        val alias = labelAliases[lang]?.get(label) ?: throw NotImplementedError(label)
        return pluggableOnnxModels.getOrDefault(alias, null) ?: yolo
    }

    fun predict(image: ByteArray, label: String, lang: String = "en"): Boolean {
        val model = switchModel(label, lang)
        return model.predict(image)
    }

    fun predict(imageUrl: String, label: String, lang: String = "en"): Boolean {
        val url = URL(imageUrl)
        val byteArray = ByteArrayOutputStream()

        url.openStream().use { inputStream ->
            var n: Int
            val buffer = ByteArray(1024)
            while (-1 != inputStream.read(buffer).also { n = it }) {
                byteArray.write(buffer, 0, n)
            }
        }
        return predict(byteArray.toByteArray(), label, lang)
    }

}