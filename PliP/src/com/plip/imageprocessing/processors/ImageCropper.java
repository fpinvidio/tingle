package com.plip.imageprocessing.processors;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class ImageCropper {

	public static Mat cropContour(Mat detectedObjects, Rect boundRect) {
		if (detectedObjects != null && boundRect != null) {

			if (boundRect.x < 0) {
				boundRect.x = 0;
			}
			if (boundRect.y < 0) {
				boundRect.y = 0;
			}
			if (boundRect.width + boundRect.x > detectedObjects.cols()) {
				boundRect.width = Math
						.abs(detectedObjects.cols() - boundRect.x);
			}
			if (boundRect.height + boundRect.y > detectedObjects.rows()) {
				boundRect.height = Math.abs(detectedObjects.rows()
						- boundRect.y);
			}

			Mat subImage = detectedObjects.submat(boundRect);
			return subImage;
		} else {
			return new Mat();
		}
	}
}
