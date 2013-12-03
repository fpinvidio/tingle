package imageprocessing.tray;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class SquareValidator {
	private static double[] allowed_areas = { 26000.0, 29000.0 };
	private static double allowed_area_tol = 0.08;
	
	public static boolean validateSquare(Mat square) {
		return hasAppropiateArea(square);
	}

	private static boolean hasAppropiateArea(Mat square) {
		boolean result = false;
		double area = Math.abs(Imgproc.contourArea(square));
		//System.out.println(area);
		for (double allowed_area : allowed_areas) {
			double upper_limit = allowed_area * (1 + allowed_area_tol);
			double lower_limit = allowed_area * (1 - allowed_area_tol);
			if (lower_limit < area && area < upper_limit) {
				result = true;
				break;
			}
		}
		return result;
	}
}
