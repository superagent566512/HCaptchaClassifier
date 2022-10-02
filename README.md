# HCaptchaClassifier

A fast solution to classify HCaptcha images. Made with the JVM.

## About:
Made with Kotlin and Java using the OpenCV library and ONNX models from QIN2DIM's hcaptcha-challenger project.

## Usage:

**Java**:
```java
public class HClassifierDemo {

    public static void main(String[] args) throws Exception {
        System.loadLibrary("opencv_java460");

        HClassifier classifier = new HClassifier();
        boolean prediction = classifier.predict("https://i.imgur.com/GVZDdz4.jpg", "bedroom", "en");

        System.out.println("Result: " + prediction);
    }
}
```

**Kotlin:**
```kotlin
fun main() {
    System.loadLibrary("opencv_java460")
    
    val classifier = HClassifier()
    val prediction = classifier.predict("https://i.imgur.com/GVZDdz4.jpg", "bedroom", "en")
    
    println("Result: $prediction")
}
``` 

## References:
ONNX pre-trained models: https://github.com/QIN2DIM/hcaptcha-challenger <br>
OpenCV for Java: https://github.com/bytedeco/javacv 
