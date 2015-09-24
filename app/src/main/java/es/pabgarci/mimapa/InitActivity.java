package es.pabgarci.mimapa;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

//esto es una prueba

public class InitActivity extends AppCompatActivity {

    LocationsDBHandler admin;
    SQLiteDatabase db;
    ListView list;
    ArrayAdapter<String> adapter;

    public int countDB() {
        Cursor c = db.rawQuery("Select * from Locations", null);
        return c.getCount();
    }

    public String[] fillArrayFromDB(){
        String values[] = new String[countDB()];

        for(int i=1;i<=countDB();i++){

            String name;
            String address;
            String city;
            String text;
            String lat;
            String lon;


            db = admin.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT _id, NAME, ADDRESS, CITY FROM Locations WHERE _id=" + i, null);
            c.moveToFirst();
            name=c.getString(1);
            address=c.getString(2);
            city=c.getString(3);
            lat=c.getString(4);
            lon=c.getString(5);

            text= name + ", " + address + ", " + city;

            values[i-1] = text;
        }


        return values;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        admin = new LocationsDBHandler(this,"Locations", null, 1);
        db = admin.getWritableDatabase();
        //list = (ListView)findViewById(R.id.listView);
        //setListView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setListView(){

        ArrayList<String> valuesList = new ArrayList<String>();
        valuesList.addAll(Arrays.asList(fillArrayFromDB()));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, valuesList);

        list.setAdapter(adapter);
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, Map.class);
        startActivityForResult(intent, 1);
    }

    public void deleteDB(){
        db.delete("Locations", null, null);
        setListView();
    }

    public void writeDB(String name, String address, String city, String lat, String lon){

        ContentValues registro = new ContentValues();  //es una clase para guardar datos
        registro.put("_id", countDB()+1);
        registro.put("NAME", name);
        registro.put("ADDRESS", address);
        registro.put("CITY", city);
        registro.put("LAT", lat);
        registro.put("LON", lon);
        db.insert("Locations", null, registro);
        setListView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1) {

            if (data != null) {

                String showName = data.getStringExtra("NAME");
                String showAddress = data.getStringExtra("ADDRESS");
                String showCity = data.getStringExtra("CITY");
                String showLat = data.getStringExtra("LAT");
                String showLon = data.getStringExtra("LON");
                //mLocationViewLastLocation.setText("Last location:\n" + showName + "\n" + showAddress + ", " + showCity);
                String show = "Location saved:\n" + showName + "\n" + showAddress + ", " + showCity;
                Toast.makeText(getApplicationContext(), show, Toast.LENGTH_SHORT).show();
                writeDB(showName, showAddress, showCity, showLat, showLon);
                setListView();

            }else if(data==null){
                Toast.makeText(getApplicationContext(), "Any location saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_init, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
