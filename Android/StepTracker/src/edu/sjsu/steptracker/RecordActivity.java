package edu.sjsu.steptracker;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import edu.sjsu.steptracker.db.StepTrackerDBHelper;
import edu.sjsu.steptracker.db.StepTrackerDBHelper.WorkoutReading;
import edu.sjsu.steptracker.db.StepTrackerDBHelper.WorkoutSummary;

public class RecordActivity extends Activity implements OnClickListener,
		SensorEventListener {


	private static final String TAG = "StepTracker::RecordActivity";

	private TextView stepsTxt;
	private TextView caloriesTxt;
	private TextView timeTxt;
	private Button startStopBtn;
	private Button nextBtn;
	private boolean isRunning;

	private Calendar startTime;
	private Calendar endTime;
	private boolean isCalStartSet = false;

	private float threshold = 0.10f;
	private float lastReading = 0.0f;

	private Timer timer;

	int seconds = 0, minutes = 0, hours = 0, steps = 0, calories = 0;
	private final float CALORIES_PER_STEP = 0.05f;
	private SensorManager mSensorManager;
	private Sensor mRotationalSensor;

	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private StepTrackerDBHelper stepTrackerDBHelper;

	private String login;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		SharedPreferences settings = getSharedPreferences("steptracker",Context.MODE_PRIVATE);
		login = settings.getString("username", "");

		stepsTxt = (TextView) findViewById(R.id.stepsTxt);
		caloriesTxt = (TextView) findViewById(R.id.caloriesTxt);
		timeTxt = (TextView) findViewById(R.id.timeTxt);
		startStopBtn = (Button) findViewById(R.id.start_stop_btn);
		nextBtn = (Button) findViewById(R.id.next_btn);

		isRunning = false;
		startStopBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		findViewById(R.id.reset_btn).setOnClickListener(this);

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				makeUseOfNewLocation(location);
			}

			private void makeUseOfNewLocation(Location location) {
//				Log.d(TAG,
//						"Loc: " + location.getLatitude()
//								+ location.getLongitude() );
				SQLiteDatabase db = stepTrackerDBHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(WorkoutReading.COLUMN_DATETIME, startTime.getTimeInMillis());
				values.put(WorkoutReading.COLUMN_LAT, location.getLatitude());
				values.put(WorkoutReading.COLUMN_LONG, location.getLongitude());
				values.put(WorkoutReading.COLUMN_USERNAME, login);
				db.insert(WorkoutReading.TABLE_NAME, WorkoutReading.COLUMN_DATETIME, values);

			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		// 1000, 1.0f, locationListener);
		stepTrackerDBHelper = new StepTrackerDBHelper(getApplicationContext(), "steptracker", null, 2);

	}
	
	/**
	 * just need to clear the reading db
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((KeyEvent.KEYCODE_BACK == keyCode) && (startTime != null)){
			stepTrackerDBHelper.deleteRowsWithDate(startTime);
		}
		return super.onKeyDown(keyCode, event);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_record, menu);
//		return true;
//	}

	private void resetMetrics() {
		seconds = 0;
		minutes = 0;
		hours = 0;
		steps = 0;
		calories = 0;
		updateMetrics();
	}

	private void updateMetrics() {

		calories = (int) (steps * CALORIES_PER_STEP);
		stepsTxt.setText(String.format("%03d", steps));
		caloriesTxt.setText(String.valueOf(calories));
		timeTxt.setText(String
				.format("%02d:%02d:%02d", hours, minutes, seconds));
	}

	/**
	 * updates every second
	 */
	private void updateTimer() {
		seconds++;
		if (seconds == 60) {
			seconds = 0;
			minutes++;
		}

		if (minutes == 60) {
			minutes = 0;
			hours++;
		}

		updateMetrics();
	}

	/**
	 * Our Handler used to execute operations on the main thread. This is used
	 * to schedule increments of our value.
	 */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			// It is time to bump the value!
			case 0:
				updateTimer();
				break;

			}
		}
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.start_stop_btn:
			if (isRunning) {
				isRunning = false;
				endTime = Calendar.getInstance();
				nextBtn.setEnabled(true);
				startStopBtn.setText(getString(R.string.start));
				timer.cancel();
				mSensorManager.unregisterListener(this);
				locationManager.removeUpdates(locationListener);
			} else {
				isRunning = true;
				if (!isCalStartSet) {
					startTime = Calendar.getInstance();
					isCalStartSet = true;
				}
				
				getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // easy
																			// way
																			// to
																			// keep
																			// screen
																			// and
																			// CPU
																			// running
																			// instead
																			// of
																			// wake
																			// locks
				mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
				mRotationalSensor = mSensorManager
						.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
				mSensorManager.registerListener(this, mRotationalSensor,
						SensorManager.SENSOR_DELAY_NORMAL);
				nextBtn.setEnabled(false);
				startStopBtn.setText(getString(R.string.stop));
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0,
						locationListener);
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0,
						locationListener);
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {

						Message msg = new Message();
						msg.what = 0;
						mHandler.sendMessageAtFrontOfQueue(msg);
					}

				}, 1000, 1000);
				
			}
			break;
		case R.id.next_btn:
			SQLiteDatabase db = stepTrackerDBHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(WorkoutSummary.COLUMN_DATETIME, startTime.getTimeInMillis());
			values.put(WorkoutSummary.COLUMN_STEPS, steps);
			values.put(WorkoutSummary.COLUMN_CALORIES, calories);
			values.put(WorkoutSummary.COLUMN_TOTAL_TIME, String.format("%02d:%02d:%02d", hours, minutes, seconds));
			values.put(WorkoutSummary.COLUMN_USERNAME, login);
			db.insert(WorkoutSummary.TABLE_NAME, WorkoutSummary.COLUMN_DATETIME, values);
			Intent intent = new Intent(getApplicationContext(),
					ResultsActivity.class);
			intent.putExtra(ResultsActivity.STEPS, steps);
			intent.putExtra(ResultsActivity.CALS, calories);
			intent.putExtra(ResultsActivity.TIME,
					String.format("%02d:%02d:%02d", hours, minutes, seconds));
			intent.putExtra(ResultsActivity.START_TIME,
					startTime.getTimeInMillis());
			intent.putExtra(ResultsActivity.END_TIME, endTime.getTimeInMillis());
			startActivity(intent);
			finish(); // i don't want this activity to be changed once done is hit!
			break;
		case R.id.reset_btn:
			resetMetrics();
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
//		 Log.d(TAG,"Event: " + event.values[1] + " lastReading: " + lastReading);

//		if (stepForward && Math.abs(event.values[1]) > upperRange) {
//			steps++;
//			stepForward = false;
//		}
//
//		if (!stepForward && Math.abs(event.values[1]) < lowerRange) {
//			steps++;
//			stepForward = true;
//		}
		 float absVal = Math.abs(event.values[1]);
		 if((Math.abs((absVal-lastReading)) > threshold)){
			 lastReading = absVal;
			 steps++;
		 }

		updateMetrics();
	}

	/**
	 * its not necessary to detach the listener the user can for example play
	 * music while they are walking
	 */
	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		// mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		if(locationManager != null)
			locationManager.removeUpdates(locationListener);
		if(mSensorManager != null)
			mSensorManager.unregisterListener(this);
		if(timer != null)
			timer.cancel();
		super.onDestroy();
	}
}
