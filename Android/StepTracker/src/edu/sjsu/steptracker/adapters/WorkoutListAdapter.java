package edu.sjsu.steptracker.adapters;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import edu.sjsu.steptracker.R;
import edu.sjsu.steptracker.db.StepTrackerDBHelper.WorkoutSummary;

public class WorkoutListAdapter extends BaseAdapter implements ListAdapter {

	private List<WorkoutSummary> menuItems = new LinkedList<WorkoutSummary>();

	public void setMenuItems(List<WorkoutSummary> menuItems) {
		this.menuItems = menuItems;
	}

	private LayoutInflater mInflater;
	private Context context;

	public WorkoutListAdapter(Context context,List<WorkoutSummary> workoutSummary) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		menuItems = workoutSummary; 
	}

	static class ViewHolder {
		TextView date;
		TextView steps;
		TextView cals;
	}

	@Override
	public int getCount() {
		return menuItems.size();
	}

	@Override
	public Object getItem(int position) {
		return menuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = new ViewHolder();

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.workout_list_item, null);
			viewHolder.date = (TextView)convertView.findViewById(R.id.workout_item_date);
			viewHolder.steps = (TextView)convertView.findViewById(R.id.workout_item_steps);
			viewHolder.cals = (TextView)convertView.findViewById(R.id.workout_item_cals);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		WorkoutSummary menuItem = (WorkoutSummary) getItem(position);
		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		viewHolder.date.setText(df.format(menuItem.getDatetime().getTime()));
		viewHolder.steps.setText(context.getString(R.string.steps_taken) + String.valueOf(menuItem.getSteps()));
		viewHolder.cals.setText(context.getString(R.string.calories_burned) + String.valueOf(menuItem.getCalories()));

		return convertView;
	}

}
