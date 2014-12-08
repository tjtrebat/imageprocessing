package codec;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;

public class ImageEncoder {

	private BufferedImage image;

	public ImageEncoder(BufferedImage image) {
		this.image = image;
	}

	public int[][] encode() {
		int width = this.getImage().getWidth();
		int height = this.getImage().getHeight();
		// get array of RGB values
		int[] rgbArray = this.getImage().getRGB(0, 0, width, height, null, 0,
				width);
		int[][] blocks = new int[3][rgbArray.length];
		for (int component = 0; component < 3; component++) {
			for (int x = 0; x < width; x += 8) {
				for (int y = 0; y < rgbArray.length; y += 8 * width) {
					int[] block = new int[64];
					for (int i = 0; i < block.length; i++) {
						// convert RGB to YCbCr values
						int[] rgb = this.getRGB(rgbArray[x + y + width
								* (i / 8) + (i % 8)]);
						switch (component) {
						case 0:
							block[i] = this.getYComponent(rgb);
							break;
						case 1:
							block[i] = this.getCbComponent(rgb);
							break;
						case 2:
							block[i] = this.getCrComponent(rgb);
							break;
						}
						// shift down by 128
						block[i] -= 128;
					}
					// compute DCT
					double[] dct = this.computeDCT(block);
					// quantization
					for (int i = 0; i < block.length; i++)
						block[i] = (int) Math.round(dct[i]
								/ Constants.getQuantizationValues()[i]);
					// zigzag
					int[] zigzag = new int[block.length];
					// TODO: Encode DC coefficient separately
					// Encode AC coefficients
					int n = 0, k = 0;
					for (int i = 0; i < 15; i++) {
						int m = i % 8;
						for (int j = 0; j < ((i / 8 > 0) ? 8 - m - 1 : m + 1); j++) {
							if (j != 0)
								k += 7 * ((i % 2 > 0) ? 1 : -1);
							zigzag[n++] = block[k];
						}
						if ((i + 1) / 8 > 0)
							k += ((i % 2 > 0) ? 1 : 8);
						else
							k += ((i % 2 > 0) ? 8 : 1);
					}
					for (int i = 0; i < zigzag.length; i++)
						blocks[component][8 * y / width + x * height + i] = zigzag[i];
				}
			}
		}
		return blocks;
	}

	public String encode(int[][] blocks, Map<Object, String> prefixCodes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j += 64) {
				int runlength = 0;
				for (int k = 0; k < 64; k++) {
					if (blocks[i][j + k] != 0) {
						sb.append(prefixCodes.get(runlength));
						sb.append(prefixCodes.get(blocks[i][j + k]));
						runlength = 0;
					} else
						runlength++;
				}
				sb.append(prefixCodes.get(Constants.getEob()));
			}
		}
		return sb.toString();
	}

	public Map<Object, String> getHuffmanCodes(int[][] blocks) {
		// Huffman encode blocks
		Huffman huffman = new Huffman();
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j += 64) {
				int runlength = 0;
				for (int k = 0; k < 64; k++) {
					if (blocks[i][j + k] != 0) {
						huffman.addSymbol(runlength);
						huffman.addSymbol(blocks[i][j + k]);
						runlength = 0;
					} else
						runlength++;
				}
				huffman.addSymbol(Constants.getEob()); // EOB symbol
			}
		}
		huffman.buildCodeTable(huffman.buildTree(), "");
		return huffman.getPrefixCodes();
	}

	public double[] computeDCT(int[] block) {
		double[] dct = new double[64];
		for (int i = 0; i < dct.length; i++) {
			int u = i % 8;
			int v = i / 8;
			for (int j = 0; j < block.length; j++)
				dct[i] += block[j]
						* Math.cos((Math.PI * (1 + 2 * (j % 8)) * u) / 16)
						* Math.cos((Math.PI * (1 + 2 * (j / 8)) * v) / 16);
			dct[i] *= .25 * ((u == 0) ? 1 / Math.sqrt(2) : 1)
					* ((v == 0) ? 1 / Math.sqrt(2) : 1);
		}
		return dct;
	}

	public int[] getRGB(int rgb) {
		return new int[] { new Color(rgb).getRed(), new Color(rgb).getGreen(),
				new Color(rgb).getBlue() };
	}

	public int getYComponent(int[] rgb) {
		return Math.round(.299f * rgb[0] + .587f * rgb[1] + .114f * rgb[2]);
	}

	public int getCbComponent(int[] rgb) {
		return Math.round(128 - .168736f * rgb[0] - .331264f * rgb[1] + .5f
				* rgb[2]);
	}

	public int getCrComponent(int[] rgb) {
		return Math.round(128 + .5f * rgb[0] - .418688f * rgb[1] - .081312f
				* rgb[2]);
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
