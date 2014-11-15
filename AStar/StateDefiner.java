public class StateDefiner {
	static int[][] distances = new int[10][10];
	static int desired[] = { 9, 1, 2, 3, 4, 5, 6, 7, 8 };
	static int neighbours[][] = { { 0 }, { 2, 4, 5 }, { 1, 5, 3 }, { 2, 5, 6 },
			{ 1, 5, 7 }, { 2, 4, 6, 8 }, { 3, 5, 9 }, { 4, 8 }, { 5, 7, 9 },
			{ 8, 6 } };
	static {

		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j <= 9; j++) {
				int x1 = i / 3 + ((i % 3 > 0) ? 1 : 0);
				int y1 = (i % 3 + (i % 3 == 0 ? 1 : 0) * 3);
				int x2 = j / 3 + ((j % 3 > 0) ? 1 : 0);
				int y2 = (j % 3 + (j % 3 == 0 ? 1 : 0) * 3);

				distances[i][j] = Math.abs(x2 - x1) + Math.abs(y2 - y1);
			}
		}
	}

	public static int calculateHeuristic(int desk[]) {
		int heuristic = 0;
		for (int i = 1; i <= 9; i++) {
			heuristic += distances[i][desired[desk[i]]];
		}
		return heuristic;

	}
}
