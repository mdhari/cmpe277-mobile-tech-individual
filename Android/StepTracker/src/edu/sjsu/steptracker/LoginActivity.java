package edu.sjsu.steptracker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import edu.sjsu.steptracker.utils.Utils;

public class LoginActivity extends Activity {

	//private static final String TAG = "StepTracker::LoginActivity";

	private AutoCompleteTextView username;

	// private EditText password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// password = (EditText)findViewById(R.id.password);

		findViewById(R.id.login_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (validateUsernameAndPassword(username.getText().toString())) {// ,password.getText().toString())){
					SharedPreferences settings = getSharedPreferences("steptracker",Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("username", username.getText().toString());
					editor.commit();
					startActivity(new Intent(getApplicationContext(),
							HomeActivity.class));
				}

			}

		});

		AccountManager am = AccountManager.get(this); // "this" references the
														// current Context

		Account[] accounts = am.getAccounts();

		String[] strArrOfAcct = new String[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			strArrOfAcct[i] = accounts[i].name;
		}

		// Get a reference to the AutoCompleteTextView in the layout
		username = (AutoCompleteTextView) findViewById(R.id.username);

		// Restore preferences
		SharedPreferences settings = getSharedPreferences("steptracker",Context.MODE_PRIVATE);
		String lastLogin = settings.getString("username", "");

		username.setText(lastLogin);

		// Create the adapter and set it to the AutoCompleteTextView
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, strArrOfAcct);
		username.setAdapter(adapter);

	}

	private boolean validateUsernameAndPassword(String username) {// , String
																	// password){

		if (!Utils.isEmpty(username)) // && !Utils.isEmpty(password))
			return true;
		else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.login_fail))
					.setMessage(getString(R.string.invalid_credentials))
					.setNeutralButton("OK", null).setCancelable(true).create()
					.show();
			return false;
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_login, menu);
//		return true;
//	}

}
