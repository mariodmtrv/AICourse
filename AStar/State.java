import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

class State implements Comparable {
	private static final int DESKSIZE = 10;

	private int desk[];
	private int path;
	private int heuristic;
	private int zeroPos;

	public State(int desk[], int path, int zeroPos) {
		this.desk = desk;
		this.path = path;
		this.zeroPos = zeroPos;
		this.heuristic = StateDefiner.calculateHeuristic(desk);
		for (int i = 0; i < DESKSIZE; i++) {
			this.desk[i] = desk[i];
		}

	}

	public int getPath() {
		return path;
	}

	public void setPath(int path) {
		this.path = path;
	}

	public int getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}

	@Override
	public int hashCode() {
		int code = 0;
		for (int i = 1; i <= 9; i++) {
			code *= 10;
			code += desk[i];
		}
		return code;
	}

	List<State> getNeighbours(Set<State> traversed) {
		List<State> neighbours = new ArrayList<>();
		// now try all repositions of the zero
		for (int neighPos : StateDefiner.neighbours[zeroPos]) {
			// copies the state
			int[] deskNext = new int[10];
			System.arraycopy(desk, 0, deskNext, 0, desk.length);
			// moves the 0 in the new desk
			deskNext[zeroPos] = desk[neighPos];
			deskNext[neighPos] = 0;

			State neighbourState = new State(deskNext, path + 1, neighPos);
			if (!(traversed.contains(neighbourState))) {
				// adds it to the candidates
				neighbours.add(neighbourState);

				traversed.add(neighbourState);
			}
		}
		return neighbours;
	}

	public boolean equals(Object other) {
		return (this.heuristic+this.path) == (((State) other).heuristic+((State) other).path);
	}

	@Override
	public int compareTo(Object other) {
		return (this.heuristic +this.path)- (((State) other).heuristic+((State) other).path);
	}
}