package ca.qc.johnabbott.cs603.asg4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import android.R;
import android.os.AsyncTask;

public class PostPicture<S, T> extends AsyncTask<String/* Param */, Boolean /* Progress */, Boolean /* Result */> {
	
	private DrawingView drawing;
	private Picture picture;
	private HttpClient mHc = new DefaultHttpClient();

	public PostPicture(DrawingView view) {
		/*DEBUG*/System.out.println("PostPicture new");
		this.drawing = view;
		this.picture = drawing.getPicture();
	}
	
	@Override
    protected Boolean doInBackground(String... params) {
		/*DEBUG*/System.out.println("PostPicture doInBackground "+params[0].getClass().getName());
		String username = (String)params[0];
		HttpURLConnection urlConnection = null;
		try {
			///*DEBUG*/System.out.println("PostPicture doInBackground");
			URL url = new URL("http://10.0.2.2:9999/"+username+"/picture");
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);
			
			//InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
			ObjectOutputStream oout = new ObjectOutputStream(out);
			
			oout.writeObject(picture);
			oout.close();
			///*DEBUG*/System.out.println("Picture posted to "+username);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		//publishProgress(true);
		// Do the usual httpclient thing to get the result
		return true; //result;
    }
}
