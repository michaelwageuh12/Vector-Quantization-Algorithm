import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class Compress {

	public int codebook_hight;
	public int codebook_width;
	public int width = 512;
	public int height = 512;
	public int num_vectors;
	public Vector<CodeBook> allCodeBooks = new Vector<CodeBook>();
	public Vector<CodeBook> finalResult = new Vector<CodeBook>();
	int pixels[][];

	public Compress(int h, int w, int n, String filePath) {
		codebook_hight = h;
		codebook_width = w;
		num_vectors = n;
		readImage(filePath);
		makecodebook();
		finalResult.add(get_avrage());
	}

	public void readImage(String filePath) {
		int h = 0;
		int w = 0;
		File file = new File(filePath);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
		}
		w = image.getWidth();
		h = image.getHeight();
		if (h % codebook_hight != 0) {
			height = h + (codebook_hight - (w % codebook_hight));
		}
		if (w % codebook_width != 0) {
			width = w + (codebook_width - (w % codebook_width));
		}
		if (w % codebook_width == 0 && h % codebook_hight == 0) {
			width = w;
			height = h;
		}
		pixels = new int[height + 1][width + 1];
		for (int t = 0; t < height + 1; t++) {
			for (int i = 0; i < width + 1; i++) {
				pixels[t][i] = 0;
			}
		}
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int rgb = image.getRGB(x, y);
				int alpha = (rgb >> 24) & 0xff;
				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = (rgb >> 0) & 0xff;
				pixels[y][x] = r;
			}
		}
	}

	public void makecodebook() {
		for (int i = 0; i < height; i += codebook_hight) {
			for (int j = 0; j < width; j += codebook_width) {
				CodeBook CB = new CodeBook(codebook_hight, codebook_width);
				for (int a = 0; a < codebook_hight; a++) {
					for (int b = 0; b < codebook_width; b++) {
						CB.arr[a][b] = pixels[i + a][j + b];
					}
				}
				allCodeBooks.add(CB);
			}
		}
	}

	public CodeBook get_avrage() {
		CodeBook res = new CodeBook(codebook_hight, codebook_width);
		for (int i = 0; i < allCodeBooks.size(); i++) {
			for (int j = 0; j < codebook_hight; j++) {
				for (int a = 0; a < codebook_width; a++) {
					res.arr[j][a] += allCodeBooks.get(i).arr[j][a];
				}
			}
		}
		int a = allCodeBooks.size();
		for (int t = 0; t < codebook_hight; t++) {
			for (int y = 0; y < codebook_width; y++) {
				res.arr[t][y] /= a;
			}
		}
		return res;
	}

	public Vector<CodeBook> nearestVectors(Vector<CodeBook> v) {
		Vector<CodeBook> vec = new Vector<>();
		for (int i = 0; i < v.size(); i++) {
			CodeBook CB = new CodeBook(codebook_hight, codebook_width);
			vec.add(CB);
		}
		for (int t = 0; t < allCodeBooks.size(); t++) {
			double min = Integer.MAX_VALUE, res = 0;
			int index = 0;
			for (int up = 0; up < v.size(); up++) {
				res = 0;
				for (int a = 0; a < codebook_hight; a++) {
					for (int b = 0; b < codebook_width; b++) {
						res += Math.abs(allCodeBooks.get(t).arr[a][b] - v.get(up).arr[a][b]);
					}
				}
				res /= (codebook_width * codebook_hight);
				if (res < min) {
					min = res;
					index = up;
				}
			}
			vec.get(index).count++;
			for (int a = 0; a < codebook_hight; a++) {
				for (int b = 0; b < codebook_width; b++) {
					vec.get(index).arr[a][b] += allCodeBooks.get(t).arr[a][b];
				}
			}
		}
		for (int x = 0; x < vec.size(); x++) {
			for (int a = 0; a < codebook_hight; a++) {
				for (int b = 0; b < codebook_width; b++) {
					vec.get(x).arr[a][b] /= vec.get(x).count;
				}
			}
			vec.get(x).count = 0;
		}
		return vec;
	}

	public void compress() {
		if (finalResult.size() >= num_vectors) {
			return;
		}
		Vector<CodeBook> assistant = new Vector<>();
		for (int t = 0; t < finalResult.size(); t++) {
			CodeBook l = new CodeBook(codebook_hight, codebook_width);
			CodeBook h = new CodeBook(codebook_hight, codebook_width);
			for (int i = 0; i < codebook_hight; i++) {
				for (int j = 0; j < codebook_width; j++) {
					h.arr[i][j] = finalResult.get(t).arr[i][j] + 1;
					l.arr[i][j] = finalResult.get(t).arr[i][j] - 1;
				}
			}
			assistant.add(l);
			assistant.add(h);
		}
		finalResult.clear();
		finalResult = nearestVectors(assistant);
		compress();
	}

	public void Final() {
		while (true) {
			Vector<CodeBook> assistant = new Vector<>();
			assistant = finalResult;
			assistant = nearestVectors(assistant);
			boolean equal = true;
			for (int t = 0; t < assistant.size(); t++) {
				if (!Arrays.deepEquals(assistant.get(t).arr, finalResult.get(t).arr)) {
					finalResult.set(t, assistant.get(t));
					equal = false;
				}
			}
			if (equal) {
				break;
			} else
				finalResult = assistant;
		}
	}

	public void files() throws IOException {

		BufferedWriter out = null;
		BufferedWriter out2 = null;
		try {
			out = new BufferedWriter(new FileWriter("output.txt"));
			out2 = new BufferedWriter(new FileWriter("pixels.txt"));

		} catch (IOException e) {
		}
		out.write(finalResult.size() + " " + codebook_hight + " " + codebook_width + " " + " " + height + " " + width);
		out.newLine();
		for (int i = 0; i < finalResult.size(); i++) {
			for (int x = 0; x < codebook_hight; x++) {
				for (int y = 0; y < codebook_width; y++) {
					out.write(finalResult.get(i).arr[x][y] + " ");
				}
				out.newLine();
			}
		}
		for (int t = 0; t < allCodeBooks.size(); t++) {
			double min = Integer.MAX_VALUE, res = 0;
			int index = 0;
			for (int tt = 0; tt < finalResult.size(); tt++) {
				res = 0;
				for (int a = 0; a < codebook_hight; a++) {
					for (int b = 0; b < codebook_width; b++) {
						res += Math.abs(allCodeBooks.get(t).arr[a][b] - finalResult.get(tt).arr[a][b]);
					}
				}
				if (res < min) {
					min = res;
					index = tt;
				}
			}
			out2.write(index + " ");
		}
		out.close();
		out2.close();
	}
}