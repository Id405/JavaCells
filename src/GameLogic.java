import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JFrame;

public class GameLogic {
	JFrame frame;
	GameUi ui;

	final static String SCOREDIR = System.getProperty("user.home")
			+ "\\AppData\\Roaming\\JavaCells\\highscore.properties";
	final static String DEFAULTSCORE = "#Fri Feb 21 08:00:49 AKST 2020\r\n" + "easy=10000000000\r\n"
			+ "medium=10000000000\r\n" + "insane=10000000000\r\n" + "hard=10000000000\r\n" + "";

	int timerType;
	private long timerStartTime = 0;

	private Properties highScoreProperties;

	GameLogic(JFrame frr) {
		frame = frr;

		loadHighscore();

		ui = new GameUi(this);
		frame.add(ui, BorderLayout.CENTER);
	}

	public void packWindow() {
		frame.setExtendedState(Frame.NORMAL);
		frame.setSize(1000, 400);
//		frame.pack();
	}

	public void maximizeWindow() {
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	public void lose() {
		getTimer();
		ui.lose();
	}

	public void win(long time) {
		if (time < getHighscore(timerType)) {
			setHighScore(timerType, time);
			writeHighscore();
		}
		ui.lose();
	}

	public void startTimer(int timerType) {
		timerStartTime = System.nanoTime();
		this.timerType = timerType;
	}

	public long getTimer() {
		return (System.nanoTime() - timerStartTime) / 1000000;
	}

	public void writeHighscore() {
		try {
			highScoreProperties.store(new FileOutputStream(SCOREDIR), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadHighscore() {
		FileInputStream fis = null;
		Properties prop = null;

		for (int i = 0; i < 2; i++) {
			try {
				fis = new FileInputStream(SCOREDIR);
				prop = new Properties();
				prop.load(fis);
			} catch (FileNotFoundException e) {
				try {
					File file = new File(SCOREDIR);
					file.getParentFile().mkdirs();
					file.createNewFile();

					FileOutputStream fStream = new FileOutputStream(SCOREDIR);
					fStream.write(DEFAULTSCORE.getBytes());
					fStream.flush();
					fStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		highScoreProperties = prop;
	}

	public long getHighscore(int type) {
		if (type == GameUi.playListener.EASY) {
			return Long.parseLong(highScoreProperties.getProperty("easy"));
		} else if (type == GameUi.playListener.MEDIUM) {
			return Long.parseLong(highScoreProperties.getProperty("medium"));
		} else if (type == GameUi.playListener.HARD) {
			return Long.parseLong(highScoreProperties.getProperty("hard"));
		} else if (type == GameUi.playListener.INSANE) {
			return Long.parseLong(highScoreProperties.getProperty("insane"));
		}
		return -1;
	}

	public String getHighscoreFormatted(int type) {
		long time = getHighscore(type);
		if (time >= 5000000000l) {
			return "n\\a";
		}

		return Helpers.formatTime(time);
	}

	public void setHighScore(int type, long val) {
		if (type == GameUi.playListener.EASY) {
			highScoreProperties.setProperty("easy", "" + val);
		} else if (type == GameUi.playListener.MEDIUM) {
			highScoreProperties.setProperty("medium", "" + val);
		} else if (type == GameUi.playListener.HARD) {
			highScoreProperties.setProperty("hard", "" + val);
		} else if (type == GameUi.playListener.INSANE) {
			highScoreProperties.setProperty("insane", "" + val);
		}
	}
}
