package prover.utility.utilities;

public class Logger {

	public static void log(String str) {
		System.out.println(str);
	}
	
	public static void log(Object obj) {
		log(obj.toString());
	}
	
}
