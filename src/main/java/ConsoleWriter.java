public class ConsoleWriter {
	public static final String ANSI_RED = "\033[0;31m";
	public static final String ANSI_WHITE = "\033[0;37m";
	public static final String ANSI_BLUE = "\033[0;36m";

	public static void writeError(String err) {
		System.out.format("%s[ERROR]%s %s\n", ANSI_RED, ANSI_WHITE, err);
	}
	
	public static void writeInfo(String info) {
		System.out.format("%s[INFO]%s %s\n", ANSI_BLUE, ANSI_WHITE, info);
	}
}
