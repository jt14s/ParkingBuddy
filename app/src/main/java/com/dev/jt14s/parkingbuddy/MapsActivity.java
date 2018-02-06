package com.dev.jt14s.parkingbuddy;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.markSpotButton) FloatingActionButton spotMarkerButton;

    private int REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    private LatLng parkingLatLng;
    private LatLng userLatLng;
    private Map<String,Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        markers = new HashMap<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                spotMarkerButton.setVisibility(View.VISIBLE);
                spotMarkerButton.setClickable(true);

                if(markers.containsKey("user_marker"))
                    markers.get("user_marker").remove();
                userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                Marker userMarker = mMap.addMarker(new MarkerOptions()  .position(userLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_icon)));
                markers.put("user_marker", userMarker);
                if (userLatLng != null && parkingLatLng != null) {
                    userMarker.setRotation(getBearing());
                    /*CameraPosition cameraPosition = CameraPosition
                            .builder(mMap.getCameraPosition()).bearing(getBearing()).build();*/
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
                    //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { mMap = googleMap; }

    @OnClick(R.id.markSpotButton)
    public void markParkingSpot() {
        if (markers.containsKey("parking_marker")) {

        }
        parkingLatLng = userLatLng;
        Marker parkingMarker = mMap.addMarker(new MarkerOptions()
                .position(userLatLng)
                .title("Parked Here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));
        markers.put("parking_marker", parkingMarker);
    }

    public float getBearing() {
        Location userLocation = new Location("userLocation");
        userLocation.setLatitude(userLatLng.latitude);
        userLocation.setLongitude(userLatLng.longitude);

        Location parkingLocation = new Location("parkingLocation");
        parkingLocation.setLatitude(parkingLatLng.latitude);
        parkingLocation.setLongitude(parkingLatLng.longitude);

        return userLocation.bearingTo(parkingLocation);
    }
}
