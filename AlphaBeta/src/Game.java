import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
	private Board board;

	public Game() {
		this.board = new Board();
	}

	public Game(PlayerSign[] boardSigns) {
		this.board = new Board(boardSigns);
	}

	private void playerMove() {
		System.out.println(board);
		System.out.println("Make your move: (1-9)");
		Scanner s = new Scanner(System.in);
		int pos = s.nextInt();
		if (board.board[pos - 1] == PlayerSign.NEITHER) {
			board.board[pos - 1] = PlayerSign.PLAYER;
		} else {
			System.out.println("Cell is occupied");
			playerMove();
		}
	}

	public void agentMove() {
		List<Integer> possibleMoves = board.getAvailable();
		int bestMove = possibleMoves.get(0);
		// tries possible moves performs the best move
		for (Integer possibleMove : possibleMoves) {
			Board testNode = new Board(board);
			testNode.performMove(possibleMove, PlayerSign.AGENT);
			int value = alphaBetaPruning(PlayerSign.PLAYER, -1, 1, testNode);
			if (value == 1) {
				bestMove = possibleMove;
				break;
			} else if (value == 0) {
				bestMove = possibleMove;
			}
		}
		board.performMove(bestMove, PlayerSign.AGENT);
	}

	private int alphaBetaPruning(PlayerSign maximizingPlayer, int alpha,
			int beta, Board node) {

		PlayerSign winner = node.getWinner();
		if (winner != PlayerSign.NEITHER) {
			if (winner == PlayerSign.AGENT) {
				return 1;
			}
			return -1;
		}
		// gameend make the move
		if (node.getAvailable().size() == 0) {
			return 0;
		}

		if (maximizingPlayer == PlayerSign.AGENT) {
			for (Integer available : node.getAvailable()) {
				Board child = new Board(node);
				child.performMove(available, maximizingPlayer);
				alpha = Math
						.max(alpha,
								alphaBetaPruning(PlayerSign.PLAYER, alpha,
										beta, child));
				if (beta <= alpha) {
					return alpha;
				}
			}

		} else {
			for (Integer available : node.getAvailable()) {
				Board child = new Board(node);
				child.performMove(available, maximizingPlayer);
				beta = Math.min(beta,
						alphaBetaPruning(PlayerSign.AGENT, alpha, beta, child));
				if (beta <= alpha) {
					return beta;
				}
			}

		}
		return 0;
	}

	private boolean winnerFound() {
		PlayerSign winner = board.getWinner();
		if (winner != PlayerSign.NEITHER) {
			System.out.println(winner.getName() + " wins!");
			return true;
		}
		if (board.getAvailable().size() == 0) {
			System.out.println("It's a tie.");
			return true;
		}
		return false;
	}

	public void play() {
		while (true) {
			playerMove();
			if (winnerFound()) {
				break;
			}
			agentMove();
			if (winnerFound()) {
				break;
			}
		}
	}
}
