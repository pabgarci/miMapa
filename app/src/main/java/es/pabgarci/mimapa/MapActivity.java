package es.pabgarci.mimapa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private String textAddress;
    private String textCity;
    private double textLan;
    private double textLon;
    private String namePlace;

    private TextView mLocationView;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mLocationView = (TextView) findViewById(R.id.textGPS);
        mLocationView.setText(R.string.text_getLocation);

        setUpMapIfNeeded();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("MyLocation", "Connection to Google Api has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult cResult) {
        Log.i("MyLocation", "Connection to Google Api has failed");
    }


    public String getAddress(double myLat, double myLng){

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String textAddress = "";

        try {
            List<Address> myAddress = geoCoder.getFromLocation(myLat,myLng,1);

            if (myAddress.size() > 0) {
                for (int i = 0; i < myAddress.get(0).getMaxAddressLineIndex(); i++)
                    textAddress = myAddress.get(0).getAddressLine(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return textAddress;
    }


    public String getCityAndCountryCode(double myLat, double myLng){

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String textCity = "";

        try {
            List<Address> myAddress = geoCoder.getFromLocation(myLat,myLng,1);

            if (myAddress.size() > 0) {
                for (int i = 0; i < myAddress.get(0).getMaxAddressLineIndex(); i++)
                    textCity = myAddress.get(0).getLocality()+" ("+myAddress.get(0).getCountryCode()+")";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return textCity;
    }

    @Override
    public void onLocationChanged(Location location) {
        String toShow = location.toString() + "\nAddress: " + getAddress(location.getLatitude(), location.getLongitude())
                + ", " + getCityAndCountryCode(location.getLatitude(), location.getLongitude());
        mLocationView.setText(toShow);
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(myLatLng).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,15));
        textAddress=getAddress(location.getLatitude(), location.getLongitude());
        textCity=getCityAndCountryCode(location.getLatitude(), location.getLongitude());
        textLan=location.getLatitude();
        textLon=location.getLongitude();
    }


    public void saveLocation(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter location name");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                namePlace = input.getText().toString();
                Bundle b = new Bundle();
                //String showAddress = "Location saved:\n" + namePlace + "\n" + textAddress + ", " + textCity;
                Intent intent=getIntent();
                b.putString("NAME", namePlace);
                b.putString("ADDRESS", textAddress);
                b.putString("CITY", textCity);
                b.putDouble("LAT", textLan);
                b.putDouble("LON", textLon);
                intent.putExtras(b);
                setResult(1, intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
    }
}
