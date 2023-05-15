public class Bot {
	private int mode;
	private int sign;
	private Field testField;
	public static final int EASY = 1;
	public static final int MEDIUM = 2;
	public static final int HARD = 3;

	public Bot(int mode, int sign) {
		this.mode = mode;
		this.sign = sign;
	}

	private int[] testWin(int spelerID) {
		Field tmpfield = testField;
		int[] res = new int[2];
		res[0] = 0x1337;
		res[1] = 0x1337;
		Place[][] tmpplace = tmpfield.getPlaces().clone();
		for (Place[] places : tmpplace) {
			for (Place place : places) {
				if (place.canSetState()) {
					place.setState(spelerID);
					if (tmpfield.checkWin(place)) {
						res[0] = place.getX();
						res[1] = place.getY();
					}
					place.setStateEx(Place.EMPTY);
				}
			}
		}
		return res;
	}

	public int[] getMove(Field field) {
		int[] resarray;
		this.testField = field;
		switch (mode) {
		case EASY: {
			resarray = getEasyBotMove();
			break;
		}
		case MEDIUM: {
			resarray = getMediumBotMove();
			break;
		}
		case HARD: {
			resarray = getHardBotMove();
			break;
		}
		default: {
			resarray = getEasyBotMove();
		}
		}
		return resarray;
	}

	public int[] getEasyBotMove() {
		return toevalligeVrijePlace();
	}

	public int[] getMediumBotMove() {
		int[] res = new int[2];
		Place[][] places = testField.getPlaces();
		if (places[1][1].canSetState()) {
			System.out.println("RULE: set middle place Pri1");
			res[0] = places[1][1].getX();
			res[1] = places[1][1].getY();
		} else if (testWin(sign)[0] != 0x1337) {
			System.out.println("RULE: set winning place for me Pri2");
			res = testWin(sign);
		} else if (testWin(Speler.getOpponent(sign))[0] != 0x1337) {
			res = testWin(Speler.getOpponent(sign));
			System.out.println("RULE: set nonwinning place for op Pri3");
		} else {
			res = toevalligeVrijePlace();
			System.out.println("RULE: choose random place Pri4");
		}
		return res;
	}

	public int[] getHardBotMove() {
		int[] res = new int[2];
		return res;
	}

	private int[] toevalligeVrijePlace() {
		int[] res = new int[2];
		Place[] vrijePlaces = new Place[9];
		for (Place[] plaats : testField.getPlaces()) {
			for (Place pos : plaats) {
				if (pos.getState() == Place.EMPTY) {
					boolean haswritten = false;
					for (int i = 0; (haswritten == false)
							&& (i < vrijePlaces.length); i++) {
						if (vrijePlaces[i] == null) {
							vrijePlaces[i] = pos;
							haswritten = true;
						}
					}
				}
			}
		}
		Place resplc = vrijePlaces[0];
		for (Place plc : vrijePlaces) {
			if ((plc != null) && (Math.random() < 0.2)) {
				resplc = plc;
			}
		}
		res[0] = resplc.getX();
		res[1] = resplc.getY();
		return res;
	}
}
