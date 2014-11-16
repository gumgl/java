package ca.qc.johnabbott.cs603.asg3;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private DrawingView drawing;
	private Dialog current;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawing = (DrawingView)this.findViewById(R.id.drawing_view);

		/*DEBUG*/System.out.println("MainActivity onCreate!");
		///*DEBUG*/drawing.setBackgroundColor(0xFF00FF00 );
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
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
				//TODO next assignment
				current.dismiss();
			}
		});
		
		buttonRestore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO next assignment
				current.dismiss();
			}
		});
		
		current.show();
	}
	
}
