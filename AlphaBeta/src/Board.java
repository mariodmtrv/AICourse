import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
	PlayerSign[] board;
	private static final int BOARD_SIZE = 9;

	public Board() {
		this.board = new PlayerSign[BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			board[i] = PlayerSign.NEITHER;
		}
	}

	public void performMove(int position, PlayerSign player) {
		this.board[position] = player;
	}

	public Board(PlayerSign[] board) {
		this.board = new PlayerSign[BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			this.board[i] = board[i];
		}
	}

	public Board(Board other) {
		this(other.board);
	}

	PlayerSign getWinner() {
		PlayerSign result;
		for (int i = 0; i < 3; i++) {
			// horizontal
			result = areEqual(board[i * 3], board[i * 3 + 1], board[i * 3 + 2]);

			if (result != PlayerSign.NEITHER) {
				return result;
			}
			// vertical
			result = areEqual(board[i], board[i + 3], board[i + 6]);
			if (result != PlayerSign.NEITHER) {
				return result;
			}
		}
		//

		result = areEqual(board[2], board[4], board[6]);
		if (result != PlayerSign.NEITHER) {
			return result;
		}
		result = areEqual(board[0], board[4], board[8]);
		return result;
	}

	PlayerSign areEqual(PlayerSign a, PlayerSign b, PlayerSign c) {
		if (a.equals(b) && b.equals(c)) {
			return a;
		}
		return PlayerSign.NEITHER;
	}

	List<Integer> getAvailable() {
		List<Integer> available = new ArrayList<>();
		for (int boardIndex = 0; boardIndex < BOARD_SIZE; boardIndex++) {
			if (board[boardIndex] == PlayerSign.NEITHER) {
				available.add(boardIndex);
			}
		}
		return available;
	}

	public String toString() {
		StringBuilder boardStr = new StringBuilder();
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (i % 3 == 0) {
				boardStr.append("\n");
			}
			boardStr.append(" " + board[i] + " ");
		}
		return boardStr.toString();
	}
}
