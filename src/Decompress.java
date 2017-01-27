import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class Decompress {

	int codebook_hight, numVectors, height, width, codebook_width;
	Vector<CodeBook> CodeBooks = new Vector<>();
	Vector<CodeBook> vec = new Vector<>();
	int[][] pixels;

	public void writeImage(String outputFilePath, int height, int width) {
		File fileout = new File(outputFilePath);
		BufferedImage image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image2.setRGB(x, y, (pixels[y][x] << 16) | (pixels[y][x] << 8) | (pixels[y][x]));
			}
		}
		try {
			ImageIO.write(image2, "jpg", fileout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void decompress(String path) throws IOException {
		BufferedReader in;
		BufferedReader in2;
		try {
			in = new BufferedReader(new FileReader("output.txt"));
			in2 = new BufferedReader(new FileReader("pixels.txt"));
			String s = in.readLine();
			String[] arr = s.split(" ");
			numVectors = Integer.parseInt(arr[0]);
			codebook_hight = Integer.parseInt(arr[1]);
			codebook_width = Integer.parseInt(arr[2]);
			height = Integer.parseInt(arr[4]);
			width = Integer.parseInt(arr[5]);

			for (int i = 0; i < numVectors; i++) {
				CodeBook CB = new CodeBook(codebook_hight, codebook_width);
				for (int j = 0; j < codebook_hight; j++) {
					s = in.readLine();
					arr = s.split(" ");
					for (int k = 0; k < arr.length; k++) {
						CB.arr[j][k] = Integer.parseInt(arr[k]);
					}
				}
				CodeBooks.add(CB);
			}
			pixels = new int[height][width];
			while ((s = in2.readLine()) != null) {
				String values[] = s.split(" ");
				for (int t = 0; t < values.length; t++) {
					int a = Integer.parseInt(values[t]);
					CodeBook cb = new CodeBook(codebook_hight, codebook_width);
					for (int i = 0; i < codebook_hight; i++) {
						for (int j = 0; j < codebook_width; j++) {
							cb.arr[i][j] = CodeBooks.get(a).arr[i][j];
						}
					}
					vec.add(cb);
				}
			}
			int it = 0;
			for (int x = 0; x < 512; x += codebook_hight) {
				for (int y = 0; y < 512; y += codebook_width) {
					for (int a = 0; a < codebook_hight; a++) {
						for (int b = 0; b < codebook_width; b++) {
							pixels[x + a][y + b] = vec.get(it).arr[a][b];
						}
					}
					it++;
				}
			}
			writeImage("compressed.jpg", 512, 512);
		} catch (IOException e) {
		}
	}
}
