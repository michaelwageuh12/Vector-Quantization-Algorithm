public class CodeBook {
	int width, height;
	int[][] arr;
	double sum, count;

	CodeBook() {
		width = height = 0;
	}

	CodeBook(int width, int height) {
		this.width = width;
		this.height = height;
		this.count = 0;
		this.sum = 0;
		arr = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				arr[i][j] = 0;
			}
		}
	}
}
