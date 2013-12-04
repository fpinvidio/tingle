package com.plip.imageprocessing.algorithms;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ZhangSuenProcessor {

	public Mat thinningIteration(Mat image, int iteration) {
		Mat marker = Mat.zeros(image.size(), CvType.CV_8UC1);
		for (int i = 1; i < image.rows() - 1; i++) {
			for (int j = 1; j < image.cols() - 1; j++) {
				double[] p2 = image.get(i - 1, j);
				double[] p3 = image.get(i - 1, j + 1);
				double[] p4 = image.get(i, j + 1);
				double[] p5 = image.get(i + 1, j + 1);
				double[] p6 = image.get(i + 1, j);
				double[] p7 = image.get(i + 1, j - 1);
				double[] p8 = image.get(i, j - 1);
				double[] p9 = image.get(i - 1, j - 1);
				int A = 0;
				int B = 0;
				if (getPointValue(p2) == 0 && getPointValue(p3) == 1) {
					A++;
				}
				if (getPointValue(p3) == 0 && getPointValue(p4) == 1) {
					A++;
				}
				if (getPointValue(p4) == 0 && getPointValue(p5) == 1) {
					A++;
				}
				if (getPointValue(p5) == 0 && getPointValue(p6) == 1) {
					A++;
				}
				if (getPointValue(p6) == 0 && getPointValue(p7) == 1) {
					A++;
				}
				if (getPointValue(p7) == 0 && getPointValue(p8) == 1) {
					A++;
				}
				if (getPointValue(p8) == 0 && getPointValue(p9) == 1) {
					A++;
				}
				if (getPointValue(p9) == 0 && getPointValue(p2) == 1) {
					A++;
				}

				B = getPointValue(p2) + getPointValue(p3) + getPointValue(p4)
						+ getPointValue(p5) + getPointValue(p6)
						+ getPointValue(p7) + getPointValue(p8)
						+ getPointValue(p9);

				int m1 = iteration == 0 ? getPointValue(p2) * getPointValue(p4)
						* getPointValue(p6) : getPointValue(p2)
						* getPointValue(p4) * getPointValue(p8);
				int m2 = iteration == 0 ? getPointValue(p4) * getPointValue(p6)
						* getPointValue(p8) : getPointValue(p2)
						* getPointValue(p6) * getPointValue(p8);
				if (A == 1 && (B >= 2 && B <= 6) && m1 == 0 && m2 == 0) {
					marker.put(i, j, new double[] { 1.0 });
				}
			}
		}
		return marker;
	}

	public Mat thinning(Mat image) {
		Mat ones = Mat.ones(image.size(), CvType.CV_8U);
		image.mul(ones, 1 / 255);
		Mat previous = Mat.zeros(image.size(), CvType.CV_8UC1);
		Mat diff = new Mat();
		do {
			thinningIteration(image, 0);
			thinningIteration(image, 1);
			Core.absdiff(image, previous, diff);
			image.copyTo(previous);
		} while (Core.countNonZero(diff) > 0);
		image.mul(ones, 255);
		return image;
	}

	public int getPointValue(double[] point) {
		return (int) point[0];
	}

}
