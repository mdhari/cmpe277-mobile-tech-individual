package edu.sjsu.steptracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ResultsActivity extends Activity {
	public static final String TAG = "StepTracker::ResultsActivity";
	public static final String STEPS = "STEPS";
	public static final String CALS = "CALS";
	public static final String TIME = "TIME";
	public static final String START_TIME = "START_TIME";
	public static final String END_TIME = "END_TIME";

	private String steps;
	private String calories;
	private String time;
	private long startTime;
//	private long endTime;
//
//	private String createDescString() {
//		String str = "";
//		str += "Steps: " + steps + "\n";
//		str += "Calories: " + calories + "\n";
//		str += "Time: " + time;
//		return str;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		steps = String.valueOf(getIntent().getExtras().getInt(STEPS));
		calories = String.valueOf(getIntent().getExtras().getInt(CALS));
		time = getIntent().getExtras().getString(TIME);
		startTime = getIntent().getExtras().getLong(START_TIME);

		((TextView) findViewById(R.id.steps_result_txt)).setText(steps);
		((TextView) findViewById(R.id.calories_result_txt)).setText(calories);
		((TextView) findViewById(R.id.time_result_txt)).setText(time);

		findViewById(R.id.map_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),MapActivity.class)
						.putExtra(START_TIME, startTime);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.home_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_results, menu);
//		return true;
//	}

}
