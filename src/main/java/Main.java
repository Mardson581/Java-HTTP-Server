import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	public static final int SERVER_PORT = 4221;
	public static String DIRECTORY = "";

	public static void main(String[] args) {
		ServerSocket server = null;
		Socket client = null;

		if (args.length > 0) {

			switch (args[0]) {
				case "--directory":
					if (args.length < 2) {
						System.out.println("Usage: java Main [OPTION] [PATH]\nValid arguments:");
						System.out.format("--help: %10-s", "Print all valid arguments\n");
						System.out.format("--directory: %10-s", "Specify the web server directory when looking for files\n");
						System.exit(0);
					}
				
					Main.DIRECTORY = args[1];
				break;
				
				case "--help":
					System.out.println("Usage: java Main [OPTION]\nValid arguments:");
					System.out.format("--help: %10s", "Print all valid arguments\n");
					System.out.format("--directory <path>: %10s", "Specify the web server directory when looking for files\n");
					System.exit(0);
				break;
				
				default:
					System.out.format("Invalid option: %s\nTry --help for more information\n", args[0]);
				break;
			}
		}
		
		try {
			server = new ServerSocket(SERVER_PORT);
			server.setReuseAddress(true);
			ConsoleWriter.writeInfo("ServerSocket started. Waiting for clients...");
			
			while (true) {
				new Thread(new ClientThread(server.accept())).start();
				ConsoleWriter.writeInfo("Client accepted!");
			}
		} catch (IOException e) {
			ConsoleWriter.writeError(e.getMessage());
		}
	}
}
