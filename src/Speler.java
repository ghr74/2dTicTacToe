public class Speler {
	private int ID;
	private int mode;
	private Bot bot;
	public static final int HUMAN = 0;
	public static final int BOTEASY = 1;
	public static final int BOTMEDIUM = 2;
	public static final int BOTHARD = 3;
	public static final int NONE = 0;
	public static final int X = 1;
	public static final int O = 2;
	public static final int BOTH = 3;

	public static int getOpponent(int pID) {
		if (pID == X) {
			return O;
		} else {
			return X;
		}
	}

	public Speler(int ID) {
		this.ID = ID;
	}

	public void setMode(int mode) {
		this.mode = mode;
		switch (mode) {
		case HUMAN: {
			this.bot = null;
			break;
		}
		case BOTEASY: {
			this.bot = new Bot(Bot.EASY, this.ID);
			break;
		}
		case BOTMEDIUM: {
			this.bot = new Bot(Bot.MEDIUM, this.ID);
			break;
		}
		case BOTHARD: {
			this.bot = new Bot(Bot.HARD, this.ID);
		}
		}
	}

	public int getID() {
		return ID;
	}

	public int getMode() {
		return mode;
	}

	public int[] getMove(Field field) {
		return this.bot.getMove(field);
	}
}
