import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class ClientThread implements Runnable {
	private BufferedInputStream input;
	private BufferedOutputStream output;
	private HttpRequest req;
	private Socket client;
	
	private static final int BUFFER_SIZE = 1024;

	public ClientThread(Socket client) {
		this.client = client;
		
		try {
			this.input = new BufferedInputStream(client.getInputStream());
			this.output = new BufferedOutputStream(client.getOutputStream());
		} catch (IOException e) {
			ConsoleWriter.writeError(e.getMessage());
		}
	}
	
	@Override
	public void run() {
		try {
			// Let's read the client request
			byte[] buffer = new byte[BUFFER_SIZE];
			ConsoleWriter.writeInfo("Reading data...");
			
			int d;
			do {
				d = input.read(buffer, 0, BUFFER_SIZE);
			} while (d == -1);
			
			String data = new String(buffer);
			
			ConsoleWriter.writeInfo("Client requested:\n" + data + "\n");
			this.req = new HttpRequest(data);
			
			// Writing the response
			switch (req.getPath()) {
				case "/":
					output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
				break;
				
				case "/echo":
					output.write("HTTP/1.1 200 OK\r\n".getBytes());
					output.write("Content-type: text/plain\r\n".getBytes());
					output.write(String.format("Content-Length: %d\r\n\r\n", req.getFile().replaceFirst("/", "").length()).getBytes());
					output.write(req.getFile().replaceFirst("/", "").getBytes());
				break;
				
				case "/user-agent":
					String user = req.getHeader("User-Agent");
					output.write("HTTP/1.1 200 OK\r\n".getBytes());
					output.write("Content-type: text/plain\r\n".getBytes());
					output.write(String.format("Content-Length: %d\r\n\r\n", (user.length() - 1)).getBytes());
					output.write(user.getBytes());
				break;
				
				case "/files":
				
					if (req.getMethod().equals("GET")) {
						FileLoader file = new FileLoader(req.getFile().replaceFirst("/", ""));

						if (file.toString() != null) {
							output.write("HTTP/1.1 200 OK\r\n".getBytes());
							output.write("Content-Type: application/octet-stream\r\n".getBytes());
							output.write(String.format("Content-Length: %d\r\n\r\n", file.getBytes().length).getBytes());
							output.write(file.getBytes());
						} else {
							output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
						}
					} else {
						FileSaver writer = new FileSaver(req.getFile().replaceFirst("/", ""), req.getBodyData());
						output.write("HTTP/1.1 201 OK\r\n\r\n".getBytes());
					}
				break;
				
				default:
					output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
				break;
			}
			
			ConsoleWriter.writeInfo("Writing data...");
			output.flush();	

			client.close();
		} catch (IOException e) {
			ConsoleWriter.writeError(e.getMessage());
		}
	}
}
