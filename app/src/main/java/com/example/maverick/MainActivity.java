package com.example.maverick;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView side_bar;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.main);
        side_bar = findViewById(R.id.side_bar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        side_bar.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.kanban_board) {
            KanbanBoardFragment kanbanBoardFragment = new KanbanBoardFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, kanbanBoardFragment).commit();
        }
        else if(id == R.id.pomodoro) {
            PomodoroFragment pomodoroFragment = new PomodoroFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, pomodoroFragment).commit();
            side_bar.setCheckedItem(id);
        }


        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}