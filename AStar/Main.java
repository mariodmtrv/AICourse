public class Main {

	public static void main(String[] args) {
		ProblemSolver solver = new ProblemSolver();
		solver.createInitialState();
		int shortestPath = solver.getShortestPath();
		System.out.println("Minumum moves required: " + shortestPath);
	}

}
