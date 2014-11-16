import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


@SuppressWarnings("restriction")
public class PictureServer implements HttpHandler {

	/* API
	 * 
	 * POST: "user"/"name"
	 *   - Store a picture at the specified location.
	 *   
	 * GET: "user"/"name"
	 *   - Retrieve the picture at the specified location.
	 */
	
	private static final String ROOT = "./repo/";
	
	/* Patterns for URL validation:
	 * 
	 * username    = alphanum with first alpha first character.
	 * picturename = alphanum.
	 *  */
	private static final String validUserName = "[a-zA-Z]\\w*";
	private static final String validPictureName = "\\w+";

    @Override
	public void handle(HttpExchange exchange) throws IOException {
		
		String requestMethod = exchange.getRequestMethod();
		
		System.out.println("Received request:" + requestMethod + 
				           " " + exchange.getRequestURI() +
				           " at " + new Date());
		
		try {
			if(requestMethod.equalsIgnoreCase("GET"))
				processGet(exchange);
			else if(requestMethod.equalsIgnoreCase("POST"))
				processPost(exchange);
			else 
				throw new Exception("Unsupported request method");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			exchange.sendResponseHeaders(404, 0);
			exchange.getResponseBody().close();
		}
	}
	
	private void processGet(HttpExchange exchange) throws IOException, FileNotFoundException {
		
		Pattern validURL = Pattern.compile("/(" + validUserName + ")/("+ validPictureName  + ")");
		Matcher matcher = validURL.matcher(exchange.getRequestURI().toString());
		if(!matcher.matches())
			throw new IOException("URI does not conform to the API.");
		
		File directory = new File(ROOT, matcher.group(1));
		File picture = new File(directory, matcher.group(2));
		FileInputStream fin = new FileInputStream(picture);			

		exchange.sendResponseHeaders(200, 0);

		BufferedOutputStream os = new BufferedOutputStream(exchange.getResponseBody());
		copyStream(fin, os);
		fin.close();
		os.close();

	}

	private void processPost(HttpExchange exchange) throws IOException, FileNotFoundException {

		Pattern validURL = Pattern.compile("/(" + validUserName + ")/("+ validPictureName + ")");
		Matcher matcher = validURL.matcher(exchange.getRequestURI().toString());
		if(!matcher.matches())
			throw new IOException("URI does not conform to the API.");

		File directory = new File(ROOT, matcher.group(1));
		if(!directory.exists()) {
			// make directory
			if(!directory.mkdirs()) 
				throw new IOException("Could not create directory " + directory); 
		}
		else if(!directory.isDirectory())
			throw new FileNotFoundException("Not a directory: " + directory);
	
		File picture = new File(directory, matcher.group(2));
		
		FileOutputStream fout = new FileOutputStream(picture);
		
		BufferedInputStream is = new BufferedInputStream(exchange.getRequestBody());
		copyStream(is, fout);
		is.close();
		fout.close();

		exchange.sendResponseHeaders(200, 0);
		exchange.getResponseBody().close();
	}


	// stack overflow question 411592
	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024];
		int length;
		while((length = input.read(buffer, 0, buffer.length)) > 0) 
			output.write(buffer, 0, length);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		int port = Integer.parseInt(args[0]);
		InetSocketAddress addr = new InetSocketAddress(port);

		try{
			HttpServer server = HttpServer.create(addr, 0);
			server.createContext("/", new PictureServer());
			server.setExecutor(Executors.newCachedThreadPool());
			server.start();
			System.out.println("Server is listening on port " + port);
		}
		catch (IOException e) {
			System.err.println("Could not start server on port: " + port);
		}
	}

	

}
