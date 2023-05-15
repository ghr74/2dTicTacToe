public class Field {
	private int[] size = new int[2];
	private Place[][] field;
	private int winX;
	private int winY;

	public Field(int lines, int rows) {
		size[0] = rows;
		size[1] = lines;
		this.winX = Game.winsizeX;
		this.winY = Game.winsizeY;
		initField();
	}

	private void initField() {
		field = new Place[size[0]][size[1]];
		int uSpace = ((winX / ((size[1] * 5) + 1)) + (winY / ((size[0] * 5) + 1))) / 2;
		for (int row = 1; row < (size[0] + 1); row++) {
			for (int wid = 1; wid < (size[1] + 1); wid++) {
				field[row - 1][wid - 1] = new Place((uSpace * 3) * wid,
						(uSpace * 3) * row, uSpace);
			}
		}
	}

	public Place getPlaceAt(int pX, int pY) {
		int x = pX;
		int y = winY - pY;
		for (int row = 1; row < (size[0] + 1); row++) {
			for (int wid = 1; wid < (size[1] + 1); wid++) {
				if (field[row - 1][wid - 1].isInPlace(x, y)) {
					return field[row - 1][wid - 1];
				}
			}
		}

		return null;
	}

	public Place getPlaceAtEx(int pX, int pY) {
		int x = pX;
		int y = pY;
		for (int row = 1; row < (size[0] + 1); row++) {
			for (int wid = 1; wid < (size[1] + 1); wid++) {
				if (field[row - 1][wid - 1].isInPlace(x, y)) {
					return field[row - 1][wid - 1];
				}
			}
		}

		return null;
	}

	public Place[][] getPlaces() {
		return field;
	}

	public void clear() {
		field = null;
		initField();
	}

	private int[] getArrayPos(Place p) {
		int[] tmpArray = new int[2];
		for (int row = 0; row < (size[0]); row++) {
			for (int wid = 0; wid < (size[1]); wid++) {
				if (field[row][wid] == p) {
					tmpArray[0] = row;
					tmpArray[1] = wid;
					return tmpArray;
				}
			}
		}
		return tmpArray;
	}

	public boolean checkWin(Place p) {
		int[] tmpP = new int[2];
		tmpP = getArrayPos(p);
		int vec0;
		int vec1;
		for (int row = tmpP[0] - 1; row < (tmpP[0] + 2); row++) {
			for (int wid = tmpP[1] - 1; wid < (tmpP[1] + 2); wid++) {
				if ((row >= 0) && (row < (size[0])) && (wid >= 0)
						&& (wid < (size[1]))) {
					Place tP = field[row][wid];
					if ((tP != null) && (tP != p)
							&& (tP.getState() == p.getState())) {
						int[] tmpA = new int[2];
						tmpA = getArrayPos(tP);
						vec0 = tmpA[0] - tmpP[0];
						vec1 = tmpA[1] - tmpP[1];
						int p0 = tmpA[0] + vec0;
						int p1 = tmpA[1] + vec1;
						int r0 = tmpA[0] - (2 * vec0);
						int r1 = tmpA[1] - (2 * vec1);
						if ((isInsideArrayNotNull(p0, p1) && ((field[p0][p1] != p) && (field[p0][p1]
								.getState() == p.getState())))
								|| (isInsideArrayNotNull(r0, r1)
										&& (field[r0][r1] != p) && (field[r0][r1]
										.getState() == p.getState()))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isInsideArrayNotNull(int par1, int par2) {
		return (par1 >= 0) && (par2 >= 0) && (par1 < (size[0]))
				&& (par2 < (size[1])) && (field[par1][par2] != null);
	}

	public boolean isFull() {
		for (int row = 0; row < (size[0]); row++) {
			for (int wid = 0; wid < (size[1]); wid++) {
				if (field[row][wid].getState() == Place.EMPTY) {
					return false;
				}
			}
		}
		return true;
	}
}
