import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

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
		JLabel mLabel = new JLabel("MINESWEEPER!!!");
		mLabel.setFont(mLabel.getFont().deriveFont(120f));
		mLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(mLabel, BorderLayout.NORTH);

		JPanel centerPanelFlow = new JPanel();
		centerPanelFlow.setLayout(new FlowLayout());

		JPanel centerPanel = new JPanel();

		sizeField = new JTextField();
		mineField = new JTextField();

		centerPanel.setLayout(new GridLayout(3, 2));

		JLabel dLabel = new JLabel("Difficulty: ");
		dLabel.setFont(mLabel.getFont().deriveFont(40f));
		centerPanel.add(dLabel);

		centerPanel.add(mineField);

		JLabel sLabel = new JLabel("Size: ");
		sLabel.setFont(mLabel.getFont().deriveFont(40f));

		centerPanel.add(sLabel);
		centerPanel.add(sizeField);

		centerPanelFlow.add(centerPanel);

		this.add(centerPanelFlow, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 5));

		buttonPanel.add(new JLabel());

		JLabel eSL = new JLabel(logic.getHighscoreFormatted(playListener.EASY));
		eSL.setHorizontalAlignment(SwingConstants.CENTER);
		buttonPanel.add(eSL);

		JLabel mSL = new JLabel(logic.getHighscoreFormatted(playListener.MEDIUM));
		mSL.setHorizontalAlignment(SwingConstants.CENTER);
		buttonPanel.add(mSL);

		JLabel hSL = new JLabel(logic.getHighscoreFormatted(playListener.HARD));
		hSL.setHorizontalAlignment(SwingConstants.CENTER);
		buttonPanel.add(hSL);

		JLabel iSL = new JLabel(logic.getHighscoreFormatted(playListener.INSANE));
		iSL.setHorizontalAlignment(SwingConstants.CENTER);
		buttonPanel.add(iSL);

		JButton customPlayButton = new JButton("Play With Custom Settings");
		customPlayButton.addActionListener(new playListener(this, playListener.CUSTOM));
		buttonPanel.add(customPlayButton);

		JButton easyPlayButton = new JButton("Easy");
		easyPlayButton.addActionListener(new playListener(this, playListener.EASY));
		buttonPanel.add(easyPlayButton);

		JButton mediumPlayButton = new JButton("Medium");
		mediumPlayButton.addActionListener(new playListener(this, playListener.MEDIUM));
		buttonPanel.add(mediumPlayButton);

		JButton hardPlayButton = new JButton("Hard");
		hardPlayButton.addActionListener(new playListener(this, playListener.HARD));
		buttonPanel.add(hardPlayButton);

		JButton insanePlayButton = new JButton("Insane");
		insanePlayButton.addActionListener(new playListener(this, playListener.INSANE));
		buttonPanel.add(insanePlayButton);

		this.add(buttonPanel, BorderLayout.SOUTH);

		logic.packWindow();

		repaint();
		revalidate();
	}

	private void addGameScreen() {
		addGameScreen(0);
	}

	private void addGameScreen(int type) {
		this.removeAll();
		board = new GameBoard(logic, width, height,
				(int) ((double) width * (double) height * ((double) minecount / 100.0)), type);
		this.setLayout(new BorderLayout());
		this.add(board, BorderLayout.CENTER);
		logic.maximizeWindow();
		repaint();
		revalidate();
	}

	public void Custom() {
		try {
			int tSize = Integer.parseInt(sizeField.getText());
			if (tSize > 3) {
				width = tSize;
				height = tSize;
			}
		} catch (NumberFormatException e) {

		}

		try {
			int tmines = Integer.parseInt(mineField.getText());
			if (tmines > 1) {
				minecount = tmines;
			}
		} catch (NumberFormatException e) {

		}

		addGameScreen();
	}

	public void Easy() {
		width = 30;
		height = 30;
		minecount = 10;

		addGameScreen(1);
		logic.startTimer(playListener.EASY);
	}

	public void Medium() {
		width = 40;
		height = 40;
		minecount = 30;

		addGameScreen(2);
		logic.startTimer(playListener.MEDIUM);
	}

	public void Hard() {
		width = 50;
		height = 50;
		minecount = 50;

		addGameScreen(3);
		logic.startTimer(playListener.HARD);
	}

	public void Insane() {
		width = 80;
		height = 80;
		minecount = 70;

		addGameScreen(4);
		logic.startTimer(playListener.INSANE);
	}

	static class playListener implements ActionListener {
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
			if (type == CUSTOM) {
				parent.Custom();
			} else if (type == EASY) {
				parent.Easy();
			} else if (type == MEDIUM) {
				parent.Medium();
			} else if (type == HARD) {
				parent.Hard();
			} else if (type == INSANE) {
				parent.Insane();
			}
		}

	}
}
