package com.andrew.examsapp.eerydictionary.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrew.examsapp.eerydictionary.R;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private String[] navItems;
	private int[] navIcons = {
			0,
			R.drawable.ic_action_home,
			R.drawable.ic_action_new,
			R.drawable.ic_action_help,
			R.drawable.ic_action_settings};

	public NavDrawerListAdapter(Context context, String[] navItems) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.navItems = navItems;
	}

	@Override
	public int getCount() {
		return navItems.length;
	}

	@Override
	public Object getItem(int position) {
		return navItems[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	static class ViewHolder {
		TextView navUserText;
		ImageView navIcon;
		TextView navText;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			if (position == 0) {
				convertView = inflater.inflate(R.layout.drawer_user_item, parent, false);
			} else {
				convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
			}
			holder = new ViewHolder();
			holder.navUserText = (TextView) convertView.findViewById(R.id.navUserText);
			holder.navIcon = (ImageView) convertView.findViewById(R.id.navIcon);
			holder.navText = (TextView) convertView.findViewById(R.id.navText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			holder.navUserText.setText(navItems[position]);
		} else {
			holder.navText.setText(navItems[position]);
			holder.navIcon.setImageResource(navIcons[position]);
		}
		return convertView;
	}
}
