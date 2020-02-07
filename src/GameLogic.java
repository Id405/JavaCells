import java.awt.BorderLayout;

import javax.swing.JFrame;

public class GameLogic {
	JFrame frame;
	GameUi ui;

	GameLogic(JFrame frr) {
		frame = frr;

		ui = new GameUi(this);
		frame.add(ui, BorderLayout.CENTER);
	}

	public void lose() {
		ui.lose();
	}
}
