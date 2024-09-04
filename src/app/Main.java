package app;

import debug.Debug;
import gui.authentication.LoginDialog;

public class Main {
	public static void main(String[] args) {

		Debug.debugMig = false;
		
		Application.loadAllData();
		
		new LoginDialog();
		
	}
}
