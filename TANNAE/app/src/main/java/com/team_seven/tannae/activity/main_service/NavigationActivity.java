package com.team_seven.tannae.activity.main_service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.team_seven.tannae.R;
import com.team_seven.tannae.network.Network;
import com.team_seven.tannae.sub.InnerDB;
import com.team_seven.tannae.sub.Toaster;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// << Navigation Activity >>
public class NavigationActivity extends AppCompatActivity {
    private Button btnEndService, btnPass;
    private Switch switchDrive;
    private TextView tvNext;
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private boolean type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        type = getIntent().getBooleanExtra("type", false);
        setViews();
        setEventListeners();
        setNetworks();
        setMap();

        try {
            if (!type) {
                btnPass.setVisibility(View.INVISIBLE);
                btnEndService.setVisibility(View.INVISIBLE);
                switchDrive.setVisibility(View.INVISIBLE);

                if (InnerDB.sp.getInt("state", 0) == 1) {
                    Toaster.show(getApplicationContext(), "서비스를 이용중입니다.");
                    JSONObject path = new JSONObject(InnerDB.sp.getString("path", ""));
                    showPathOnMap(path);
                } else
                    Network.socket.emit("requestService", new JSONObject(getIntent().getStringExtra("data")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // < Register views >
    private void setViews() {
        (btnPass = findViewById(R.id.btn_pass_navigation)).setOnClickListener(v -> Network.socket.emit("passWaypoint", InnerDB.getUser()));
        (btnEndService = findViewById(R.id.btn_end_service_navigation)).setOnClickListener(v -> Network.socket.emit("serviceEnd", InnerDB.getUser()));
        btnPass.setBackgroundColor(Color.parseColor("#BDBDBD"));
        btnEndService.setBackgroundColor(Color.parseColor("#BDBDBD"));
        switchDrive = findViewById(R.id.switch_drive_state_navigation);
        tvNext = findViewById(R.id.tv_waypoints_navigation);
    }

    // < Register event listeners >
    private void setEventListeners() {
        // Change service availability [SOCKET]
        switchDrive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (btnEndService.isEnabled()) {
                Toaster.show(getApplicationContext(), "요금을 정산해주세요.");
                switchDrive.setChecked(false);
            } else
                try {
                    Network.socket.emit(isChecked ? "serviceOn" : "serviceOff", InnerDB.getUser().put("service", isChecked));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setNetworks() {
        // Response service
        Network.socket.on("responseService", args -> {
            if (InnerDB.state == 3)
                return;
            // args[0] = flag:int / args[1] = path:JSONObject / args[2] = usn:String
            runOnUiThread(() -> {
                try {
                    // Flag : -2 (Server Error) / -1 (Unavailable Path) / 0 (No Vehicle) / 1 (Share vehicle) / 2 (Non-share vehicle to share user) / 3 (Non-share vehicle to non-share user)
                    // 4 (Passenger boarding) / 5 (Passenger get off)
                    int flag = (int) args[0];
                    JSONObject path = (JSONObject) args[1];
                    String usnOut = (String) args[2];
                    String usnIn = InnerDB.sp.getString("usn", "");
                    JSONArray waypoints = path != null ? path.getJSONArray("waypoints") : null;

                    // Toast
                    Toaster.show(getApplicationContext(), flag == -1 ? "해당 경로는 현재 교통 상황으로 인해 서비스가 불가합니다."
                            : flag == 0 ? "이용 가능한 차량이 없습니다."
                            : flag == 1 ? (usnOut.equals(usnIn) ? "동승 차량이 배차되었습니다." : "추가 인원이 배차되었습니다.\n경로를 수정합니다.")
                            : flag == 2 ? (usnOut.equals(usnIn) ? "동승 가능한 차량이 없습니다.\n일반 차량이 배차되었습니다." : "요청이 들어왔습니다.\n운행을 시작합니다.")
                            : flag == 3 ? (usnOut.equals(usnIn) ? "일반 차량이 배차되었습니다." : "요청이 들어왔습니다.\n운행을 시작합니다.")
                            : flag == 4 ? (usnOut.equals(usnIn) ? "차량이 도착하였습니다.\n탑승해 주시기 바랍니다." : "탑승자가 승차하였습니다.\n경로를 수정합니다.")
                            : flag == 5 ? (usnOut.equals(usnIn) ? "목적지에 도착하였습니다.\n하차해 주시기 바랍니다." : path != null ? "탑승자가 하차하였습니다.\n경로를 수정합니다." : "마지막 탑승자가 하차하였습니다.")
                            : "배차 오류가 발생하였습니다.\n고객센터에 문의하세요.");

                    // Event handle by flag number
                    if (!(flag == -1 || flag == 0)) {
                        if (flag == 1 || flag == 2 || flag == 3) {
                            // Set inner DB
                            InnerDB.state = usnOut.equals(usnIn) ? 1 : InnerDB.state;
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
                                InnerDB.state = 3;
                                InnerDB.editor.putInt("state", 0).apply();
                                InnerDB.editor.putString("path", null).apply();

                                mapViewContainer.removeView(mapView);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
                                // Erase map
                                mapView.removeAllCircles();
                                mapView.removeAllPolylines();

                                // Get & Set inner DB
                                JSONObject destination = new JSONObject(InnerDB.sp.getString("path", "")).getJSONObject("destination");
                                InnerDB.editor.putString("path", null).apply();

                                // Draw destination
                                mapView.addCircle(new MapCircle(MapPoint.mapPointWithGeoCoord(destination.getDouble("y"), destination.getDouble("x")), 15,
                                        Color.argb(200, 255, 0, 0), Color.argb(255, 255, 0, 0)));

                                // Set View's variable
                                tvNext.setText("요금을 정산해주세요.");
                                btnPass.setEnabled(false);
                                btnPass.setBackgroundColor(Color.parseColor("#BDBDBD"));
                                btnPass.setText("경유");
                                btnEndService.setEnabled(true);
                                btnEndService.setBackgroundColor(Color.parseColor("#FF127CEA"));
                                switchDrive.setChecked(false);
                            }
                        }
                    } else {    // When there is no vehicle available
                        mapViewContainer.removeView(mapView);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        // End service
        Network.socket.on("serviceEnd", args -> runOnUiThread(() -> {
            InnerDB.state = 0;
            Toaster.show(getApplicationContext(), "운행이 종료되었습니다.\n영수증을 확인하여 주세요.");
            mapViewContainer.removeView(mapView);
            startActivity(new Intent(getApplicationContext(), PaymentActivity.class).putExtra("result", args[0].toString()));
        }));
    }

    public void setMap() {
        mapView = new MapView(this);
        mapViewContainer = findViewById(R.id.map_view_navigation);
        mapView.setZoomLevel(2, true);

        if (type) {
            Network.service.getPos(InnerDB.sp.getString("usn", null)).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject res = new JSONObject(response.body());
                        String[] pos = res.getString("pos").split(" ");
                        double longitude = Double.parseDouble(pos[0]);
                        double latitude = Double.parseDouble(pos[1]);
                        mapView.addCircle(new MapCircle(MapPoint.mapPointWithGeoCoord(latitude, longitude), 20,
                                Color.argb(200, 255, 0, 0), Color.argb(255, 255, 0, 0)));
                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                        mapViewContainer.addView(mapView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toaster.show(getApplicationContext(), "Error");
                    Log.e("Error", t.getMessage());
                }
            });
        } else {
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.1761175, 126.9058167), true);
            mapViewContainer.addView(mapView);
        }
    }

    // < Show path data on map >
    private void showPathOnMap(JSONObject path) {
        try {
            // Get path
            JSONObject origin = path.getJSONObject("origin");
            JSONObject destination = path.getJSONObject("destination");
            JSONArray waypoints = path.getJSONArray("waypoints");

            // Erase map
            mapView.removeAllPolylines();
            mapView.removeAllCircles();

            // INIT Polyline
            MapPolyline polyline = new MapPolyline();
            polyline.setLineColor(Color.argb(255, 240, 128, 128));

            // Add total path
            MapCircle circle = new MapCircle(MapPoint.mapPointWithGeoCoord(origin.getDouble("y"), origin.getDouble("x")), 15,
                    Color.argb(200, 255, 0, 0), Color.argb(255, 255, 0, 0));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(origin.getDouble("y"), origin.getDouble("x")));
            for (int i = 0; i < waypoints.length(); i++) {
                JSONObject waypoint = waypoints.getJSONObject(i);
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(waypoint.getDouble("y"), waypoint.getDouble("x")));
            }
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(destination.getDouble("y"), destination.getDouble("x")));

            // Show
            mapView.addPolyline(polyline);
            mapView.addCircle(circle);
            MapPointBounds bounds = new MapPointBounds(polyline.getMapPoints());
            mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(bounds, 100));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (switchDrive.isChecked())
            Toaster.show(getApplicationContext(), "서비스 제공 중에는 종료할 수 없습니다.");
        else if (type && !InnerDB.sp.getString("path", "NULL").equals("NULL"))
            Toaster.show(getApplicationContext(), "운행중에는 내비게이션을 종료할 수 없습니다.");
        else {
            mapViewContainer.removeView(mapView);
            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }
}
