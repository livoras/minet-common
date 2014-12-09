package Common.src;

public class Logger {
	public static void log(Object obj) {
		System.out.println(obj);
	}

	public static void warn(Object obj) {
		System.out.println("WARNING: " + obj);
	}
}
