package es.pabgarci.mimapa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/*
* This activity shows the details of a saved location from the database
* it shows the picture saved from the location, the street, city and country,
* as well as the latitude and longitude of the location.
* To show the picture first it gets the size of the window to determine the size of the picture
 */


public class LocationDetailsActivity extends AppCompatActivity {

    double showLat;
    double showLon;

    private Bitmap getImageFromSD(String photoLocation){
        Bitmap bitmap = null;
        File imageFile = new File(photoLocation);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    /*
 *IMPORTANT NOTE: we couldn't manage to make it work in certain phone brands,
 * because of their own camera app. The native camera app saves the picture in its default folder
 * and with the default name, this way when we want to get the picture we have a null pointer exception
 * in the name and folder we are using, but in some brands such as samsung it works perfectly
 * */
    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        Bundle b = getIntent().getExtras();
        String showName = b.getString("SHOWNAME");

        String showAddress = b.getString("SHOWADDRESS");
        showLat = b.getDouble("SHOWLAT");
        showLon = b.getDouble("SHOWLON");
        String photoLocation = b.getString("PHOTOLOCATION");

        TextView detailsName = (TextView)findViewById(R.id.textView_details_large);
        TextView detailsAddress = (TextView)findViewById(R.id.textView_details_medium);
        TextView detailsLatLon = (TextView)findViewById(R.id.textView_details_small);
        ImageView detailsImage = (ImageView)findViewById(R.id.imageView_details);

        detailsName.setText(showName);
        detailsAddress.setText(showAddress);
        String textAux = "Lat: "+showLat+", Lon: "+showLon;
        detailsLatLon.setText(textAux);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("miMapa");
        getSupportActionBar().setSubtitle("Detalles de localización");

        Display display = getWindowManager().getDefaultDisplay();
        assert photoLocation != null;
        if(photoLocation.equals("NO")) {
            detailsImage.setImageResource(R.drawable.nophoto);
        }else{
            Bitmap bitMapScaled = Bitmap.createScaledBitmap(getImageFromSD(photoLocation), display.getWidth(), display.getHeight() - 350, true);
            if(bitMapScaled!=null) {
                detailsImage.setImageBitmap(bitMapScaled);
            }
        }


    }


    public void showLocationOnMap(View view) {
        Intent intentShow = new Intent(this, ShowLocationActivity.class);
        Bundle b = new Bundle();
        b.putDouble("SHOWLAT", showLat);
        b.putDouble("SHOWLON", showLon);
        intentShow.putExtras(b);
        startActivity(intentShow);
    }


}
