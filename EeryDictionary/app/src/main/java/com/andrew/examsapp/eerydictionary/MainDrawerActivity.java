package com.andrew.examsapp.eerydictionary;


import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andrew.examsapp.eerydictionary.fragments.AddNewWordFragment;
import com.andrew.examsapp.eerydictionary.fragments.HelpAndFeedbackFragment;
import com.andrew.examsapp.eerydictionary.fragments.MainFragment;
import com.andrew.examsapp.eerydictionary.fragments.SettingsFragment;
import com.andrew.examsapp.eerydictionary.fragments.UserFragment;
import com.andrew.examsapp.eerydictionary.model.NavDrawerListAdapter;


/*
*
* This Activity contains only the navigation drawer,
* it will call the right fragment based on the navigation item selected
*
* */

public class MainDrawerActivity extends ActionBarActivity {

	private static final String KEY_TITLE = "title";

	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle drawerToggle;
	private String[] navItems;
	private CharSequence drawerTitle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_drawer);


		drawerTitle = getTitle();
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawerListView = (ListView) findViewById(R.id.drawerList);

		navItems = getResources().getStringArray(R.array.nav_drawer_items);
		drawerListView.setAdapter(new NavDrawerListAdapter(this, navItems));
		drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectItem(position);
			}
		});
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(R.string.app_name);
				invalidateOptionsMenu();
				hideKeyboard(drawerListView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getSupportActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			selectItem(1);
		} else {
			drawerTitle = savedInstanceState.getCharSequence(KEY_TITLE);
			getSupportActionBar().setTitle(drawerTitle);
		}
	}


	public void selectItem(int position) {
		Fragment fragment = null;
		switch (position) {
			case 0:
				fragment = new UserFragment();
				break;
			case 1:
				fragment = new MainFragment();
				break;
			case 2:
				fragment = new AddNewWordFragment();
				break;
			case 3:
				fragment = new HelpAndFeedbackFragment();
				break;
			case 4:
				fragment = new SettingsFragment();
				break;
			default:
				break;
		}
		if (fragment != null) {
			setTitle(navItems[position]);
			drawerTitle = navItems[position];
			drawerListView.setItemChecked(position, true);
			drawerLayout.closeDrawer(drawerListView);
			invalidateOptionsMenu();
			//fragment transaction
			getFragmentManager().beginTransaction()
					.replace(R.id.frameLayout, fragment)
					.commit();
		} else {
			Log.e("ERROR_FRAGMENT", "Error in creating the fragment");
		}
	}

	public void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putCharSequence(KEY_TITLE, drawerTitle);
	}

	//close the nav drawer if opened on back pressed
	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(drawerListView)) {
			drawerLayout.closeDrawer(drawerListView);
		} else
			super.onBackPressed();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main_drawer, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//provides actions when selecting nav drawer icon and back icon in the action bar
		/*if (drawerToggle.onOptionsItemSelected(item))
			return true;
		return super.onOptionsItemSelected(item);*/
		return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean isDrawerOpened = drawerLayout.isDrawerOpen(drawerListView);
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setVisible(!isDrawerOpened);
		}
		return super.onPrepareOptionsMenu(menu);
	}
}