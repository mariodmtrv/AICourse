public class Simulator {

	public static void main(String[] args) {
		PlayerSign[] signs = new PlayerSign[] { PlayerSign.AGENT,
				PlayerSign.PLAYER, PlayerSign.AGENT, PlayerSign.PLAYER,
				PlayerSign.AGENT, PlayerSign.PLAYER, PlayerSign.NEITHER,
				PlayerSign.NEITHER, PlayerSign.NEITHER };
		
		Game game = new Game();
		game.play();
	}

}
