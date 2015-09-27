package es.pabgarci.mimapa;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap showMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);
        setUpMapIfNeeded();

        Bundle extraInfo = getIntent().getExtras();

        if(extraInfo==null){
            finish();
        }else {
            showLocation(extraInfo.getDouble("SHOWLAT"), extraInfo.getDouble("SHOWLON"));
        }
    }

    public void showLocation(double showLat,double showLon){
        LatLng showLatLon = new LatLng(showLat, showLon);
        Toast.makeText(getApplicationContext(), "Lat: " + showLat + ", Lon: " + showLon, Toast.LENGTH_SHORT).show();
        showMap.addMarker(new MarkerOptions().position(showLatLon).title("You are here"));
        showMap.animateCamera(CameraUpdateFactory.newLatLngZoom(showLatLon, 15));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (showMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            showMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
          /*  if (showMap != null) {

            }*/
        }
    }

}
