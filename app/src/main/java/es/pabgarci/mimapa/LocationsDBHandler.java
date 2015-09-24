package es.pabgarci.mimapa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationsDBHandler extends SQLiteOpenHelper {

    public LocationsDBHandler(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Locations(_id integer primary key autoincrement, NAME text, ADDRESS text, CITY text, LAT text, LON text);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int lastVersion, int newVersion) {
        db.execSQL("drop table if exists Locations");
        db.execSQL("create table Locations(_id integer primary key, NAME text, ADDRESS text, CITY text, LAT text, LON text);");
    }
}
