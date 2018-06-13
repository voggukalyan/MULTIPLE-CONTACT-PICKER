package com.example.abhishek.listnsave2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="ContactDetails";
    public static final int DATABASE_VERSION =2;
    public static final String TABLE_CONTACTS = "Contacts";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_PHONE = "Phone";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_ORG = "Org";
    public static final String KEY_STREET = "Street";
    public static final String KEY_CITY = "City";
    public static final String KEY_STATE = "State";
    public static final String KEY_COUNTRY = "Country";

    public static final String CREATE_CONTACTS = "Create table Contacts(_id  INTEGER PRIMARY KEY AUTOINCREMENT,Name varchar(255)," +
            "Phone varchar(255),Email varchar(255),Org varchar(255),Street varchar(255),City varchar(255),State varchar(255),Country varchar(255));";
    public static final String UPGRADE_CONTACTS = "Drop table if exists "+TABLE_CONTACTS;

    public ContactDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_CONTACTS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(UPGRADE_CONTACTS);
            onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

