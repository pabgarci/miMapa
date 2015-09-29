package es.pabgarci.mimapa;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

//test comment
public class InitActivity extends AppCompatActivity {

    LocationsDBHandler admin;
    SQLiteDatabase db;
    ListView list;
    Toolbar toolbar;
    SharedPreferences sharedPref;

    public int countDB() {
        int count;
        Cursor c = db.rawQuery("Select * from Locations", null);
        count = c.getCount();
        c.close();
        return count;
    }


    public String[] fillArrayFromDB() {
        String values[] = new String[countDB()];

        for (int i = 1; i <= countDB(); i++) {

            String name;
            String address;
            String city;
            String text;
            int id;

            db = admin.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT _id, NAME, ADDRESS, CITY FROM Locations WHERE _id=" + i, null);
            c.moveToFirst();
            id = c.getInt(0);
            name = c.getString(1);
            address = c.getString(2);
            city = c.getString(3);
            c.close();
            text = id + ".- " + name + ", " + address + ", " + city;

            values[i - 1] = text;
        }


        return values;
    }

    public String getName(int idAux) {

        String nameAux;

        db = admin.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT NAME FROM Locations WHERE _id=" + idAux, null);
        c.moveToFirst();
        nameAux = c.getString(0);
        c.close();
        return nameAux;

    }

    public String getAddressAndCity(int idAux) {

        String nameAddress;
        String nameCity;

        db = admin.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT ADDRESS, CITY FROM Locations WHERE _id=" + idAux, null);
        c.moveToFirst();
        nameAddress = c.getString(0);
        nameCity = c.getString(1);
        c.close();
        return nameAddress + ", " + nameCity;
    }

    public double getLat(int idAux) {

        double latAux;

        db = admin.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT LAT FROM Locations WHERE _id=" + idAux, null);
        c.moveToFirst();
        latAux = c.getDouble(0);
        c.close();
        return latAux;

    }

    public double getLon(int idAux) {

        double lonAux;

        db = admin.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT LON FROM Locations WHERE _id=" + idAux, null);
        c.moveToFirst();
        lonAux = c.getDouble(0);
        c.close();

        return lonAux;

    }

    public String getPhotoLocation(int idAux) {

        String photoAux;

        db = admin.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT PHOTOLOCATION FROM Locations WHERE _id=" + idAux, null);
        c.moveToFirst();
        photoAux = c.getString(0);
        c.close();
        return photoAux;

    }

    public void setPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Toast.makeText(getApplicationContext(), "" + sharedPref.getBoolean("data_storage", true), Toast.LENGTH_SHORT).show();
    }

    public void loadToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle("Inicio");
    }

    public void setLanguage(){
        String languageToLoad  = Locale.getDefault().getLanguage();
        String languageSetings = sharedPref.getString("pref_language", "no");
        Locale locale;
        Configuration config = new Configuration();
        if(languageSetings.equals(languageToLoad)) {
            locale= new Locale (languageToLoad);

        }else if(languageSetings.equals("auto")){
            locale= new Locale (languageToLoad);
        }else{
            locale= new Locale (languageSetings);
        }
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void setMyTheme(){
        String color = sharedPref.getString("pref_color", "Green");
        if(color.equals("Green")){
            setTheme(R.style.AppThemeGreen);
        }else if(color.equals("Orange")){
            setTheme(R.style.AppThemeOrange);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setPreferences();
        setMyTheme();
        setLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        admin = new LocationsDBHandler(this, "Locations", null, 1);
        db = admin.getWritableDatabase();
        list = (ListView) findViewById(R.id.listView);
        setListView();
        loadToolbar();


    }

    public void goToLocationDetails(int id) {
        Intent intent = new Intent(this, LocationDetailsActivity.class);
        Bundle b = new Bundle();
        b.putString("SHOWNAME", getName(id));
        b.putString("SHOWADDRESS", getAddressAndCity(id));
        b.putDouble("SHOWLAT", getLat(id));
        b.putDouble("SHOWLON", getLon(id));
        b.putString("PHOTOLOCATION", getPhotoLocation(id));
        intent.putExtras(b);
        startActivity(intent);

    }

    public void setListView() {
        ArrayList<String> valuesList = new ArrayList<>();
        valuesList.addAll(Arrays.asList(fillArrayFromDB()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, valuesList);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                goToLocationDetails((int) id + 1);
                return true;
            }
        });

        list.setAdapter(adapter);
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult(intent, 1);
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void deleteDB() {
        db.delete("Locations", null, null);
        setListView();
    }

    public void writeDB(String name, String address, String city, double lat, double lon, String photoLocation) {

        ContentValues registro = new ContentValues();  //es una clase para guardar datos
        registro.put("_id", countDB() + 1);
        registro.put("NAME", name);
        registro.put("ADDRESS", address);
        registro.put("CITY", city);
        registro.put("LAT", lat);
        registro.put("LON", lon);
        registro.put("PHOTOLOCATION", photoLocation);
        db.insert("Locations", null, registro);
        setListView();

    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        double showLat;
        double showLon;

        if (requestCode == 1) {

            if (data != null) {
                Bundle b = data.getExtras();
                String showName = b.getString("NAME");
                String showAddress = b.getString("ADDRESS");
                String showCity = b.getString("CITY");
                showLat = b.getDouble("LAT");
                showLon = b.getDouble("LON");
                String photoLocation = b.getString("PHOTO_LOCATION");
                String show = getResources().getString(R.string.text_location_saved) + "\n" + showName + "\n" + showAddress + ", " + showCity;
                Toast.makeText(getApplicationContext(), show, Toast.LENGTH_SHORT).show();
                writeDB(showName, showAddress, showCity, showLat, showLon, photoLocation);
                setListView();

            } else {
                Toast.makeText(getApplicationContext(), R.string.text_no_location, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_init, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_settings:
                    goToSettings(findViewById(android.R.id.content));
                    break;
                case R.id.action_clearRecords:
                    deleteDB();
            }
            return true;
        }

    }

