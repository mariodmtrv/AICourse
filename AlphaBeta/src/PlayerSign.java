public enum PlayerSign implements Comparable<PlayerSign> {
	PLAYER(1, "Player"), AGENT(-1, "Agent"), NEITHER(0, "Neither");
	int value;
	String name;

	PlayerSign(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public String toString() {
		switch (value) {
		case 1:
			return "X";
		case -1:
			return "O";
		default:
			return "_";
		}
	}

	public String getName() {
		return this.name;
	}
}
