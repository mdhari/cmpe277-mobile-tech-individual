package edu.sjsu.steptracker;

import java.util.Calendar;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.sjsu.steptracker.db.StepTrackerDBHelper;
import edu.sjsu.steptracker.db.StepTrackerDBHelper.WorkoutReading;

public class MapActivity extends FragmentActivity {

	private GoogleMap mMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		android.support.v4.app.FragmentManager myFragmentManager = getSupportFragmentManager();
		SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager
				.findFragmentById(R.id.map);
		
		Calendar cal = Calendar.getInstance();
		//Log.d("TAG", "" + getIntent().getExtras().getLong(ResultsActivity.START_TIME));
		cal.setTimeInMillis(getIntent().getExtras().getLong(ResultsActivity.START_TIME));
		
		StepTrackerDBHelper stepTrackerDBHelper = new StepTrackerDBHelper(getApplicationContext(), "steptracker", null, 2);
		List<WorkoutReading> listOfWorkoutMapData = stepTrackerDBHelper.getAllWorkoutsWithDate(cal);
		
		mMap = mySupportMapFragment.getMap();

		if (mMap != null) {
			//mMap.setMyLocationEnabled(true);
			// Instantiates a new Polyline object and adds points to define a
			// rectangle
			//Log.d("TAGGAGAGAGAGAGAGG", "" + listOfWorkoutMapData.size());
			PolylineOptions rectOptions = new PolylineOptions();
			rectOptions.color(Color.BLUE);
			for(int i = 0; i < listOfWorkoutMapData.size();i++){
				//Log.d("TAG", "" + workoutReading.getLatitude());
				if(i == 0){
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(listOfWorkoutMapData.get(i).getLatitude(), listOfWorkoutMapData.get(i).getLongitude()), 18));
					mMap.addMarker(new MarkerOptions().position(new LatLng(listOfWorkoutMapData.get(i).getLatitude(), listOfWorkoutMapData.get(i).getLongitude()))
							.title("Start")
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
				}

				if(i == (listOfWorkoutMapData.size()-1))
					mMap.addMarker(new MarkerOptions().position(new LatLng(listOfWorkoutMapData.get(i).getLatitude(), listOfWorkoutMapData.get(i).getLongitude()))
							.title("End")
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
					
				rectOptions.add(new LatLng(listOfWorkoutMapData.get(i).getLatitude(), listOfWorkoutMapData.get(i).getLongitude()));
			}
			
//			PolylineOptions rectOptions = new PolylineOptions()
//					.add(new LatLng(37.35, -122.0))
//					.add(new LatLng(37.45, -122.0)) // North of the previous
//													// point, but at the same
//													// longitude
//					.add(new LatLng(37.45, -122.2)) // Same latitude, and 30km
//													// to the west
//					.add(new LatLng(37.35, -122.2)) // Same longitude, and 16km
//													// to the south
//					.add(new LatLng(37.35, -122.0)); // Closes the polyline.

			// Get back the mutable Polyline
			mMap.addPolyline(rectOptions);
			
		}
		
		

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_map, menu);
//		return true;
//	}

}
