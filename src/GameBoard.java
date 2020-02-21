import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel {
	final static boolean CHEAT = true;

	GameLogic logic;
	ColorScheme scheme;

	Timer t;

	int w, h, count;
	int type = 0;
	long time = -1;
	int flagged = 0;

	double hexSize = 25;
	double hexGapScale = 20;
	double vStretch = 24;
	double vRowStretch = 0.87;
	double hexMargin = 50;
	double minHexSize;

	double hexWidth;
	double hexHeight;

	double xTOffset = 0.3;
	double yTOffset = 0.1;

	double clickMargin = 10;

	Cell[][] cells;

	private boolean lose;
	private boolean win;

	GameBoard(GameLogic g, int width, int height, int c, int type) {
		logic = g;
		w = width;
		h = height;
		count = c;

		try {
			scheme = new ColorScheme();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setBackground(scheme.get("background"));

		cells = new Cell[w][h];
		initializeBoard(count);

		System.out.println(width + ", " + height + " " + c);

		addMouseListener(new MouseListener(this));

		t = new Timer(10, new RepaintListener());
		t.start();
	}

	protected void paintComponent(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHints(rh);

		super.paintComponent(g);

		paintHexagons(g);

		g.drawString(flagged + " / " + count, 10, 10);

		long millis = logic.getTimer();

		if (time != -1) {
			millis = time;
		}

		String timeFormat = Helpers.formatTime(millis);

		g.drawString(timeFormat, 10, getHeight() - 30);

		if (lose) {
			g.setColor(scheme.get("flag"));
			g.setFont(new Font("Helvetica", Font.PLAIN, 36));
			g.drawString("You lose, Click to restart", getWidth() / 2 - 36 * "You lose, Click to restart".length() / 4,
					50);
		}

		if (win) {
			g.setColor(scheme.get("flag"));
			g.setFont(new Font("Helvetica", Font.PLAIN, 36));
			g.drawString("You WIN, Click to restart", getWidth() / 2 - 36 * "You WIN, Click to restart".length() / 4,
					50);
		}
	}

	@SuppressWarnings("unused")
	void paintHexagons(Graphics2D g) {
		hexWidth = ((double) (w) * hexSize * 2);
		hexHeight = ((double) (h) * hexSize * 2) * vRowStretch;

		double tempHexSizeW = (double) hexSize * ((double) getWidth() - hexMargin) / hexWidth;
		double tempHexSizeH = (double) hexSize * ((double) getHeight() - hexMargin) / hexHeight;

		if (tempHexSizeW < tempHexSizeH) {
			hexSize = tempHexSizeW;
		} else {
			hexSize = tempHexSizeH;
		}

		hexWidth = ((double) w * hexSize * 2);
		hexHeight = ((double) (h - 1) * hexSize * 2) * vRowStretch;

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				double xOffset = (double) (y % 2) / 2;
				g.setColor(scheme.get("unhidden"));
				if (cells[x][y].bomb) {
					g.setColor(scheme.get("bomb"));
				}
				if (cells[x][y].hidden) {
					g.setColor(scheme.get("hidden"));
				}
				if (cells[x][y].flag) {
					g.setColor(scheme.get("flag"));
				}

				if (cells[x][y].bomb && CHEAT) {
					g.setColor(scheme.get("bomb"));
				}

				double yPreOffset = (double) y * hexSize * 2 * vRowStretch;
				double yPos = ((yPreOffset + ((double) getHeight() / 2 - hexHeight / 2)));

				double xPreOffset = (((double) x) + xOffset) * hexSize * 2;
				double xPos = (xPreOffset + ((double) getWidth() / 2 - hexWidth / 2));

				paintHexagon(g, xPos, yPos);

				g.setColor(scheme.get("text"));

				if (cells[x][y].getAdjacent() != 0 && !cells[x][y].isHidden() && !cells[x][y].isBomb()) {
					g.setFont(new Font("Helvetica", Font.PLAIN, 12));

					String text = "" + cells[x][y].getAdjacent();

					FontMetrics metrics = g.getFontMetrics(getFont());

					int x1 = (int) (xPos - metrics.stringWidth(text) / 2);
					// Determine the Y coordinate for the text (note we add the ascent, as in java
					// 2d 0 is top of the screen)
					int y1 = (int) ((yPos - metrics.getHeight() / 2) + metrics.getAscent());

					g.drawString(text, x1, y1);
				}
			}
		}
	}

	void paintHexagon(Graphics2D g, double x, double y) {
		int sides = 6;
		Polygon p = new Polygon();

		double hexGap = hexSize / hexGapScale;

		double theta = 2 * Math.PI / sides;
		for (int i = 0; i < sides; ++i) {
			int xx = (int) (Math.cos(theta * (double) i + Math.PI / 2) * (hexSize + hexGap) + x);
			int yy = (int) (Math.sin(theta * (double) i + Math.PI / 2) * (hexSize + hexGap) + y);
			p.addPoint(xx, yy);
		}
		g.fillPolygon(p);
	}

	private void initializeBoard(int count) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				cells[x][y] = new Cell(cells, x, y);
			}
		}

		Random r = new Random();

		for (int i = 0; i < count; i++) {
			int x = r.nextInt(w - 2) + 1;
			int y = r.nextInt(h - 2) + 1;
			cells[x][y].makeBomb();
		}

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				cells[x][y].findAdjacent();
			}
		}

		for (int x = 0; x < w; x++) { // unhide edges
			for (int y = 0; y < h; y++) {
				if (x == 0 || y == 0 || x == cells[0].length - 1 || y == cells.length - 1) {
					unhide(x, y);
				}
			}
		}
	}

	public void mouseClicked(int xx, int yy, boolean rightClick) {
		if (lose) {
//			initializeBoard(count);
//			lose = false;
//			repaint();
			logic.lose();
			return;
		}

		if (win) {
			logic.win(time);
			return;
		}

		int xxx = 0;
		int yyy = 0;

		double minDist = Integer.MAX_VALUE;

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				double xOffset = (double) (y % 2) / 2;

				double yPreOffset = (double) y * hexSize * 2 * vRowStretch;
				int yPos = (int) ((yPreOffset + ((double) getHeight() / 2 - hexHeight / 2)));

				double xPreOffset = (((double) x) + xOffset) * hexSize * 2;
				int xPos = (int) (xPreOffset + ((double) getWidth() / 2 - hexWidth / 2));

				double distance = Math.hypot(xx - xPos, yy - yPos);
				if (distance < minDist) {
					xxx = x;
					yyy = y;
					minDist = distance;
				}

			}
		}

		if (minDist < hexSize + clickMargin) {
			if (rightClick) {
				if (cells[xxx][yyy].isHidden()) {
					cells[xxx][yyy].flag = !cells[xxx][yyy].flag;

					if (cells[xxx][yyy].flag == true) {
						flagged++;
					} else {
						flagged--;
					}
				}
			} else {
				if (cells[xxx][yyy].isBomb()) {
					lose();
					return;
				} else {
					if (!cells[xxx][yyy].isFlag()) {
						unhide(xxx, yyy);
					}
				}

				if (checkWin()) {
					win = true;
					time = logic.getTimer();
					t.stop();
					repaint();
				}
			}
		}
		repaint();
	}

	public void unhide(int x, int y) { // Just terrible code here
		cells[x][y].unhide();

		int rowMod = ((y) % 2) - 1;

		if (cells[x][y].adjacent == 0) {
			if (x + rowMod > 0) {
				if (y > 1 && x + rowMod < cells[0].length) {
					if (cells[x + rowMod][y - 1].getAdjacent() == 0 && cells[x + rowMod][y - 1].isHidden()
							&& !cells[x + rowMod][y - 1].isBomb()) {
						unhide(x + rowMod, y - 1);
					}
					cells[x + rowMod][y - 1].hidden = false;
				}
				if (y + 1 < cells.length && x + rowMod + 1 < cells[0].length) {
					if (cells[x + rowMod][y + 1].getAdjacent() == 0 && cells[x + rowMod][y + 1].isHidden()
							&& !cells[x + rowMod][y + 1].isBomb()) {
						unhide(x + rowMod, y + 1);
					}
					cells[x + rowMod][y + 1].hidden = false;
				}
			}

			if (x + rowMod + 1 < cells[0].length) {
				if (y + 1 < cells.length) {
					if (cells[x + rowMod + 1][y + 1].getAdjacent() == 0 && cells[x + rowMod + 1][y + 1].isHidden()
							&& !cells[x + rowMod + 1][y + 1].isBomb()) {
						unhide(x + rowMod + 1, y + 1);
					}
					cells[x + rowMod + 1][y + 1].hidden = false;
				}
				if (y > 0) {
					if (cells[x + rowMod + 1][y - 1].getAdjacent() == 0 && cells[x + rowMod + 1][y - 1].isHidden()
							&& !cells[x + rowMod + 1][y - 1].isBomb()) {
						unhide(x + rowMod + 1, y - 1);
					}
					cells[x + rowMod + 1][y - 1].hidden = false;
				}
			}

			if (x > 0) {
				if (cells[x - 1][y].getAdjacent() == 0 && cells[x - 1][y].isHidden() && !cells[x - 1][y].isBomb()) {
					unhide(x - 1, y);
				}
				cells[x - 1][y].hidden = false;
			}

			if (x + 1 < cells[0].length) {
				if (cells[x + 1][y].getAdjacent() == 0 && cells[x + 1][y].isHidden() && !cells[x + 1][y].isBomb()) {
					unhide(x + 1, y);
				}
				cells[x + 1][y].hidden = false;
			}
		}
	}

	private void lose() {
		showBombs();
		repaint();

		lose = true;
		t.stop();
	}

	private void showBombs() {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (cells[x][y].bomb) {
					cells[x][y].flag = false;
					cells[x][y].unhide();
				}
			}
		}
	}

	private boolean checkWin() {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (cells[x][y].hidden && !cells[x][y].bomb) {
					return false;
				}
			}
		}
		return true;
	}

	static class MouseListener extends MouseAdapter {
		GameBoard gboard;

		MouseListener(GameBoard g) {
			gboard = g;
		}

		public void mouseClicked(MouseEvent e) {
			gboard.mouseClicked(e.getX(), e.getY(), e.getButton() == MouseEvent.BUTTON3);
		}
	}

	class RepaintListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			repaint();
		}
	}
}
