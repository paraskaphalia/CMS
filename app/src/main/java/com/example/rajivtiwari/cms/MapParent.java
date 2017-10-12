package com.example.rajivtiwari.cms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class MapParent extends AppCompatActivity implements OnMapReadyCallback {

    private Marker myMarker,childMarker;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private DatabaseReference parentReference,childReference;
    private FirebaseAuth firebaseAuth;
    private UserDetails userDetails;
    private MyLocation parentLocation,childLocation;
    private SharedPreferences sharedPreferences;
    private LatLng ll,latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_parent);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("com.example.rajivtiwari.cms", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), " ");
        userDetails = gson.fromJson(json, UserDetails.class);

        if(userDetails.getLinkedNumber() == null) {
            startActivity(new Intent(MapParent.this, getNoChild.class));
        }

        gson = new Gson();
        json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), " ");
        userDetails = gson.fromJson(json, UserDetails.class);

        childReference = FirebaseDatabase.getInstance().getReference().child("Users");
        parentReference = FirebaseDatabase.getInstance().getReference().child("Users");


        childReference.child(userDetails.getLinkedNumber()).child("Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childLocation = dataSnapshot.getValue(MyLocation.class);

                ll = new LatLng(childLocation.getLatitude(),childLocation.getLongitude());
                Log.i(String.valueOf(ll.latitude),String.valueOf(ll.longitude));
                if(childMarker != null)
                    childMarker.remove();
                childMarker = mMap.addMarker(new MarkerOptions().position(ll).title("Child's Location"));
                //childMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(ll));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in delhi and move the camera
        LatLng delhi = new LatLng(28.6139, 77.2090);
        myMarker = mMap.addMarker(new MarkerOptions().position(delhi).title("Marker in New Delhi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(delhi,3));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latlng = new LatLng(location.getLatitude(),location.getLongitude());
                if(myMarker!=null)
                    myMarker.remove();
                myMarker = mMap.addMarker(new MarkerOptions().position(latlng).title("parent Location").draggable(true));
                //myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,3));

                parentLocation = new MyLocation(location.getLatitude(),location.getLongitude());
                parentReference.child(userDetails.getPhoneNumber()).child("Location").setValue(parentLocation);

              //Intent directionsIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + latlng.latitude + "," + latlng.longitude + "&daddr=" + ll.latitude + "," + ll.longitude));
                //startActivity(directionsIntent);
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
            startActivityForResult(new Intent(getApplicationContext(),settings.class),1);

            Gson gson = new Gson();
            String json = sharedPreferences.getString(firebaseAuth.getCurrentUser().getEmail(), "NULL");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==123 && requestCode==1){
            Toast.makeText(this, "Signing Out", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            finish();
        }
    }
}
