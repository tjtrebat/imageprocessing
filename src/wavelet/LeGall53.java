package wavelet;

public class LeGall53 extends WaveletTransform {

	// lowpass filter
	private static final double[] h = { -1d / 8, 1d / 4, 3d / 4, 1d / 4, -1d / 8 };
	// highpass filter
	private static final double[] g = { -1d / 2, 1d, -1d / 2 };

	public LeGall53(int width, int height) {
		super(h, g, width, height);
	}

	@Override
	public void constructWaveletMatrix() {
		double[][] wavelet = new double[this.getHeight()][this.getWidth()];
		wavelet[0][0] = this.getH()[2];
		for (int j = 1; j < 3; j++)
			wavelet[0][j] = 2 * this.getH()[2 + j];
		int offset = 0;
		for (int i = 1; i < this.getHeight() / 2; i++) {
			for (int j = offset; j < offset + this.getH().length; j++)
				if (j < wavelet[i].length)
					wavelet[i][j] = this.getH()[j - offset];
			offset += 2;
		}
		// add h[0] to h[2] in the last row
		wavelet[this.getHeight() / 2 - 1][this.getHeight() - 2] += this.getH()[0];
		offset = 0;
		for (int i = this.getHeight() / 2; i < this.getHeight(); i++) {
			for (int j = offset; j < offset + this.getG().length; j++)
				if (j < wavelet[i].length)
					wavelet[i][j] = this.getG()[j - offset];
			offset += 2;
		}
		// add g[1] to g[1] in the last row
		wavelet[this.getHeight() - 1][this.getHeight() - 2] += this.getG()[0];
		this.setWavelet(wavelet);
	}

}
