package jcubed.wakemewhen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = "MapsActivity: ";
    private GoogleMap mMap;
    boolean permissionGranted = false;
    LocationManager locationManager;
    LocationListener locationListener;
    Double[] alarmLatLong = new Double[2];
    String addressString;
    final private int REQUEST_ACCESS_CODE = 123;
    private boolean mapReady = false;
    private boolean destinationSet = false;
    private boolean hasAddress = false;
    private final MarkerOptions destMarker = new MarkerOptions();
    static final String LATITUTDE = "latitude";
    static final String LONGITUDE = "longitude";
    static final String ADDRESS = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (mapReady) {
                    LatLng dest = place.getLatLng();
                    alarmLatLong[0] = dest.latitude;
                    alarmLatLong[1] = dest.longitude;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest,17));
                    destMarker.position(dest);
                    destMarker.title("Destination");
                    mMap.addMarker(destMarker);
                    destinationSet = true;
                    if (place.getAddress() != null) {
                        addressString = place.getAddress().toString();
                        hasAddress = true;
                    }

                } else { Log.w(TAG, "Map not ready!"); }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        Button doneBtn = (Button) findViewById(R.id.done_button);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!destinationSet) {
                    CharSequence text = "Please set a destination.";
                    Toast.makeText(MapsActivity.this, text, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "dest not set");
                } else {
                    Intent i = new Intent(MapsActivity.this, AlarmEdit.class);
                    i.putExtra(LATITUTDE, alarmLatLong[0]);
                    i.putExtra(LONGITUDE, alarmLatLong[1]);
                    if (hasAddress) { i.putExtra(ADDRESS, addressString); }
                    startActivity(i);
                }
            }
        });


    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                double lat = latLng.latitude;
                double lon = latLng.longitude;
                LatLng dest = new LatLng(lat, lon);
                destMarker.position(dest);
                destMarker.title("Destination");
                mMap.addMarker(destMarker);
                alarmLatLong[0] = lat;
                alarmLatLong[1] = lon;
                destinationSet = true;
                Log.i(TAG, "Lat: " + Double.toString(lat) + ", Lon: " + Double.toString(lon));
            }
        });


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_CODE);
            Log.d(TAG, "Permissions not granted.");
            return;
        } else {
            permissionGranted = true;
        }

        if (permissionGranted) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);

            Log.d(TAG, "Requesting location updates.");
        }

        //adds a marker at your current location (do we want this or not?)

        Location currentLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLoc != null) {
            LatLng p = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            MarkerOptions myMarker = new MarkerOptions();
            myMarker.position(p);
            myMarker.title("Current Location");
            mMap.addMarker(myMarker);
        }


    }

    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location loc) {
            if (loc != null) {

                LatLng p = new LatLng((int) (loc.getLatitude()),(int) (loc.getLongitude()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(p));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(8));
            }
        }

        //Called when a tracked/listened provider is disabled
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "Provider disabled.");
        }

        //Called when a tracked/listened provider is enabled
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "Provider enabled.");
        }

        //Called when there is a change in the provider status
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String statusString = "";
            switch (status) {
                case LocationProvider.AVAILABLE:
                    statusString = "AVAILABLE";
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    statusString = "OUT OF SERVICE";
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    statusString = "TEMPORARILY UNAVAILABLE";
            }
            Log.d(TAG, statusString);
        }
    }
}
