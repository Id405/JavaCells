
public class Cell {
	Cell[][] cells;

	int x, y;

	boolean hidden;
	boolean bomb;
	boolean flag;
	int adjacent;

	Cell(Cell[][] cs, int xx, int yy) {
		cells = cs;

		x = xx;
		y = yy;

		hidden = true;
		bomb = false;
		adjacent = 0;
	}

	public void findAdjacent() { // Not my proudest code
		if (!bomb) {
			int sum = 0;
			int rowMod = ((y) % 2) - 1;

			if (x + rowMod > 0) {
				if (y > 1 && x + rowMod < cells[0].length) {
					if (cells[x + rowMod][y - 1].isBomb()) {
						sum++;
					}
				}
				if (y + 1 < cells.length && x + rowMod + 1 < cells[0].length) {
					if (cells[x + rowMod][y + 1].isBomb()) {
						sum++;
					}
				}
			}

			if (x + rowMod + 1 < cells[0].length) {
				if (y + 1 < cells.length) {
					if (cells[x + rowMod + 1][y + 1].isBomb()) {
						sum++;
					}
				}
				if (y > 0) {
					if (cells[x + rowMod + 1][y - 1].isBomb()) {
						sum++;
					}
				}
			}

			if (x > 0) {
				if (cells[x - 1][y].isBomb()) {
					sum++;
				}
			}

			if (x + 1 < cells[y].length) {
				if (cells[x + 1][y].isBomb()) {
					sum++;
				}
			}

			adjacent = sum;
		}
	}

	public int getAdjacent() {
		findAdjacent();
		if(bomb) {
			adjacent = 7;
		}
		return adjacent;
	}

	public boolean reveal() { // returns true if cell was a bomb
		hidden = false;
		return bomb;
	}

	public void makeBomb() {
		bomb = true;
	}

	public boolean isBomb() {
		return bomb;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isFlag() {
		return flag;
	}

	public void unhide() {
		if (!flag) {
			hidden = false;
		}
	}
}
