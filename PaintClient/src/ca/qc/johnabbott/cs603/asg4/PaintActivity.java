package ca.qc.johnabbott.cs603.asg4;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ca.qc.johnabbott.cs603.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PaintActivity extends Activity {
	
	private DrawingView drawing;
	private Dialog current;
	private String username;
	private AsyncTask<String, Boolean, Boolean> post;
	private AsyncTask<String, Boolean, Picture> get;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*DEBUG*/System.out.println("PaintActivity onCreate!");
		setContentView(R.layout.activity_paint);
		drawing = (DrawingView)this.findViewById(R.id.drawing_view);
		
		Intent intent = getIntent();
		this.username = (String)intent.getStringExtra(LoginActivity.USERNAME);
		String action = intent.getStringExtra(LoginActivity.ACTION);
		
		/*DEBUG*/System.out.println("u: "+username);
		if (action.equals("load"))
			RestorePicture();
		/*DEBUG*/System.out.println("a: "+action);
		///*DEBUG*/drawing.setBackgroundColor(0xFF00FF00 );
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_paint, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case R.id.menu_tools:
			showToolsDialog();
			return true;
		case R.id.menu_menu:
			showMenuDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showToolsDialog() {
		new ToolSettingsDialog(this, drawing.getToolBox());
	}

	private void showMenuDialog() {
		current = new Dialog(this);
		current.setContentView(R.layout.dialog_menu);
		current.setTitle("Menu");
		current.setCanceledOnTouchOutside(true);
		
		Button buttonErase = (Button) current.findViewById(R.id.buttonErase);
		Button buttonRestore = (Button) current.findViewById(R.id.buttonRestore);
		Button buttonSave = (Button) current.findViewById(R.id.buttonSave);
		
		buttonErase.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				drawing.erase();
				drawing.invalidate();
				current.dismiss();
			}
		});
		
		buttonSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*DEBUG*/System.out.println("buttonSave.onClickListener "+username);
				SavePicture();
				current.dismiss();
			}
		});
		
		buttonRestore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RestorePicture();
				current.dismiss();
			}
		});
		
		current.show();
	}
	
	private void SavePicture() {
		post = new PostPicture(drawing);
		post.execute(username);
	}
	
	private void RestorePicture() {
		get = new GetPicture(drawing);
		get.execute(username);
	}
}
