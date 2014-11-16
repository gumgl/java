package ca.qc.johnabbott.cs603.asg4;

import ca.qc.johnabbott.cs603.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	public static final String USERNAME = "ca.qc.johnabbott.asg4.USERNAME";
	public static final String ACTION = "ca.qc.johnabbott.asg4.MESSAGE";

	private EditText editUsername;
	private Dialog current;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*DEBUG*/System.out.println("LoginActivity onCreate!");
		setContentView(R.layout.activity_login);
		editUsername = (EditText)this.findViewById(R.id.edit_username);
		
	}
	
	public void buttonNew(View v){
		/*DEBUG*/System.out.println("LoginActivity buttonNew!");
		Intent intent = new Intent (LoginActivity.this, PaintActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_username);
		String message = editText.getText().toString();
		intent.putExtra(USERNAME, message);

		/*DEBUG*/System.out.println("LoginActivity buttonNew put extra!");
		intent.putExtra(ACTION, "new");
		startActivity(intent);
	}
	public void buttonLoad(View v){
		/*DEBUG*/System.out.println("LoginActivity buttonLoad!");
		Intent intent = new Intent (this, PaintActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_username);
		String message = editText.getText().toString();
		intent.putExtra(USERNAME, message);
		intent.putExtra(ACTION, "load");
		startActivity(intent);
	}
}
