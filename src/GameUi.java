import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameUi extends JPanel {
	int width = 20;
	int height = 20;
	int minecount = 10;

	GameLogic logic;
	GameBoard board;

	private JTextField sizeField;
	private JTextField mineField;

	GameUi(GameLogic grr) {
		logic = grr;
		addTitleScreen();
	}

	void lose() {
		addTitleScreen();
	}

	private void addTitleScreen() {
		this.removeAll();
		this.setLayout(new BorderLayout());
		this.add(new JLabel("MINESWEEPER!!!"), BorderLayout.NORTH);

		JPanel centerPanel = new JPanel();

		sizeField = new JTextField();
		mineField = new JTextField();

		centerPanel.setLayout(new GridLayout(3, 2));
		centerPanel.add(new JLabel("Difficulty: "));
		centerPanel.add(mineField);
		centerPanel.add(new JLabel("Size: "));
		centerPanel.add(sizeField);

		this.add(centerPanel, BorderLayout.CENTER);

		JButton playButton = new JButton("Play With Custom Settings");
		playButton.addActionListener(new playListener(this, playListener.CUSTOM));
		this.add(playButton, BorderLayout.SOUTH);

		repaint();
		revalidate();
	}

	private void addGameScreen() {
		this.removeAll();
		board = new GameBoard(logic, width, height, (int) ((double) width * (double) height * ((double) minecount / 100.0)));
		this.setLayout(new BorderLayout());
		this.add(board, BorderLayout.CENTER);
		repaint();
		revalidate();
	}

	public void Custom() {
		try {
			int tSize = Integer.parseInt(sizeField.getText());
			if (tSize > 10) {
				width = tSize;
				height = tSize;
			}

			int tmines = Integer.parseInt(mineField.getText());
			if (tmines > 1) {
				minecount = tmines;
			}
		} catch (NumberFormatException e) {
			
		}
		addGameScreen();
	}

	class playListener implements ActionListener {
		public static final int CUSTOM = 0;
		public static final int EASY = 1;
		public static final int MEDIUM = 2;
		public static final int HARD = 3;
		public static final int INSANE = 4;

		int type;
		GameUi parent;

		playListener(GameUi parent, int type) {
			this.type = type;
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (type == 0) {
				parent.Custom();
			}
		}

	}
}
