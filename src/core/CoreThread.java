package core;

import java.io.IOException;

public class CoreThread {

	public static boolean debugger = true;
	public static void main(String[] args) {

		try {
			Thread thread = new Master(); // Instantiates a server on a separate thread
			thread.start(); // Executes the server
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
