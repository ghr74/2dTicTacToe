import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class Game {
	public static final int winsizeX = 800;
	public static final int winsizeY = 600;
	private int rows = 3;
	private int lines = 3;
	private float bgRed = 0.5f;
	private float bgGreen = 0.5f;
	private float bgBlue = 1.0f;
	private float tkRed = 0.7f;
	private float tkGreen = 0.7f;
	private float tkBlue = 1.0f;
	private UnicodeFont font = new UnicodeFont(new Font("Verdana", Font.PLAIN,
			40));
	private Speler X = new Speler(Place.X);
	private Speler O = new Speler(Place.O);
	private Speler activePlayer;
	private boolean gameRunning;
	private boolean gameActive;
	private Speler startingPlayer = O;
	private int winningPlayerID = Place.EMPTY;
	private MainMenu mainmenu = new MainMenu();

	// private Place place = new Place(posX, posY, width);
	private Field field = new Field(lines, rows);

	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(winsizeX, winsizeY));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		// init OpenGL here

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, winsizeX, winsizeY, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		mainmenu.getButton().setCoords(winsizeX / 2, winsizeY - 80, 200, 50);

		gameRunning = true;
		initiate();

		// display loop

		while (!Display.isCloseRequested() && gameRunning) {

			// render OpenGL here
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			pollInput();
			if (gameActive) {
				for (int row = 0; row < rows; row++) {
					for (int line = 0; line < lines; line++) {
						drawPlace(field.getPlaces()[row][line]);
					}
				}
				drawString(winsizeX - 300, 0, "Actief Speler:");
				if (winningPlayerID == Speler.X) {
					drawEindeMenu("X");
				} else if (winningPlayerID == Speler.O) {
					drawEindeMenu("O");
				} else if (winningPlayerID == Speler.BOTH) {
					drawEindeMenu("U");
				} else {
					drawGameMenu();
				}
				if (activePlayer.getID() == Speler.X) {
					drawCross(winsizeX - 170, 200, 100, 10.0f, tkRed, tkGreen,
							tkBlue);
				} else {
					drawCircle(winsizeX - 170, 200, 100, 10.0f, tkRed, tkGreen,
							tkBlue);
				}
			} else {
				drawBeginMenu();
			}
			// drawPlace(place);
			Display.update();
			Display.sync(15);
		}

		Display.destroy();
	}

	public void initiate() {
		switch (startingPlayer.getID()) {
		case (Speler.O): {
			activePlayer = O;
		}
		case (Speler.X): {
			activePlayer = X;
		}
		default: {
			activePlayer = O;
		}
		}
		X.setMode(0x1337);
		O.setMode(0x1337);
		field.clear();
		winningPlayerID = Place.EMPTY;
	}

	public void pollInput() {
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					int x = Mouse.getX();
					int y = Mouse.getY();
					if (gameActive && (activePlayer.getMode() == Speler.HUMAN)) {
						System.out.println("human click");
						Place tmpPlace = field.getPlaceAt(x, y);
						if ((tmpPlace != null) && tmpPlace.canSetState()) {
							if ((winningPlayerID == Speler.NONE)
									&& (activePlayer.getID() == Speler.X)) {
								tmpPlace.setState(Place.X);
								if (field.checkWin(tmpPlace)) {
									winningPlayerID = Speler.X;
								} else if ((winningPlayerID == Speler.NONE)
										&& field.isFull()) {
									winningPlayerID = Speler.BOTH;
								}
								activePlayer = O;
							} else if (winningPlayerID == Speler.NONE) {
								tmpPlace.setState(Place.O);
								if (field.checkWin(tmpPlace)) {
									winningPlayerID = Speler.O;
								} else if ((winningPlayerID == Speler.NONE)
										&& field.isFull()) {
									winningPlayerID = Speler.BOTH;
								}
								activePlayer = X;
							}
						}
					} else {
						if (!mainmenu.switchStateAt(x, y)) {
							if (mainmenu.isButtonAt(x, y)) {
								X.setMode(mainmenu.getPosChecked(1));
								O.setMode(mainmenu.getPosChecked(0));
								gameActive = true;
							}
						}
					}
				}
			}
		}
		if (gameActive
				&& ((activePlayer.getMode() > Speler.HUMAN) && (winningPlayerID == Speler.NONE))
				&& (field.isFull() == false)) {
			System.out.println("bot action");
			int[] botr = activePlayer.getMove(field);
			System.out.println("botmove: X " + botr[0] / 100 + " ; Y "
					+ botr[1] / 100);
			Place tmpplace = field.getPlaceAtEx(botr[0], botr[1]);
			if ((tmpplace != null) && tmpplace.canSetState()) {
				tmpplace.setState(activePlayer.getID());
				if (field.checkWin(tmpplace)) {
					winningPlayerID = activePlayer.getID();
				} else if ((winningPlayerID == Speler.NONE) && field.isFull()) {
					winningPlayerID = Speler.BOTH;
				}
			}
			if (activePlayer.getID() == Speler.X) {
				activePlayer = O;
			} else {
				activePlayer = X;
			}
		}
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_E) {
					gameRunning = false;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_R) {
					initiate();
					gameActive = false;
				}
			}
		}
	}

	public void drawPlayerSelect(int pX, int pY, int speler) {
		drawString(pX, pY, "Mens");
		drawString(pX, pY + 75, "Easy KI");
		drawString(pX, pY + 150, "Medium KI");
		drawString(pX, pY + 225, "Hard KI");
		if (speler == 1) {
			mainmenu.getBoxes()[speler - 1][0].setCoords(pX + 265, pY + 20, 20);
			mainmenu.getBoxes()[speler - 1][1].setCoords(pX + 265, pY + 95, 20);
			mainmenu.getBoxes()[speler - 1][2]
					.setCoords(pX + 265, pY + 170, 20);
			mainmenu.getBoxes()[speler - 1][3]
					.setCoords(pX + 265, pY + 245, 20);
		} else {
			mainmenu.getBoxes()[speler - 1][0].setCoords(pX - 50, pY + 20, 20);
			mainmenu.getBoxes()[speler - 1][1].setCoords(pX - 50, pY + 95, 20);
			mainmenu.getBoxes()[speler - 1][2].setCoords(pX - 50, pY + 170, 20);
			mainmenu.getBoxes()[speler - 1][3].setCoords(pX - 50, pY + 245, 20);
		}
		for (int i = 0; i < 4; i++) {
			drawCheckbox(mainmenu.getBoxes()[speler - 1][i]);
		}
	}

	public void drawBeginMenu() {
		drawString((winsizeX / 2) - 100, 0, "tic-tac-toe");
		drawString(0, 100, "Speler 1:");
		drawPlayerSelect(0, 200, 1);
		drawString((winsizeX / 3) * 2, 100, "Speler 2:");
		drawPlayerSelect(((winsizeX / 3) * 2) + 50, 200, 2);
		drawButton(mainmenu.getButton());
		drawCircle(75, 50, 44, 10.0f, tkRed, tkGreen, tkBlue);
		drawCross(winsizeX - 200, 50, 44, 10.0f, tkRed, tkGreen, tkBlue);
		drawString(75, 500, "R: restart");
		drawString(winsizeX - 250, 500, "E: einde");
		drawString((winsizeX / 2) - 50, winsizeY - 110, "Start!");
	}

	public void drawGameMenu() {
		drawString(winsizeX - 300, 300, "R: restart");
		drawString(winsizeX - 300, 350, "E: einde");
	}

	public void drawEindeMenu(String speler) {
		if (speler.equals("U")) {
			drawString(winsizeX - 200, 300, "Draw!");
		} else {
			drawString(winsizeX - 300, 300, "Speler " + speler + " wins!");
		}
		drawString(winsizeX - 300, 350, "R: restart");
		drawString(winsizeX - 300, 400, "E: einde");
	}

	public void drawButton(Button b) {
		drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), bgRed,
				bgGreen, bgBlue);
	}

	public void drawCheckbox(Checkbox c) {
		drawQuadra(c.getX(), c.getY(), c.getRad(), bgRed, bgGreen, bgBlue);
		if (c.getState() == Checkbox.CHECKED) {
			drawCross(c.getX(), c.getY(), c.getRad(), 10.0f, tkRed, tkGreen,
					tkBlue);
		}
	}

	public void drawPlace(Place p) {
		drawQuadra(p.getX(), p.getY(), p.getRad(), bgRed, bgGreen, bgBlue);
		switch (p.getState()) {
		case Place.EMPTY: {
			break;
		}
		case Place.X: {
			drawCross(p.getX(), p.getY(), p.getRad(), 10.0f, tkRed, tkGreen,
					tkBlue);
			break;
		}
		case Place.O: {
			drawCircle(p.getX(), p.getY(), p.getRad() - 5, 10.0f, tkRed,
					tkGreen, tkBlue);
			break;
		}
		default: {

		}
		}
	}

	@SuppressWarnings("unchecked")
	public void drawString(int pX, int pY, String str) {
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		font.addAsciiGlyphs();
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		font.drawString(pX, pY, str, Color.white);
	}

	void drawQuadra(int pX, int pY, int pRad, float RED, float GREEN, float BLUE) {
		int hRad = pRad;
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(RED, GREEN, BLUE);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(pX - hRad, pY - hRad);
		GL11.glVertex2f(pX + hRad, pY - hRad);
		GL11.glVertex2f(pX + hRad, pY + hRad);
		GL11.glVertex2f(pX - hRad, pY + hRad);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	void drawRect(int pX, int pY, int pWidth, int pHeight, float RED,
			float GREEN, float BLUE) {
		int hWidth = pWidth / 2;
		int hHeight = pHeight / 2;
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(RED, GREEN, BLUE);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(pX - hWidth, pY - hHeight);
		GL11.glVertex2f(pX + hWidth, pY - hHeight);
		GL11.glVertex2f(pX + hWidth, pY + hHeight);
		GL11.glVertex2f(pX - hWidth, pY + hHeight);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	void drawCircle(int x, int y, int radius, float lineWidth, float RED,
			float GREEN, float BLUE) {
		double angle;
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(RED, GREEN, BLUE);
		GL11.glLineWidth(lineWidth);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int i = 0; i < 100; i++) {
			angle = (i * 2 * Math.PI) / 100;
			GL11.glVertex2d(x + (Math.cos(angle) * radius), y
					+ (Math.sin(angle) * radius));
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	void drawCross(int pX, int pY, int pRad, float lineWidth, float RED,
			float GREEN, float BLUE) {
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(RED, GREEN, BLUE);
		GL11.glLineWidth(lineWidth);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(pX - pRad, pY - pRad);
		GL11.glVertex2f(pX + pRad, pY + pRad);
		GL11.glVertex2f(pX + pRad, pY - pRad);
		GL11.glVertex2f(pX - pRad, pY + pRad);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	public static void main(String[] argv) {
		Game inputExample = new Game();
		inputExample.start();
	}
}