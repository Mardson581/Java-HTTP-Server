import java.util.HashMap;

public class HttpRequest {
	private String method;
	private String path;
	private String realPath;
	private String file;
	private String http_version;
	private HashMap<String, String> headers;
	private StringBuilder body_data;

	// Constructor
	public HttpRequest(String data) {
		String[] lines = data.split("\n");
		this.method = lines[0].split(" ")[0];
		this.realPath = lines[0].split(" ")[1];
		this.http_version = lines[0].split(" ")[2];
		
		if (realPath.replace("/", "").length() > 0)
			this.path = "/" + realPath.split("/")[1];
		else
			this.path = "/";

		this.file = realPath.replace(path, "");
		this.headers = new HashMap<String, String>();
		
		if (lines.length > 1) {
			for (int i = 1; i < lines.length; i++) {
				if (lines[i].replaceAll("\r\n", "").isBlank()) {
					body_data = new StringBuilder();
					
					for (int j = i + 1; j < lines.length; j++) {
						body_data.append(lines[j]).append("\r\n");
					}
					
					break;
				}
				
				String[] header = lines[i].split(": ");
				headers.put(header[0], header[1]);
			}
		}
	}
	
	// Getters
	public String getMethod() {
		return method;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getRealPath() {
		return realPath;
	}
	
	public String getFile() {
		return file;
	}
	
	public String getHttpVersion() {
		return http_version;
	}
	
	public String getHeader(String header) {
		return headers.get(header);
	}
	
	public byte[] getBodyData() {
		return this.body_data.toString().trim().getBytes();
	}
	
	// Setters
	public void setMethod(String method) {
		this.method = method;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setHttpVersion(String http_version) {
		this.http_version = http_version;
	}
}
