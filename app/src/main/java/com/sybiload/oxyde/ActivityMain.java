package com.sybiload.oxyde;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ActivityMain extends AppCompatActivity
{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    private SharedPreferences mainPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPref = this.getSharedPreferences("main", 0);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if(toolbar != null)
            setSupportActionBar(toolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.txt_open, R.string.txt_close)
        {

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerToggle.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId())
                {
                    case R.id.item_scores:
                        tx.replace(R.id.main, Fragment.instantiate(ActivityMain.this, "com.sybiload.oxyde.FragmentScores"));
                        tx.commit();
                        break;

                    case R.id.item_installation:
                        break;

                    case R.id.item_about:
                        tx.replace(R.id.main, Fragment.instantiate(ActivityMain.this, "com.sybiload.oxyde.FragmentAbout"));
                        tx.commit();
                        break;

                    case R.id.item_logout:
                        mainPref.edit().clear().commit();
                        Intent intent = new Intent(getApplication(), ActivityLogin.class);
                        startActivity(intent);
                        finish();
                        break;

                    default:
                        break;
                }

                drawerLayout.closeDrawers();

                return false;
            }
        });

        if (mainPref.getString("username", null) == null)
        {
            Intent intent = new Intent(getApplication(), ActivityLogin.class);
            startActivity(intent);
            finish();
        }
        else
        {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, Fragment.instantiate(ActivityMain.this, "com.sybiload.oxyde.FragmentScores"));
            tx.commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout != null)
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}