public class Checkbox {
	private boolean state;
	public static final boolean CHECKED = true;
	public static final boolean UNCHECKED = false;
	private int x;
	private int y;
	private int rad;

	public Checkbox(boolean state) {
		this.state = state;
	}

	public void setCoords(int x, int y, int rad) {
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

	public boolean getState() {
		return state;
	}

	public void switchState() {
		state = !state;
	}

	public boolean isInPlace(int pX, int pY) {
		// Math.abs(pY -= Game.winsizeY);
		return ((pX > (x - rad)) && (pX < (x + rad)) && (pY > (y - rad)) && (pY < (y + rad)));
	}
}