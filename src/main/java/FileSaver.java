import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class FileSaver {
	private BufferedOutputStream output;
	private String directory;

	public FileSaver(String fileName, byte[] data) {
		this.directory = Main.DIRECTORY;
		
		try {
			Path path = FileSystems.getDefault().getPath(directory, fileName);
			
			this.output = new BufferedOutputStream(Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE));
			output.write(data);
			output.flush();
			
			output.close();
		} catch (IOException e) {
			ConsoleWriter.writeError(e.getMessage());
		}
	}
}
