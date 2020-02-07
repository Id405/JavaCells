import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class ColorScheme {
	HashMap<String, Color> scheme;
	final static String SCHEMEDIR = System.getProperty("user.home") + "\\Documents\\JavaCells\\scheme.properties";

	ColorScheme() throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(SCHEMEDIR);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException e) {
			//TODO Create file
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw e;
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw e;
			} catch (NullPointerException e) {
				
			}
		}
		
		scheme = new HashMap<String, Color>();
		
		@SuppressWarnings("unchecked")
		Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
	    while (enums.hasMoreElements()) {
	      String key = enums.nextElement();
	      String value = prop.getProperty(key);
	      
	      String[] splitCol = value.split(", ");
	      Color col = new Color(Integer.parseInt(splitCol[0]), Integer.parseInt(splitCol[1]), Integer.parseInt(splitCol[2]));
	      
	      scheme.put(key, col);
	    }
	}
	
	public Color get(String key) {
		Color col = scheme.get(key);
		if(col == null) {
			return scheme.get("DEFAULTCOLOR");
		}
		return col;
	}
}
