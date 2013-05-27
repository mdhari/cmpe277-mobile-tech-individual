package edu.sjsu.steptracker.adapters;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import edu.sjsu.steptracker.R;
import edu.sjsu.steptracker.models.MenuItem;

public class MainMenuAdapter extends BaseAdapter implements ListAdapter {

	private List<MenuItem> menuItems = new LinkedList<MenuItem>();

	private LayoutInflater mInflater;

	public MainMenuAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		menuItems.add(new MenuItem(R.drawable.record, context
				.getString(R.string.record_txt)));
		menuItems.add(new MenuItem(R.drawable.calendar, context
				.getString(R.string.view_cal_txt)));
//		menuItems.add(new MenuItem(R.drawable.calendar, context
//				.getString(R.string.view_map_txt)));
	}

	static class ViewHolder {
		ImageView icon;
		TextView title;
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
			convertView = mInflater.inflate(R.layout.main_menu_list_item, null);

			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.menu_item_img);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.menu_item_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		MenuItem menuItem = (MenuItem) getItem(position);
		viewHolder.icon.setImageResource(menuItem.getImgId());
		viewHolder.title.setText(menuItem.getItemName());

		return convertView;
	}

}
