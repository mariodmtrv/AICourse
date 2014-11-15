import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class ProblemSolver {
	private PriorityQueue<State> candidateStates = new PriorityQueue<>();
	private Set<State> traversed = new HashSet<>();

	public void createInitialState() {

		int desk[] = new int[10];
		int zeroPos = 0;

		Scanner scanner = new Scanner(System.in);
		for (int i = 1; i <= 9; i++) {
			int num = scanner.nextInt();
			desk[i] = num;
			if (num == 0) {
				zeroPos = i;
			}
		}
		State initialState = new State(desk, 0, zeroPos);
		candidateStates.add(initialState);
		traversed.add(initialState);
	}

	public Integer getShortestPath() {

		while (!candidateStates.isEmpty()) {
			State current = candidateStates.poll();
			// terminal state reached
			if (current.getHeuristic() == 0) {
				return current.getPath();
			}
			candidateStates.addAll(current.getNeighbours(traversed));
		}
		// / all 9! failed?
		return (int) 10e6;

	}

}
