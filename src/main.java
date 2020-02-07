import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class main {
	static final int WIDTH = 800;
	static final int HEIGHT = 800;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setSize(800, 800);
		frame.setLayout(new BorderLayout());

		GameLogic gameLogic = new GameLogic(frame);

		frame.setVisible(true);
		frame.revalidate();
		frame.getIgnoreRepaint();
	}
}
