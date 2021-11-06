package game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Saving {

	public static final String PATH = System.getProperty("user.dir") + "\\";
	
	public static void writeObject(Object o, String outname) {
        ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(PATH + outname));
			out.writeObject(o);
			out.close();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	
	
	public static Object readObject(String inname) throws ClassNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(PATH + inname));
		Object o = in.readObject();
		in.close();
		return o;
	}
}
