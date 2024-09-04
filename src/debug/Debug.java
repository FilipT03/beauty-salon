package debug;

public class Debug {
	public static boolean debugMig = false;

	/*
	 * Enables the Mig Layout debug mode if the debug boolean is set to true
	 */
	public static String debugCons() { 
		if(debugMig)
			return ", debug";
		else
			return "";
	};
	public static String debugCons(boolean overwrite) { 
		if(overwrite)
			return ", debug";
		else
			return "";
	};
}
