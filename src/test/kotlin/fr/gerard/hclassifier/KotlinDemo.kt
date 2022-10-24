package fr.gerard.hclassifier

fun main() {
    System.loadLibrary("opencv_java460")

    val classifier = HClassifier()
    val prediction = classifier.predict("https://i.imgur.com/GVZDdz4.jpg", "bedroom", "en")

    println("Result: $prediction")
}