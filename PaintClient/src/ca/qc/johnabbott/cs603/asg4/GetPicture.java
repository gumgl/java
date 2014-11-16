package ca.qc.johnabbott.cs603.asg4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import android.R;
import android.os.AsyncTask;

public class GetPicture<S, T> extends AsyncTask<String/* Param */, Boolean /* Progress */, Picture /* Result */> {
	
	private final DrawingView drawing;
	private String username;
	private HttpClient mHc = new DefaultHttpClient();

	public GetPicture(DrawingView view) {
		/*DEBUG*/System.out.println("GetPicture new");
		this.drawing = view;
	}
	
	@Override
    protected Picture doInBackground(String... params) {
		Picture picture = null;
		String username = (String)params[0];
		HttpURLConnection urlConnection = null;
		try {
			///*DEBUG*/System.out.println("PostPicture doInBackground");
			URL url = new URL("http://10.0.2.2:9999/"+username+"/picture");
			urlConnection = (HttpURLConnection) url.openConnection();
			
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			//OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
			ObjectInputStream oin = new ObjectInputStream(in);
			picture = (Picture)oin.readObject();
			in.close();
			///*DEBUG*/System.out.println("Picture posted to "+username);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
			return picture;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return picture; //result;
    }
	
	@Override
	protected void onPostExecute(Picture pic) {
		super.onPostExecute(pic);
		/*DEBUG*/System.out.println("Picture set! "+pic.shapes.size());
		drawing.setPicture(pic);
		drawing.invalidate();
	}
}
