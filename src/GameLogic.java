import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;

public class GameLogic {
	JFrame frame;
	GameUi ui;
	
	final static String SCOREDIR = System.getProperty("user.home") + "\\Documents\\JavaCells\\highscore";

	GameLogic(JFrame frr) {
		frame = frr;

		ui = new GameUi(this);
		frame.add(ui, BorderLayout.CENTER);
	}

	public void lose() {
		ui.lose();
	}
	
	public void win() {
		ui.lose();
	}
	
	public int loadHighscore() {
		try {
			String content = new String ( Files.readAllBytes( Paths.get(SCOREDIR) ) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
