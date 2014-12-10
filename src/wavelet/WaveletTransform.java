package wavelet;

import java.awt.Color;

public abstract class WaveletTransform implements IWaveletTransform {

	private double[] h, g;
	private int width, height;
	private double[][] wavelet;

	public WaveletTransform(double[] h, double[] g, int width, int height) {
		this.h = h;
		this.g = g;
		this.width = width;
		this.height = height;
	}

	public void constructWaveletMatrix() {
		double[][] wavelet = new double[this.getHeight()][this.getWidth()];
		int offset = this.getHeight() - this.getH().length / 2;
		for (int i = 0; i < wavelet.length / 2; i++) {
			int k = offset;
			for (int j = 0; j < this.getH().length; j++) {
				wavelet[i][k] = this.getH()[j];
				k = (k + 1) % wavelet[i].length;
			}
			offset = (offset + 2) % wavelet[i].length;
		}
		offset = this.getHeight() - this.getG().length / 2;
		for (int i = wavelet.length / 2; i < wavelet.length; i++) {
			int k = offset;
			for (int j = 0; j < this.getG().length; j++) {
				wavelet[i][k] = this.getG()[j];
				k = (k + 1) % wavelet[i].length;
			}
			offset = (offset + 2) % wavelet[i].length;
		}
		this.setWavelet(wavelet);
	}

	@Override
	public void forward(int[] rgbArray, int imageWidth) {
		// apply the wavelet transformation to columns
		for (int x = 0; x < this.getWidth(); x++) {
			int[] col = new int[this.getHeight()];
			for (int y = 0; y < this.getHeight(); y++) {
				int[] rgb = new int[3];
				for (int i = 0; i < this.getHeight(); i++) {
					Color color = new Color(rgbArray[x + i * imageWidth]);
					rgb[0] += (int) (this.getWavelet()[y][i] * color.getRed());
					rgb[1] += (int) (this.getWavelet()[y][i] * color.getGreen());
					rgb[2] += (int) (this.getWavelet()[y][i] * color.getBlue());
				}
				for (int i = 0; i < rgb.length; i++)
					if (rgb[i] < 0)
						rgb[i] = 0;
					else if (rgb[i] > 255)
						rgb[i] = 255;
				col[y] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
			}
			// replace col in rgbArray
			for (int y = 0; y < this.getHeight(); y++)
				rgbArray[x + y * imageWidth] = col[y];
		}
		// apply wavelet transform to rows
		for (int y = 0; y < this.getHeight(); y++) {
			int[] row = new int[this.getWidth()];
			for (int x = 0; x < this.getWidth(); x++) {
				int[] rgb = new int[3];
				for (int i = 0; i < this.getWidth(); i++) {
					Color color = new Color(rgbArray[i + y * imageWidth]);
					rgb[0] += (int) (this.getWavelet()[x][i] * color.getRed());
					rgb[1] += (int) (this.getWavelet()[x][i] * color.getGreen());
					rgb[2] += (int) (this.getWavelet()[x][i] * color.getBlue());
				}
				for (int i = 0; i < rgb.length; i++)
					if (rgb[i] < 0)
						rgb[i] = 0;
					else if (rgb[i] > 255)
						rgb[i] = 255;
				row[x] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
			}
			// replace row in rgbArray
			for (int x = 0; x < this.getWidth(); x++)
				rgbArray[x + y * imageWidth] = row[x];
		}
	}

	public double[] getH() {
		return h;
	}

	public void setH(double[] h) {
		this.h = h;
	}

	public double[] getG() {
		return g;
	}

	public void setG(double[] g) {
		this.g = g;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double[][] getWavelet() {
		return wavelet;
	}

	public void setWavelet(double[][] wavelet) {
		this.wavelet = wavelet;
	}

}
