package com.github.gerard

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.gerard.solutions.Model
import com.github.gerard.solutions.OnnxModel
import com.github.gerard.solutions.YoloModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class HClassifier {

    private val labelAliases = HashMap<String, HashMap<String, String>>()
    private val yolo = YoloModel()
    private val onnxModels = HashMap<String, OnnxModel>()

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
                val model = OnnxModel(prefix as String)
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

                onnxModels[model.prefix] = model
            }
        }
    }

    private fun switchModel(label: String, lang: String = "en"): Model {
        val alias = labelAliases[lang]?.get(label) ?: throw NotImplementedError(label)
        return onnxModels.getOrDefault(alias, null) ?: yolo
    }

    fun predict(image: ByteArray, label: String, lang: String = "en"): Boolean {
        val model = switchModel(label, lang)
        return model.predict(image)
    }

}