import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class ColorScheme {
	HashMap<String, Color> scheme;
	final static String SCHEMEDIR = System.getProperty("user.home")
			+ "\\AppData\\Roaming\\JavaCells\\scheme.properties";
	final static String DEFAULTSCHEME = "background = 252, 253, 252\r\n" + "text = 0, 0, 0\r\n"
			+ "flag = 255, 116, 48\r\n" + "unhidden = 184, 184, 184\r\n" + "hidden = 130, 130, 130\r\n"
			+ "bomb = 46, 46, 46\r\n" + "\r\n" + "DEFAULTCOLOR = 255, 255, 255";

	ColorScheme() throws IOException {
		FileInputStream fis = null;
		Properties prop = null;

		for (int i = 0; i < 2; i++) {
			try {
				fis = new FileInputStream(SCHEMEDIR);
				prop = new Properties();
				prop.load(fis);
			} catch (FileNotFoundException e) {
				File file = new File(SCHEMEDIR);
				file.getParentFile().mkdirs();
				file.createNewFile();

				FileOutputStream fStream = new FileOutputStream(SCHEMEDIR);
				fStream.write(DEFAULTSCHEME.getBytes());
				fStream.flush();
				fStream.close();
			} catch (IOException e) {
				throw e;
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					throw e;
				} catch (NullPointerException e) {

				}
			}
		}

		scheme = new HashMap<String, Color>();

		@SuppressWarnings("unchecked")
		Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			String value = prop.getProperty(key);

			String[] splitCol = value.split(", ");
			Color col = new Color(Integer.parseInt(splitCol[0]), Integer.parseInt(splitCol[1]),
					Integer.parseInt(splitCol[2]));

			scheme.put(key, col);
		}
	}

	public Color get(String key) {
		Color col = scheme.get(key);
		if (col == null) {
			return scheme.get("DEFAULTCOLOR");
		}
		return col;
	}
}
