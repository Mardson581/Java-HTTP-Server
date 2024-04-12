import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.FileSystems;
import java.io.BufferedInputStream;
import java.io.IOException;

public class FileLoader {
	private String directory = Main.DIRECTORY;
	private String file_data = null;
	private BufferedInputStream input;
	private Path file;
	private byte[] buffer;
	
	private int BUFFER_SIZE = 4096;
	
	public FileLoader(String path) {
		try {
			this.file = FileSystems.getDefault().getPath(directory, path);
			
			if (!Files.exists(file))
				return;

			this.input = new BufferedInputStream(Files.newInputStream(this.file, StandardOpenOption.READ));
			this.BUFFER_SIZE = input.available();
			this.buffer = new byte[BUFFER_SIZE];
			
			int b;
			do {
				b = input.read(buffer, 0, BUFFER_SIZE);
			} while (b != -1);
			
			this.file_data = new String(buffer);
			input.close();
		} catch (IOException e) {
			ConsoleWriter.writeError(e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return this.file_data;
	}
	
	public byte[] getBytes() {
		return this.buffer;
	}
}
