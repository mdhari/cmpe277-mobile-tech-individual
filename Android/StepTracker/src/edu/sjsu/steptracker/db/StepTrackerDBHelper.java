package edu.sjsu.steptracker.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class StepTrackerDBHelper extends SQLiteOpenHelper {

	//private static final int DATABASE_VERSION = 1;
	private String login = "";
	
	public class WorkoutSummary {
		public static final String TABLE_NAME = "WORKOUT_SUMMARY";
		public static final String _ID = "_ID";
		public static final String COLUMN_DATETIME = "WORKOUT_DATETIME";
		public static final String COLUMN_STEPS = "STEPS";
		public static final String COLUMN_CALORIES = "CALORIES";
		public static final String COLUMN_TOTAL_TIME = "TIME_ELAPSED";
		public static final String COLUMN_USERNAME = "USERNAME";
		
		private int id;
		private Calendar datetime;
		private int steps;
		private int calories;
		private String time_elapsed;
		private String username;
		
		public int getSteps() {
			return steps;
		}
		public void setSteps(int steps) {
			this.steps = steps;
		}
		public int getCalories() {
			return calories;
		}
		public void setCalories(int calories) {
			this.calories = calories;
		}
		public String getTime_elapsed() {
			return time_elapsed;
		}
		public void setTime_elapsed(String time_elapsed) {
			this.time_elapsed = time_elapsed;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public Calendar getDatetime() {
			return datetime;
		}
		public void setDatetime(Calendar datetime) {
			this.datetime = datetime;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		
	}

	public class WorkoutReading {
		public static final String TABLE_NAME = "WORKOUT_READING";
		public static final String _ID = "_ID";
		public static final String COLUMN_DATETIME = "WORKOUT_DATETIME";
		public static final String COLUMN_LAT = "LATITUDE";
		public static final String COLUMN_LONG = "LONGITUDE";
		public static final String COLUMN_USERNAME = "USERNAME";
		
		private int id;
		private Calendar datetime;
		private float latitude;
		private float longitude;
		private String username;

		
		
		public Calendar getDatetime() {
			return datetime;
		}
		public void setDatetime(Calendar datetime) {
			this.datetime = datetime;
		}
		public float getLatitude() {
			return latitude;
		}
		public void setLatitude(float latitude) {
			this.latitude = latitude;
		}
		public float getLongitude() {
			return longitude;
		}
		public void setLongitude(float longitude) {
			this.longitude = longitude;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}

	}

	public StepTrackerDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		
		super(context, name, factory, version);
		SharedPreferences settings = context.getSharedPreferences("steptracker",Context.MODE_PRIVATE);
		login = settings.getString("username", "");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + WorkoutReading.TABLE_NAME + " ("
				+ WorkoutReading._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ WorkoutReading.COLUMN_DATETIME + " INTEGER,"
				+ WorkoutReading.COLUMN_LAT + " REAL,"
				+ WorkoutReading.COLUMN_LONG + " REAL,"
				+ WorkoutReading.COLUMN_USERNAME + " TEXT" + ");");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " + WorkoutSummary.TABLE_NAME + " ("
				+ WorkoutSummary._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ WorkoutSummary.COLUMN_DATETIME + " INTEGER,"
				+ WorkoutSummary.COLUMN_STEPS + " INTEGER,"
				+ WorkoutSummary.COLUMN_CALORIES + " INTEGER,"
				+ WorkoutSummary.COLUMN_TOTAL_TIME + " TEXT,"
				+ WorkoutSummary.COLUMN_USERNAME + " TEXT" + ");");

	}

	/**
	 * delete rows with a date specified in milliseconds
	 * 
	 * @param date
	 */
	public void deleteAllRows() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(WorkoutReading.TABLE_NAME, WorkoutReading.COLUMN_USERNAME + "=?" ,
				new String[] { login });
		db.delete(WorkoutSummary.TABLE_NAME, WorkoutSummary.COLUMN_USERNAME + "=?" ,
				new String[] { login });
		db.close();
	}
	
	/**
	 * delete rows with a date specified in milliseconds
	 * 
	 * @param date
	 */
	public void deleteRowsWithDate(Calendar date) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(WorkoutReading.TABLE_NAME, WorkoutReading.COLUMN_DATETIME
				+ " = ? AND " + WorkoutReading.COLUMN_USERNAME + "=?" ,
				new String[] { String.valueOf(date.getTimeInMillis()),login });
		db.delete(WorkoutSummary.TABLE_NAME, WorkoutSummary.COLUMN_DATETIME
				+ " = ? AND " + WorkoutSummary.COLUMN_USERNAME + "=?" ,
				new String[] { String.valueOf(date.getTimeInMillis()),login });
		db.close();
	}
	
	/**
	 * get all workouts by date
	 * @return
	 */
    public List<WorkoutSummary> getWorkoutSummary() {
        List<WorkoutSummary> workoutList = new ArrayList<WorkoutSummary>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + WorkoutSummary.TABLE_NAME + " WHERE " + WorkoutSummary.COLUMN_USERNAME + "=\'" + login +"\'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        
        if (cursor.moveToFirst()) {
            do {
            	WorkoutSummary workout = new WorkoutSummary();
                workout.setId(cursor.getInt(0));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(cursor.getLong(1));
                workout.setDatetime(cal);
                workout.setSteps(cursor.getInt(2));
                workout.setCalories(cursor.getInt(3));
                workout.setTime_elapsed(cursor.getString(4));
                workout.setUsername(cursor.getString(5));
                workoutList.add(workout);
            } while (cursor.moveToNext());
        }
        
        db.close();
 
        // return workout list
        return workoutList;
    }
    
    /**
	 * get workout count
	 * @return
	 */
    public int getWorkoutCount() {
        //List<WorkoutReading> workoutList = new ArrayList<WorkoutReading>();
        // Select All Query
        String selectQuery = "SELECT COUNT(*) FROM " + WorkoutSummary.TABLE_NAME + " WHERE " + WorkoutSummary.COLUMN_USERNAME + "=\'" + login +"\'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        int count = 0;
        if (cursor.moveToFirst()) {

                count = cursor.getInt(0);

            } 
        
        db.close();
 
        // return workout list
        return count;
    }
	
	/**
	 * get all workouts by date
	 * @return
	 */
//    public WorkoutSummary getWorkoutSummaryWithDate(Calendar date) {
//        //List<WorkoutReading> workoutList = new ArrayList<WorkoutReading>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + WorkoutSummary.TABLE_NAME + " WHERE " + WorkoutSummary.COLUMN_DATETIME + " = ?";
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(date.getTimeInMillis())});
// 
//        WorkoutSummary workout = new WorkoutSummary();
//        if (cursor.moveToFirst()) {
//            do {
//                workout.setId(cursor.getInt(0));
//                Calendar cal = Calendar.getInstance();
//                cal.setTimeInMillis(cursor.getLong(1));
//                workout.setDatetime(cal);
//                workout.setSteps(cursor.getInt(2));
//                workout.setCalories(cursor.getInt(3));
//                workout.setTime_elapsed(cursor.getString(4));
//
//            } while (cursor.moveToNext());
//        }
//        
//        db.close();
// 
//        // return workout list
//        return workout;
//    }
	
	/**
	 * get all workouts by date
	 * @return
	 */
    public List<WorkoutReading> getAllWorkoutsWithDate(Calendar date) {
        List<WorkoutReading> workoutList = new ArrayList<WorkoutReading>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + WorkoutReading.TABLE_NAME+ " WHERE " + WorkoutSummary.COLUMN_USERNAME + "=\'" + login + "\' AND " + WorkoutReading.COLUMN_DATETIME + "= ?";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(date.getTimeInMillis())});
      
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WorkoutReading workout = new WorkoutReading();
                workout.setId(cursor.getInt(0));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(cursor.getLong(1));
                workout.setDatetime(cal);
                workout.setLatitude(cursor.getFloat(2));
                workout.setLongitude(cursor.getFloat(3));
                // Add workout to list
                workoutList.add(workout);
            } while (cursor.moveToNext());
        }
 
        cursor.close();
        db.close();
        // return workout list
        return workoutList;
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
