package work;

import java.nio.charset.StandardCharsets;

public class CodeWork {

	public static long generateSerialVersionUIDbyClassName(Class c) {
		String str = c.getCanonicalName();
		
		byte[] bs = str.getBytes(StandardCharsets.UTF_8);
		
	    long result = 0;
	    for (int i = 0; i < bs.length; i++) {
	        result <<= 8;
	        result |= (bs[i] & 0xFF);
	    }
	    
		System.out.println("For class \"" + str + "\" generated serialVersionUI: " + result);
		return result;
	}
}
