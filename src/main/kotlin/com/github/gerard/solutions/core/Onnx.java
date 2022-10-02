package com.github.gerard.solutions.core;

import org.jetbrains.annotations.NotNull;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Onnx {

    public static Core.MinMaxLocResult getResult(@NotNull File modelPath, @NotNull File imagePath, Size imageSize) {
        Mat inputImg = Imgcodecs.imread(imagePath.getAbsolutePath());
        Mat blob = Dnn.blobFromImage(inputImg, 1 / 255.0, imageSize, new Scalar(0, 0), true, false);
        Net dnnNet = Dnn.readNetFromONNX(modelPath.getAbsolutePath());

        dnnNet.setInput(blob);

        Mat forwarded = dnnNet.forward();
        return Core.minMaxLoc(forwarded);
    }
}
