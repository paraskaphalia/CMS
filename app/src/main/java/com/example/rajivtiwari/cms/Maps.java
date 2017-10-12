package com.example.rajivtiwari.cms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.gson.Gson;


public class Maps extends AppCompatActivity implements OnMapReadyCallback {

    private Marker marker;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private DatabaseReference firebasDatabase;
    private FirebaseAuth firebaseAuth;
    private UserDetails userDetails;
    private MyLocation myLocation;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebasDatabase = FirebaseDatabase.getInstance().getReference("Users");
        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);

        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "");
        userDetails = gson.fromJson(json, UserDetails.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in delhi and move the camera
        LatLng delhi = new LatLng(28.6139, 77.2090);
        marker = mMap.addMarker(new MarkerOptions().position(delhi).title("Marker in New Delhi"));
        //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(delhi,5));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
                if(marker!=null)
                    marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latlng).title("My Location").draggable(true));
                //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,7));

                myLocation = new MyLocation(location.getLatitude(),location.getLongitude());
                firebasDatabase.child(userDetails.getPhoneNumber()).child("Location").setValue(myLocation);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };

        configure_button();
    }

    void configure_button(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }

        locationManager.requestLocationUpdates("gps", 5000, 150, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.primary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings) {
            startActivityForResult(new Intent(getApplicationContext(),settings.class), 1);

            Gson gson = new Gson();
            String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "");
            userDetails = gson.fromJson(json, UserDetails.class);
        }
        if(id == R.id.action_logout) {
            firebaseAuth.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onZoom(View view) {

        if(view.getId() == R.id.zoom_in) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        else
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==123 && requestCode==1){
            Toast.makeText(this, "Signing Out", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            finish();
        }
    }
}
