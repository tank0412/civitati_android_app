package com.app.civitati;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        APIClient.context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences mySharedPreferences = getSharedPreferences("CIVITATI_PREFERENCES", Context.MODE_PRIVATE);
        if(mySharedPreferences.contains("CIVITATI_PREFERENCES")) {
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }
        else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1: {
                SharedPreferences mySharedPreferences = getSharedPreferences("CIVITATI_PREFERENCES", Context.MODE_PRIVATE);
                if(mySharedPreferences.contains("CIVITATI_PREFERENCES")) {
                    mySharedPreferences.edit().clear().commit();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent); //restarting an activity
                    Toast.makeText(this, getString(R.string.report_logout), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, getString(R.string.report_logout_fail), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
