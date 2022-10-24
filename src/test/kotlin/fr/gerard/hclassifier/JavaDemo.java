package fr.gerard.hclassifier;

public class JavaDemo {

    public static void main(String[] args) throws Exception {
        System.loadLibrary("opencv_java460");

        HClassifier classifier = new HClassifier();

        long starTime = System.currentTimeMillis();
        boolean prediction = classifier.predict("https://i.imgur.com/GVZDdz4.jpg", "bedroom", "en");
        long time = System.currentTimeMillis() - starTime;

        System.out.printf("Result: %s (%sms)%n", prediction, time);
    }
}
