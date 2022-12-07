package com.termproject.moblieprogramming.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.termproject.moblieprogramming.Fragment.AlarmFragment;
import com.termproject.moblieprogramming.Fragment.ChatFragment;
import com.termproject.moblieprogramming.Fragment.FriendFragment;
import com.termproject.moblieprogramming.Fragment.ProjectFragment;
import com.termproject.moblieprogramming.R;

public class MainActivity extends AppCompatActivity {

    ProjectFragment projectfragment;
    FriendFragment friendfragment;
    ChatFragment chatfragment;
    AlarmFragment alarmfragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectfragment = new ProjectFragment();
        friendfragment = new FriendFragment();
        chatfragment = new ChatFragment();
        alarmfragment = new AlarmFragment();


        getSupportFragmentManager().beginTransaction().replace(R.id.frgment_container, projectfragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.menu_bottom);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.project:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frgment_container, projectfragment).commit();
                        return true;
                    case R.id.friend:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frgment_container, friendfragment).commit();
                        return true;
                    case R.id.chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frgment_container, chatfragment).commit();
                        return true;
                    case R.id.alarm:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frgment_container, alarmfragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}