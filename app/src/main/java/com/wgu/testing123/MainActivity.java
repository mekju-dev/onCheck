package com.wgu.testing123;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    dbHelper db = new dbHelper(this);
    FragmentManager fragmentManager;
    private Toolbar toolbar;
    String username;
    TextView usernameTextView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //db.clearData();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodaysDriversFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            navigationView.setCheckedItem(R.id.nav_home);

            if (item.getItemId() == R.id.driversOut) {
                replaceFragment(new TodaysDriversFragment());
                navigationView.setCheckedItem(R.id.driversOut);
            } else {
                replaceFragment(new TodaysOutstandingFragment());
                navigationView.setCheckedItem(R.id.driversIn);
            }
            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {loadSelectDriverPage();
            }
        });

        fragmentManager = getSupportFragmentManager();
        replaceFragment(new TodaysDriversFragment());

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        //usernameTextView = findViewById(R.id.usernameNavText);
        usernameTextView = navigationView.getHeaderView(0).findViewById(R.id.usernameNavText);
        usernameTextView.setText("User: " + username);

    }


    /**
     * @param item The selected item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        if (item.getItemId() == R.id.nav_home){
           replaceFragment(new TodaysDriversFragment());
        }
        else if (item.getItemId() == R.id.nav_database) {
            replaceFragment(new databaseFragment());
        }
        else if (item.getItemId() == R.id.nav_report){
            Intent intent = new Intent(MainActivity.this, TimesReport.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_logout){
            db.updateAuth(username, 0);
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void loadSelectDriverPage() {

                Intent intent = new Intent(MainActivity.this, selectDrivers.class);
                startActivity(intent);
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}