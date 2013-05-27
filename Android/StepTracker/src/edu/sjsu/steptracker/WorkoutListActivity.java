package edu.sjsu.steptracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import edu.sjsu.steptracker.adapters.WorkoutListAdapter;
import edu.sjsu.steptracker.db.StepTrackerDBHelper;
import edu.sjsu.steptracker.db.StepTrackerDBHelper.WorkoutSummary;

public class WorkoutListActivity extends Activity implements OnItemClickListener, OnItemLongClickListener{

	

	ListView listView;
	WorkoutListAdapter workoutListAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout_list);
		listView = (ListView) findViewById(R.id.workout_list);
		StepTrackerDBHelper stepTrackerDBHelper = new StepTrackerDBHelper(getApplicationContext(), "steptracker", null, 2);
		workoutListAdapter = new WorkoutListAdapter(getApplicationContext(),stepTrackerDBHelper.getWorkoutSummary());
		listView.setAdapter(workoutListAdapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.workout_list, menu);
		return true;
	}
	
	private void confirmRemoveAll(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.confirmation_title))
				.setMessage(getString(R.string.confirm_all_deletion))
				.setNegativeButton(getString(R.string.no_txt), null)
				.setPositiveButton(getString(R.string.yes_txt), new OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						StepTrackerDBHelper stepTrackerDBHelper = new StepTrackerDBHelper(getApplicationContext(), "steptracker", null, 2);
						stepTrackerDBHelper.deleteAllRows();
						workoutListAdapter.setMenuItems(stepTrackerDBHelper.getWorkoutSummary());
						workoutListAdapter.notifyDataSetChanged();
					}
					
				}).setCancelable(true).create()
				.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.remove_all:
	            confirmRemoveAll();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		WorkoutSummary workoutSummary = (WorkoutSummary)workoutListAdapter.getItem(position);
		Intent intent = new Intent(getApplicationContext(),
				ResultsActivity.class);
		intent.putExtra(ResultsActivity.STEPS, workoutSummary.getSteps());
		intent.putExtra(ResultsActivity.CALS, workoutSummary.getCalories());
		intent.putExtra(ResultsActivity.TIME,
				workoutSummary.getTime_elapsed());
		intent.putExtra(ResultsActivity.START_TIME,
				workoutSummary.getDatetime().getTimeInMillis());
		startActivity(intent);
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.confirmation_title))
				.setMessage(getString(R.string.confirm_workout_deletion))
				.setNegativeButton(getString(R.string.no_txt), null)
				.setPositiveButton(getString(R.string.yes_txt), new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						StepTrackerDBHelper stepTrackerDBHelper = new StepTrackerDBHelper(getApplicationContext(), "steptracker", null, 2);
						WorkoutSummary workoutSummary = (WorkoutSummary)workoutListAdapter.getItem(position);
						stepTrackerDBHelper.deleteRowsWithDate(workoutSummary.getDatetime());
						workoutListAdapter.setMenuItems(stepTrackerDBHelper.getWorkoutSummary());
						workoutListAdapter.notifyDataSetChanged();
					}
					
				}).setCancelable(true).create()
				.show();
		return false;
	}

}
