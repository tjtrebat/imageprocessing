package wavelet;

public class HaarWavelet extends WaveletTransform {

	private static final double[] h = { Math.sqrt(2) / 2, Math.sqrt(2) / 2 };
	private static final double[] g = { -Math.sqrt(2) / 2, Math.sqrt(2) / 2 };

	public HaarWavelet(int width, int height) {
		super(h, g, width, height);
		super.constructWaveletMatrix();
	}

}
