package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import codec.ImageDecoder;
import codec.ImageEncoder;

public class ImageProcessing extends JFrame implements ActionListener {

	private static final long serialVersionUID = -8513927584316178739L;
	private String title;
	private Container cp;
	private JMenu mTransform;
	private JMenuItem miOpen, miSave, miExit;
	private JMenuItem miCopy, miJpegCodify, miHaar, miCDF97, miLeGall,
			miGrayscale;
	private JPanel panelSrc, panelDst;
	private JLabel imageSrc, imageDst;
	private JLabel imageSrcLbl, imageDstLbl;

	private JFileChooser fc;

	public ImageProcessing() {
		// JMenus
		this.mTransform = new JMenu("Transform");
		// JMenuItems
		this.miOpen = new JMenuItem("Open image");
		this.miSave = new JMenuItem("Save image");
		this.miExit = new JMenuItem("Exit");
		this.miCopy = new JMenuItem("Copy");
		this.miJpegCodify = new JMenuItem("JPEG Codify");
		this.miHaar = new JMenuItem("Haar Wavelet");
		this.miCDF97 = new JMenuItem("CDF 9/7 Wavelet");
		this.miLeGall = new JMenuItem("LeGall 5/3 Wavelet");
		this.miGrayscale = new JMenuItem("Gray scale");
		// ImagePanels
		this.panelSrc = new JPanel();
		this.panelDst = new JPanel();
		// ImageIcons
		this.imageSrc = new JLabel();
		this.imageDst = new JLabel();
		// JLabels
		this.imageSrcLbl = new JLabel();
		this.imageDstLbl = new JLabel();
		// JFileChooser
		this.fc = new JFileChooser();
		// Container
		this.cp = this.getContentPane();
		// create JMenuBar
		this.createJMenuBar();
		// add ImagePanels
		this.setupJPanels();
		// add ActionListeners
		this.addActionListeners();
	}

	public void createJMenuBar() {
		JMenuBar mb = new JMenuBar();
		// create JMenu for "File"
		JMenu menu = new JMenu("File");
		// add JMenuItems to menu
		menu.add(this.getMiOpen());
		menu.add(this.getMiSave());
		// add separator
		menu.addSeparator();
		// add JMenuItem for "Exit"
		menu.add(this.getMiExit());
		mb.add(menu); // add menu to mb
		// create JMenu for "Process"
		menu = new JMenu("Process");
		// add JMenuItems to menu
		menu.add(this.getMiCopy());
		menu.add(this.getMiJpegCodify());
		menu.add(this.getmTransform());
		this.getmTransform().add(this.getMiHaar());
		this.getmTransform().add(this.getMiCDF97());
		this.getmTransform().add(this.getMiLeGall());
		menu.add(this.getMiGrayscale());
		mb.add(menu); // add menu to mb
		setJMenuBar(mb);
	}

	public void addActionListeners() {
		this.getMiOpen().addActionListener(this);
		this.getMiSave().addActionListener(this);
		this.getMiSave().setEnabled(false);
		this.getMiExit().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.getMiCopy().addActionListener(this);
		this.getMiCopy().setEnabled(false);
		this.getMiJpegCodify().addActionListener(this);
		this.getMiJpegCodify().setEnabled(false);
		this.getMiHaar().addActionListener(this);
		this.getMiCDF97().addActionListener(this);
		this.getMiLeGall().addActionListener(this);
		this.getmTransform().setEnabled(false);
		this.getMiGrayscale().addActionListener(this);
		this.getMiGrayscale().setEnabled(false);
	}

	public void setupJPanels() {
		// setup panelSrc
		this.getPanelSrc().setPreferredSize(new Dimension(512, 512));
		this.getPanelSrc().setBorder(
				BorderFactory.createTitledBorder("No Image"));
		this.getPanelSrc().add(this.getImageSrc());
		this.getPanelSrc().add(this.getImageSrcLbl());
		// setup panelDst
		this.getPanelDst().setPreferredSize(new Dimension(512, 512));
		this.getPanelDst().setBorder(
				BorderFactory.createTitledBorder("No Image"));
		this.getPanelDst().add(this.getImageDst());
		this.getPanelDst().add(this.getImageDstLbl());
		// add JPanels to cp
		this.getCp().setLayout(new FlowLayout());
		this.getCp().add(this.getPanelSrc());
		this.getCp().add(this.getPanelDst());
	}

	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
		if ("Open image".equals(cmd)) {
			int retval = this.getFc().showOpenDialog(this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				BufferedImage bi = this.readImage(fc.getSelectedFile());
				if (bi != null) {
					int height = bi.getHeight();
					int width = bi.getWidth();
					this.getImageSrc().setIcon(new ImageIcon(bi));
					this.setBorderTitle(this.getPanelSrc(), "Original Image");
					this.getImageSrcLbl().setText(
							String.format("Image Dimension = %d X %d", width,
									height));
					this.getPanelSrc().setPreferredSize(
							new Dimension(width + 100, height + 100));
					this.getPanelDst().setPreferredSize(
							new Dimension(width + 100, height + 100));
					this.getMiCopy().setEnabled(true);
					this.getMiJpegCodify().setEnabled(false);
					this.getmTransform().setEnabled(false);
					this.getMiGrayscale().setEnabled(false);
					this.getMiSave().setEnabled(true);
					pack();
				} else
					this.getPanelSrc().removeAll();
				this.getImageDst().setIcon(null);
				this.getImageDstLbl().setText(null);
				this.setBorderTitle(this.getPanelDst(), "No Image");
			}
		} else if ("Save image".equals(cmd)) {
			int retval = this.getFc().showSaveDialog(this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				if (this.getImageDst().getIcon() == null)
					this.getImageDst().setIcon(this.getImageSrc().getIcon());
				try {
					ImageIO.write(
							(RenderedImage) this.getImage(this.getImageDst()),
							"png", this.getFc().getSelectedFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if ("Copy".equals(cmd)) {
			this.setBorderTitle(this.getPanelDst(), "Copy of Original Image");
			this.getImageDst().setIcon(this.getImageSrc().getIcon());
			this.getMiJpegCodify().setEnabled(true);
			this.getMiGrayscale().setEnabled(true);
		} else
			process(cmd);
	}

	public void process(String opName) {
		BufferedImageOp op = null;
		BufferedImage image = (BufferedImage) this.getImage(this.getImageDst());
		int width = image.getWidth();
		int height = image.getHeight();
		int[] rgbArray = image.getRGB(0, 0, width, height, null, 0, width);
		if (opName.equals("JPEG Codify")) {
			this.setBorderTitle(this.getPanelDst(), "Operation - JPEG Codify");
			this.getImageDst().setIcon(new ImageIcon(this.jpegCodify(image)));
		} else if (opName.equals("Haar Wavelet")) {
			this.setBorderTitle(this.getPanelDst(), "Operation - Haar Wavelet");
			double[] h = { Math.sqrt(2) / 2, Math.sqrt(2) / 2 };
			double[] g = { -Math.sqrt(2) / 2, Math.sqrt(2) / 2 };
			for (int n = 0; n < 3; n++) {
				double[][] wavelet = new double[height][height];
				int offset = 0;
				for (int i = 0; i < wavelet.length / 2; i++) {
					int k = offset;
					for (int j = 0; j < h.length; j++) {
						wavelet[i][k] = h[j];
						k = (k + 1) % wavelet[i].length;
					}
					offset = (offset + 2) % wavelet[i].length;
				}
				offset = 0;
				for (int i = wavelet.length / 2; i < wavelet.length; i++) {
					int k = offset;
					for (int j = 0; j < g.length; j++) {
						wavelet[i][k] = g[j];
						k = (k + 1) % wavelet[i].length;
					}
					offset = (offset + 2) % wavelet[i].length;
				}
				// apply the wavelet transformation to columns
				for (int x = 0; x < width; x++) {
					int[] col = new int[height];
					for (int y = 0; y < height; y++) {
						int[] rgb = new int[3];
						for (int i = 0; i < height; i++) {
							Color color = new Color(rgbArray[x + i
									* image.getWidth()]);
							rgb[0] += (int) (wavelet[y][i] * color.getRed());
							rgb[1] += (int) (wavelet[y][i] * color.getGreen());
							rgb[2] += (int) (wavelet[y][i] * color.getBlue());
						}
						for (int i = 0; i < rgb.length; i++)
							if (rgb[i] < 0)
								rgb[i] = 0;
							else if (rgb[i] > 255)
								rgb[i] = 255;
						col[y] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
					}
					// replace col in rgbArray
					for (int y = 0; y < height; y++)
						rgbArray[x + y * image.getWidth()] = col[y];
				}
				// apply wavelet transform to rows
				for (int y = 0; y < height; y++) {
					int[] row = new int[width];
					for (int x = 0; x < width; x++) {
						int[] rgb = new int[3];
						for (int i = 0; i < width; i++) {
							Color color = new Color(rgbArray[i + y
									* image.getWidth()]);
							rgb[0] += (int) (wavelet[x][i] * color.getRed());
							rgb[1] += (int) (wavelet[x][i] * color.getGreen());
							rgb[2] += (int) (wavelet[x][i] * color.getBlue());
						}
						for (int i = 0; i < rgb.length; i++)
							if (rgb[i] < 0)
								rgb[i] = 0;
							else if (rgb[i] > 255)
								rgb[i] = 255;
						row[x] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
					}
					// replace row in rgbArray
					for (int x = 0; x < width; x++)
						rgbArray[x + y * image.getWidth()] = row[x];
				}
				width /= 2;
				height /= 2;
			}
			BufferedImage bi = new BufferedImage(image.getWidth(),
					image.getHeight(), BufferedImage.TYPE_INT_RGB);
			bi.setRGB(0, 0, bi.getWidth(), bi.getHeight(), rgbArray, 0,
					bi.getWidth());
			this.getImageDst().setIcon(new ImageIcon(bi));
		} else if (opName.equals("CDF 9/7 Wavelet")) {
			this.setBorderTitle(this.getPanelDst(),
					"Operation - CDF 9/7 Wavelet");
			// lowpass filter
			float[] h = { 0.026748757411f, -0.016864118443f, -0.078223266529f,
					0.266864118443f, 0.602949018236f, 0.266864118443f,
					-0.078223266529f, -0.016864118443f, 0.026748757411f };
			// highpass filter
			float[] g = { 0.091271763114f, -0.057543526229f, -0.591271763114f,
					1.11508705f, -0.591271763114f, -0.057543526229f,
					0.091271763114f };
			for (int n = 0; n < 3; n++) {
				float[][] wavelet = new float[height][height];
				int offset = height - h.length / 2;
				for (int i = 0; i < wavelet.length / 2; i++) {
					int k = offset;
					for (int j = 0; j < h.length; j++) {
						wavelet[i][k] = h[j];
						k = (k + 1) % wavelet[i].length;
					}
					offset = (offset + 2) % wavelet[i].length;
				}
				offset = height - g.length / 2;
				for (int i = wavelet.length / 2; i < wavelet.length; i++) {
					int k = offset;
					for (int j = 0; j < g.length; j++) {
						wavelet[i][k] = g[j];
						k = (k + 1) % wavelet[i].length;
					}
					offset = (offset + 2) % wavelet[i].length;
				}
				// apply the wavelet transformation to columns
				for (int x = 0; x < width; x++) {
					int[] col = new int[height];
					for (int y = 0; y < height; y++) {
						int[] rgb = new int[3];
						for (int i = 0; i < height; i++) {
							Color color = new Color(rgbArray[x + i
									* image.getWidth()]);
							rgb[0] += (int) (wavelet[y][i] * color.getRed());
							rgb[1] += (int) (wavelet[y][i] * color.getGreen());
							rgb[2] += (int) (wavelet[y][i] * color.getBlue());
						}
						for (int i = 0; i < rgb.length; i++)
							if (rgb[i] < 0)
								rgb[i] = 0;
							else if (rgb[i] > 255)
								rgb[i] = 255;
						col[y] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
					}
					// replace col in rgbArray
					for (int y = 0; y < height; y++)
						rgbArray[x + y * image.getWidth()] = col[y];
				}
				// apply wavelet transform to rows
				for (int y = 0; y < height; y++) {
					int[] row = new int[width];
					for (int x = 0; x < width; x++) {
						int[] rgb = new int[3];
						for (int i = 0; i < width; i++) {
							Color color = new Color(rgbArray[i + y
									* image.getWidth()]);
							rgb[0] += (int) (wavelet[x][i] * color.getRed());
							rgb[1] += (int) (wavelet[x][i] * color.getGreen());
							rgb[2] += (int) (wavelet[x][i] * color.getBlue());
						}
						for (int i = 0; i < rgb.length; i++)
							if (rgb[i] < 0)
								rgb[i] = 0;
							else if (rgb[i] > 255)
								rgb[i] = 255;
						row[x] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
					}
					// replace row in rgbArray
					for (int x = 0; x < width; x++)
						rgbArray[x + y * image.getWidth()] = row[x];
				}
				width /= 2;
				height /= 2;
			}
			BufferedImage bi = new BufferedImage(image.getWidth(),
					image.getHeight(), BufferedImage.TYPE_INT_RGB);
			bi.setRGB(0, 0, bi.getWidth(), bi.getHeight(), rgbArray, 0,
					bi.getWidth());
			this.getImageDst().setIcon(new ImageIcon(bi));
		} else if (opName.equals("LeGall 5/3 Wavelet")) {
			this.setBorderTitle(this.getPanelDst(),
					"Operation - LeGall 5/3 Wavelet");
			// lowpass filter
			float[] h = { -1f / 8, 1f / 4, 3f / 4, 1f / 4, -1f / 8 };
			// highpass filter
			float[] g = { -1f / 2, 1, -1f / 2 };
			for (int n = 0; n < 3; n++) {
				float[][] wavelet = new float[height][height];
				// fill row 1 values
				wavelet[0][0] = h[2];
				for (int j = 1; j < 3; j++)
					wavelet[0][j] = 2 * h[2 + j];
				// fill the remaining rows
				int offset = 0;
				for (int i = 1; i < height / 2; i++) {
					for (int j = offset; j < offset + h.length; j++)
						if (j < wavelet[i].length)
							wavelet[i][j] = h[j - offset];
					offset += 2;
				}
				// add h[0] to h[2] in the last row
				wavelet[height / 2 - 1][height - 2] += h[0];
				// set g in wavelet
				offset = 0;
				for (int i = height / 2; i < height; i++) {
					for (int j = offset; j < offset + g.length; j++)
						if (j < wavelet[i].length)
							wavelet[i][j] = g[j - offset];
					offset += 2;
				}
				// add g[1] to g[1] in the last row
				wavelet[height - 1][height - 2] += g[0];
				// apply the wavelet transformation to columns
				for (int x = 0; x < width; x++) {
					int[] col = new int[height];
					for (int y = 0; y < height; y++) {
						int[] rgb = new int[3];
						for (int i = 0; i < height; i++) {
							Color color = new Color(rgbArray[x + i
									* image.getWidth()]);
							rgb[0] += (int) (wavelet[y][i] * color.getRed());
							rgb[1] += (int) (wavelet[y][i] * color.getGreen());
							rgb[2] += (int) (wavelet[y][i] * color.getBlue());
						}
						for (int i = 0; i < rgb.length; i++)
							if (rgb[i] < 0)
								rgb[i] = 0;
							else if (rgb[i] > 255)
								rgb[i] = 255;
						col[y] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
					}
					// replace col in rgbArray
					for (int y = 0; y < height; y++)
						rgbArray[x + y * image.getWidth()] = col[y];
				}
				// apply wavelet transform to rows
				for (int y = 0; y < height; y++) {
					int[] row = new int[width];
					for (int x = 0; x < width; x++) {
						int[] rgb = new int[3];
						for (int i = 0; i < width; i++) {
							Color color = new Color(rgbArray[i + y
									* image.getWidth()]);
							rgb[0] += (int) (wavelet[x][i] * color.getRed());
							rgb[1] += (int) (wavelet[x][i] * color.getGreen());
							rgb[2] += (int) (wavelet[x][i] * color.getBlue());
						}
						for (int i = 0; i < rgb.length; i++)
							if (rgb[i] < 0)
								rgb[i] = 0;
							else if (rgb[i] > 255)
								rgb[i] = 255;
						row[x] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
					}
					// replace row in rgbArray
					for (int x = 0; x < width; x++)
						rgbArray[x + y * image.getWidth()] = row[x];
				}
				width /= 2;
				height /= 2;
			}
			BufferedImage bi = new BufferedImage(image.getWidth(),
					image.getHeight(), BufferedImage.TYPE_INT_RGB);
			bi.setRGB(0, 0, bi.getWidth(), bi.getHeight(), rgbArray, 0,
					bi.getWidth());
			this.getImageDst().setIcon(new ImageIcon(bi));
		} else if (opName.equals("Gray scale")) {
			this.setBorderTitle(this.getPanelDst(), "Operation - Color to B/W");
			op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),
					null);
			this.getImageDst()
					.setIcon(
							new ImageIcon(op.filter((BufferedImage) this
									.getImage(getImageDst()), null)));
			this.getmTransform().setEnabled(true);
		}
		pack();
	}

	public BufferedImage readImage(File file) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bi;
	}

	public BufferedImage jpegCodify(BufferedImage bi) {
		// create ImageEncoder object
		ImageEncoder imageEncoder = new ImageEncoder(bi);
		// get array of image encoded component blocks
		int[][] blocks = imageEncoder.encode();
		// get prefix codes used to further encode blocks
		Map<Object, String> prefixCodes = imageEncoder.getHuffmanCodes(blocks);
		// encode blocks as a string using prefixCodes
		String data = imageEncoder.encode(blocks, prefixCodes);
		int width = bi.getWidth();
		int height = bi.getHeight();
		// create ImageDecoder object to reconstruct the original image
		ImageDecoder imageDecoder = new ImageDecoder(width, height, data,
				prefixCodes);
		// get reconstructed array of RGB values
		int[] rgbArray = imageDecoder.decode();
		// create a new BufferedImage using RGB values from rgbArray
		bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bi.setRGB(0, 0, width, height, rgbArray, 0, width);
		return bi;
	}

	public void setBorderTitle(JPanel panel, String title) {
		((TitledBorder) panel.getBorder()).setTitle(title);
	}

	public Image getImage(JLabel label) {
		return ((ImageIcon) label.getIcon()).getImage();
	}

	public static void main(String[] args) {
		JFrame frame = new ImageProcessing();
		frame.setTitle("Image Processing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Container getCp() {
		return cp;
	}

	public void setCp(Container cp) {
		this.cp = cp;
	}

	public JMenuItem getMiOpen() {
		return miOpen;
	}

	public void setMiOpen(JMenuItem miOpen) {
		this.miOpen = miOpen;
	}

	public JMenuItem getMiSave() {
		return miSave;
	}

	public void setMiSave(JMenuItem miSave) {
		this.miSave = miSave;
	}

	public JMenuItem getMiExit() {
		return miExit;
	}

	public void setMiExit(JMenuItem miExit) {
		this.miExit = miExit;
	}

	public JMenuItem getMiCopy() {
		return miCopy;
	}

	public void setMiCopy(JMenuItem miCopy) {
		this.miCopy = miCopy;
	}

	public JMenuItem getMiJpegCodify() {
		return miJpegCodify;
	}

	public void setMiJpegCodify(JMenuItem miJpegCodify) {
		this.miJpegCodify = miJpegCodify;
	}

	public JMenuItem getMiGrayscale() {
		return miGrayscale;
	}

	public void setMiGrayscale(JMenuItem miGrayscale) {
		this.miGrayscale = miGrayscale;
	}

	public JPanel getPanelSrc() {
		return panelSrc;
	}

	public void setPanelSrc(JPanel panelSrc) {
		this.panelSrc = panelSrc;
	}

	public JPanel getPanelDst() {
		return panelDst;
	}

	public void setPanelDst(JPanel panelDst) {
		this.panelDst = panelDst;
	}

	public JFileChooser getFc() {
		return fc;
	}

	public void setFc(JFileChooser fc) {
		this.fc = fc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JLabel getImageSrc() {
		return imageSrc;
	}

	public void setImageSrc(JLabel imageSrc) {
		this.imageSrc = imageSrc;
	}

	public JLabel getImageDst() {
		return imageDst;
	}

	public void setImageDst(JLabel imageDst) {
		this.imageDst = imageDst;
	}

	public JLabel getImageSrcLbl() {
		return imageSrcLbl;
	}

	public void setImageSrcDesc(JLabel imageSrcLbl) {
		this.imageSrcLbl = imageSrcLbl;
	}

	public JLabel getImageDstLbl() {
		return imageDstLbl;
	}

	public void setImageDstLbl(JLabel imageDstLbl) {
		this.imageDstLbl = imageDstLbl;
	}

	public JMenuItem getMiHaar() {
		return miHaar;
	}

	public void setMiHaar(JMenuItem miHaar) {
		this.miHaar = miHaar;
	}

	public JMenu getmTransform() {
		return mTransform;
	}

	public void setmTransform(JMenu mTransform) {
		this.mTransform = mTransform;
	}

	public JMenuItem getMiCDF97() {
		return miCDF97;
	}

	public void setMiCDF97(JMenuItem miCDF97) {
		this.miCDF97 = miCDF97;
	}

	public JMenuItem getMiLeGall() {
		return miLeGall;
	}

	public void setMiLeGall(JMenuItem miLeGall) {
		this.miLeGall = miLeGall;
	}

}