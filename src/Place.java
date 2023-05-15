public class Place {
	public static final int EMPTY = 0;
	public static final int X = 1;
	public static final int O = 2;
	private int state = EMPTY;
	private int x;
	private int y;
	private int rad;

	public Place(int x, int y, int rad) {
		this.x = x;
		this.y = y;
		this.rad = rad;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getRad() {
		return rad;
	}

	public void setState(int state) {
		if (this.state == EMPTY) {
			this.state = state;
		}
	}

	public void setStateEx(int state) {
		this.state = state;
	}

	public boolean canSetState() {
		return (this.state == EMPTY);
	}

	public int getState() {
		return state;
	}

	public boolean isInPlace(int pX, int pY) {
		return ((pX > (x - rad)) && (pX < (x + rad)) && (pY > (y - rad)) && (pY < (y + rad)));
	}
}
