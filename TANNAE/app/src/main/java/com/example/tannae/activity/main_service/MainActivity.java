package com.example.tannae.activity.main_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.user_service.UserServiceListActivity;
import com.example.tannae.network.Network;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

// << Main Activity >>
public class MainActivity extends AppCompatActivity {
    private FloatingActionButton reqBtn;
    private long backKeyPressedTime = 0;
    private Toolbar toolbar;
    private ActionMenuItemView drive;
    private BottomAppBar bottomAppBar;
    private MapView mapView;
    private ViewGroup mapViewContainer;

    // < onCreate >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting
        setViews();
        setEventListeners();
        drive.setVisibility(InnerDB.sp.getInt("drive", 0) == 1 ? View.VISIBLE : View.INVISIBLE);

        // Connect Socket.io
        if (!Network.socket.isActive())
            Network.socket.connect();

        // Set Map
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view_main);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.566406178655534, 126.97786868931414), true);
        mapViewContainer.addView(mapView);
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    } */

    // < Register views >
    private void setViews() {
        reqBtn = findViewById(R.id.req_button_main);
        toolbar = findViewById(R.id.topAppBar_main);
        bottomAppBar = findViewById(R.id.bottomAppBar_main);
        drive = findViewById(R.id.item_drive_menu);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_app_bar, menu);

        MenuInflater menuInflatera = getMenuInflater();
        menuInflatera.inflate(R.menu.bottom_app_bar, menu);

        return true;
    }


    // < Register event listeners >
    private void setEventListeners() {
        // Request service
        reqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InnerDB.sp.getInt("state", 0) == 1) {
                    mapViewContainer.removeView(mapView);

                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.putExtra("type", false);
                    startActivity(intent);
                } else {
                    mapViewContainer.removeView(mapView);

                    Intent intent = new Intent(getApplicationContext(), ServiceReqActivity.class);
                    startActivity(intent);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapViewContainer.removeView(mapView);
                Intent intent = new Intent(getApplicationContext(), UserServiceListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        // Service On
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mapViewContainer.removeView(mapView);
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                intent.putExtra("type", true);
                startActivity(intent);
                return true;
            }
        });
    }

    // < BackPress >
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toaster.show(getApplicationContext(), "종료하려면 한번 더 누르세요.");
            //Toast.makeText(this, "종료하려면 한번 더 누르세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}