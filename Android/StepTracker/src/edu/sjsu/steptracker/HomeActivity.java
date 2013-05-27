package edu.sjsu.steptracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import edu.sjsu.steptracker.adapters.MainMenuAdapter;
import edu.sjsu.steptracker.db.StepTrackerDBHelper;

public class HomeActivity extends Activity implements OnItemClickListener {

	//private final static String TAG = "StepTracker::HomeActivity";

	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		listView = (ListView) findViewById(R.id.main_menu_list);
		listView.setAdapter(new MainMenuAdapter(getApplicationContext()));
		listView.setOnItemClickListener(this);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_home, menu);
//		return true;
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent intent;
		switch (position) {
		case 0:
			startActivity(new Intent(getApplicationContext(),
					RecordActivity.class));
			break;
		case 1:
			int count = new StepTrackerDBHelper(getApplicationContext(), "steptracker", null, 2).getWorkoutCount();
			if(count > 0){
				intent = new Intent(getApplicationContext(), WorkoutListActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), getString(R.string.no_workouts_found), Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}

	}

}
