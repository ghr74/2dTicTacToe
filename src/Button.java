public class Button {
	private int x;
	private int y;
	private int height;
	private int width;

	public void setCoords(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean isInPlace(int pX, int pY) {
		// Math.abs(pY -= Game.winsizeY);
		int hHeight = height / 2;
		int hWidth = width / 2;
		return ((pX > (x - hWidth)) && (pX < (x + hWidth))
				&& (pY > (y - hHeight)) && (pY < (y + hHeight)));
	}
}
