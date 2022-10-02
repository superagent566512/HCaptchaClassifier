package com.github.gerard;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class HClassifierDemo {

    public static void main(String[] args) throws Exception {

        System.loadLibrary("opencv_java460");

        HClassifier classifier = new HClassifier();

        String maleLion = "https://i.imgur.com/KOjLHWA.jpg";
        String giraffe = "https://i.imgur.com/nvMDiij.jpg";
        String bedroom = "https://i.imgur.com/GVZDdz4.jpg";

        URL url = new URL(bedroom);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n;
            byte [] buffer = new byte[1024];

            while (-1 != (n = inputStream.read(buffer))) {
                byteArray.write(buffer, 0, n);
            }
        }

        boolean prediction = classifier.predict(byteArray.toByteArray(), "female lion", "en");

        System.out.println("Prediction: " + prediction);

    }

}
