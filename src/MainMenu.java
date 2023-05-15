public class MainMenu {

	private Checkbox[][] selectBox = new Checkbox[2][4];
	private Button button;
	private final int SPELER = 0;
	private final int ROW = 1;

	public MainMenu() {
		for (int speler = 0; speler < 2; speler++) {
			for (int box = 0; box < 4; box++) {
				if (box == 0) {
					selectBox[speler][box] = new Checkbox(true);
				} else {
					selectBox[speler][box] = new Checkbox(false);
				}
			}
		}
		button = new Button();
	}

	public int getPosChecked(int speler) {
		int res = 0xFF;
		for (int pos = 0; pos < 4; pos++) {
			if (selectBox[speler][pos].getState() == Checkbox.CHECKED) {
				res = pos;
			}
		}
		return res;
	}

	public Checkbox getBoxAt(int pX, int pY) {
		int x = pX;
		int y = Game.winsizeY - pY;
		for (int speler = 0; speler < 2; speler++) {
			for (int row = 0; row < 4; row++) {
				if (selectBox[speler][row].isInPlace(x, y)) {
					return selectBox[speler][row];
				}
			}
		}

		return null;
	}

	public Checkbox[][] getBoxes() {
		return selectBox;
	}

	public boolean isButtonAt(int pX, int pY) {
		int x = pX;
		int y = Game.winsizeY - pY;
		return button.isInPlace(x, y);
	}

	public boolean switchStateAt(int x, int y) {
		Checkbox tmpBox = getBoxAt(x, y);
		if (tmpBox != null) {
			int[] tmpP = new int[2];
			tmpP[SPELER] = getArrayPos(tmpBox)[SPELER];
			tmpP[ROW] = getArrayPos(tmpBox)[ROW];
			for (int i = 0; i < 4; i++) {
				if (selectBox[tmpP[SPELER]][i].getState() == Checkbox.CHECKED) {
					selectBox[tmpP[SPELER]][i].switchState();
				}
			}
			tmpBox.switchState();
			return true;
		} else {
			return false;
		}
	}

	/* @require c!=null */
	private int[] getArrayPos(Checkbox c) {
		int[] tmpArray = new int[2];
		for (int speler = 0; speler < 2; speler++) {
			for (int row = 0; row < 4; row++) {
				if (selectBox[speler][row] == c) {
					tmpArray[SPELER] = speler;
					tmpArray[ROW] = row;
					return tmpArray;
				}
			}
		}
		return tmpArray;
	}

	public Button getButton() {
		return button;
	}
}
