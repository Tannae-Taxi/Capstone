package com.example.tannae.activity.main_service;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tannae.R;
import com.example.tannae.network.Network;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// << Navigation Activity >>
public class NavigationActivity extends AppCompatActivity {
    private Button btnEndService, btnPass;
    private Switch switchDrive;
    private TextView tvNext;
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private boolean type;
    private MapPolyline polyline;
    private MapPoint point;

    // < onCreate >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Setting
        setViews();
        setEventListeners();
        setNetworks();

        // If main -> navigation ? true : false
        type = getIntent().getBooleanExtra("type", false);
        btnPass.setBackgroundColor(Color.parseColor("#BDBDBD"));
        btnEndService.setBackgroundColor(Color.parseColor("#BDBDBD"));

        // Set Map
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view_navigation);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.566406178655534, 126.97786868931414), true);
        mapViewContainer.addView(mapView);
        try {
            if (!type) {
                btnPass.setVisibility(View.INVISIBLE);
                btnEndService.setVisibility(View.INVISIBLE);
                switchDrive.setVisibility(View.INVISIBLE);
                if (InnerDB.sp.getInt("state", 0) == 1) {
                    Toaster.show(getApplicationContext(),"서비스를 이용중입니다.");
                    JSONObject path = new JSONObject(InnerDB.sp.getString("path", ""));
                    showPathOnMap(path);
                } else
                    Network.socket.emit("requestService", new JSONObject(getIntent().getStringExtra("data")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // < onPause >
    @Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    }

    // < Set Socket.io >
    private void setNetworks() {
        // Response service
        Network.socket.on("responseService", args -> {
            // args[0] = flag:int / args[1] = path:JSONObject / args[2] = usn:String
            runOnUiThread(() -> {
                try {
                    System.out.println();
                    // Flag : -1 (Server Error) / 0 (No Vehicle) / 1 (Share vehicle) / 2 (Non-share vehicle to share user) / 3 (Non-share vehicle to non-share user)
                    // 4 (Passenger boarding) / 5 (Passenger get off)
                    int flag = (int) args[0];
                    JSONObject path = (JSONObject) args[1];
                    String usnOut = (String) args[2];
                    String usnIn = InnerDB.sp.getString("usn", "");
                    JSONArray waypoints = path != null ? path.getJSONArray("waypoints") : null;

                    // Toast
                    String message;
                    switch (flag) {
                        case 0:
                            message = "이용 가능한 차량이 없습니다.";
                            break;
                        case 1:
                            message = usnOut.equals(usnIn) ? "동승 차량이 배차되었습니다." : "추가 인원이 배차되었습니다.\n경로를 수정합니다..";
                            break;
                        case 2:
                            message = usnOut.equals(usnIn) ? "동승 가능한 차량이 없습니다.\n일반 차량이 배차되었습니다." : "요청이 들어왔습니다.\n운행을 시작합니다.";
                            break;
                        case 3:
                            message = usnOut.equals(usnIn) ? "일반 차량이 배차되었습니다." : "요청이 들어왔습니다.\n운행을 시작합니다.";
                            break;
                        case 4:
                            message = usnOut.equals(usnIn) ? "차량이 도착하였습니다.\n탑승해 주시기 바랍니다." : "탑승자가 승차하였습니다.\n경로를 수정합니다.";
                            break;
                        case 5:
                            message = usnOut.equals(usnIn) ? "목적지에 도착하였습니다.\n하차해 주시기 바랍니다." : path != null ? "탑승자가 하차하였습니다.\n경로를 수정합니다." : "마지막 탑승자가 하차하였습니다.";
                            break;
                        default:
                            message = "배차 오류가 발생하였습니다.\n고객센터에 문의하세요.";
                            break;
                    }
                    Toaster.show(getApplicationContext(), message  + "FLAG : " + flag);
                    System.out.println(path);


                    // Event handle by flag number
                    if (!(flag == -1 || flag == 0)) {
                        if (flag == 1 || flag == 2 || flag == 3) {
                            // Set inner DB
                            InnerDB.editor.putInt("state", usnOut.equals(usnIn) ? 1 : InnerDB.sp.getInt("state", 0)).apply();
                            InnerDB.editor.putString("path", path.toString()).apply();

                            // Set View's variable
                            btnPass.setEnabled(true);
                            btnPass.setBackgroundColor(Color.parseColor("#FF127CEA"));
                            btnPass.setText("경유");
                            tvNext.setText("NEXT : " + path.getJSONArray("waypoints").getJSONObject(0).getString("name"));

                            // Show path on Map
                            showPathOnMap(path);
                        } else if (flag == 4) {
                            // Set inner DB
                            InnerDB.editor.putString("path", path.toString()).apply();

                            // Set View's variable
                            tvNext.setText("NEXT : " + (waypoints.length() == 0 ? path.getJSONObject("destination").getString("name") : waypoints.getJSONObject(0).getString("name")));
                            if (path.getJSONArray("waypoints").length() == 0)
                                btnPass.setText("도착");

                            // Show path on Map
                            showPathOnMap(path);
                        } else if (flag == 5) {
                            if (usnOut.equals(usnIn)) {     // When current user get off
                                InnerDB.editor.putInt("state", 0).apply();
                                InnerDB.editor.putString("path", null).apply();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else if (path != null) {      // When not current user and path is still left
                                // Set inner DB
                                InnerDB.editor.putString("path", path.toString()).apply();

                                JSONArray points = path.getJSONArray("waypoints");
                                // Set View's variable
                                tvNext.setText("NEXT : " + (points.length() != 0 ? points.getJSONObject(0).getString("name") : path.getJSONObject("destination").getString("name")));
                                if (path.getJSONArray("waypoints").length() == 0)
                                    btnPass.setText("도착");

                                // Show path on Map
                                showPathOnMap(path);
                            } else {                        // When driver and service ends
                                // Set inner DB
                                InnerDB.editor.putString("path", null).apply();

                                // Set View's variable
                                tvNext.setText("요금을 정산해주세요.");
                                btnPass.setEnabled(false);
                                btnPass.setBackgroundColor(Color.parseColor("#BDBDBD"));
                                btnPass.setText("경유");
                                btnEndService.setEnabled(true);
                                btnEndService.setBackgroundColor(Color.parseColor("#FF127CEA"));
                                switchDrive.setChecked(false);

                                // Erase map
                                for (MapPolyline mp : mapView.getPolylines())
                                    mapView.removePolyline(mp);
                            }
                        }
                    } else {    // When there is no vehicle available
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        // End service
        Network.socket.on("serviceEnd", args -> {
            runOnUiThread(() -> {
                JSONObject result = (JSONObject) args[0];
                Toaster.show(getApplicationContext(), "운행이 종료되었습니다.\n영수증을 확인하여 주세요.");
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra("result", result.toString());
                startActivity(intent);
            });
        });
    }

    // < Register views >
    private void setViews() {
        btnPass = findViewById(R.id.btn_pass_navigation);
        btnEndService = findViewById(R.id.btn_end_service_navigation);
        switchDrive = findViewById(R.id.switch_drive_state_navigation);
        tvNext = findViewById(R.id.tv_waypoints_navigation);
    }

    // < Register event listeners >
    private void setEventListeners() {
        // Pass next waypoint [SOCKET]
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject driver = InnerDB.getUser();
                    Network.socket.emit("passWaypoint", driver);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // End service [SOCKET]
        btnEndService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject driver = InnerDB.getUser();
                    Network.socket.emit("serviceEnd", driver);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // Change service availability [SOCKET]
        switchDrive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (btnEndService.isEnabled()) {
                    Toaster.show(getApplicationContext(), "요금을 정산해주세요.");
                    switchDrive.setChecked(false);
                } else {
                    try {
                        JSONObject driver = InnerDB.getUser();
                        driver.put("service", isChecked);
                        Network.socket.emit(isChecked ? "serviceOn" : "serviceOff", driver);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // < Show path data on map >
    private void showPathOnMap(JSONObject path) {
        try {
            // Erase map
            for (MapPolyline mp : mapView.getPolylines())
                mapView.removePolyline(mp);

            // INIT
            polyline = new MapPolyline();
            polyline.setLineColor(Color.argb(128, 0, 0, 255));
            JSONObject origin = path.getJSONObject("origin");
            JSONObject destination = path.getJSONObject("destination");
            JSONArray waypoints = path.getJSONArray("waypoints");

            // Add points
            polyline.addPoint(point.mapPointWithGeoCoord(origin.getDouble("y"), origin.getDouble("x")));
            for (int i = 0; i < waypoints.length(); i++) {
                JSONObject waypoint = waypoints.getJSONObject(i);
                polyline.addPoint(point.mapPointWithGeoCoord(waypoint.getDouble("y"), waypoint.getDouble("x")));
            }
            polyline.addPoint(point.mapPointWithGeoCoord(destination.getDouble("y"), destination.getDouble("x")));

            // Show
            mapView.addPolyline(polyline);
            MapPointBounds bounds = new MapPointBounds(polyline.getMapPoints());
            mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(bounds, 100));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (switchDrive.isChecked()) {
            Toaster.show(getApplicationContext(), "서비스 제공 중에는 종료할 수 없습니다.");
        } else {
            if (type && !InnerDB.sp.getString("path", "NULL").equals("NULL")) {
                Toaster.show(getApplicationContext(), "운행중에는 내비게이션을 종료할 수 없습니다.");
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }
}
