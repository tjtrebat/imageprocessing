package wavelet;

public class CDF97 extends WaveletTransform {

	// lowpass filter
	private static final double[] h = { 0.026748757411, -0.016864118443, -0.078223266529,
			0.266864118443, 0.602949018236, 0.266864118443, -0.078223266529,
			-0.016864118443, 0.026748757411 };
	// highpass filter
	private static final double[] g = { 0.091271763114, -0.057543526229, -0.591271763114,
			1.11508705, -0.591271763114, -0.057543526229, 0.091271763114 };

	public CDF97(int width, int height) {
		super(h, g, width, height);
	}

}
