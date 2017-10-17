package com.andrey.seconhands;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sts on 25.09.17.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SecondHandsManager";
    private static final String TABLE_SHOPS = "Shops";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CITY = "city";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_UPDATE_DAY = "update_day";

    final String TAG = "mLog";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //  context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_SHOP = "CREATE TABLE " + TABLE_SHOPS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT, "
                + KEY_CITY + " TEXT, "
                + KEY_ADDRESS + " TEXT, "
                + KEY_UPDATE_DAY + " TEXT);";
        sqLiteDatabase.execSQL(CREATE_TABLE_SHOP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_SHOPS);
        onCreate(sqLiteDatabase);
    }

    public void addShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, shop.getName());
        values.put(KEY_CITY, shop.getCity());
        values.put(KEY_ADDRESS, shop.getAddress());
        values.put(KEY_UPDATE_DAY, shop.getUpdateDay());

        // Inserting Row
        db.insert(TABLE_SHOPS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single shop
    public Shop getShop(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_SHOPS,
                new String[] { KEY_ID, KEY_NAME, KEY_CITY, KEY_ADDRESS, KEY_UPDATE_DAY },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Shop shop = new Shop(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        shop.setID(Integer.parseInt(cursor.getString(0)));

        return shop;
    }

    // Getting All shops
    public List<Shop> getAllShops() {
        List<Shop> contactList = new ArrayList<Shop>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SHOPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Shop shop = new Shop(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                shop.setID(Integer.parseInt(cursor.getString(0)));
                // Adding contact to list
                contactList.add(shop);
            } while (cursor.moveToNext());
        }

        // return shop list
        return contactList;
    }

    //select query with properties
    public List<Shop> selectWithProperties(String city, String name, String updateDay)
    {
        boolean existParametrs = false;
        String selectQuery = "SELECT * FROM " + TABLE_SHOPS;
        String cityClause = "", nameClause = "", updateDayCaluse = "";
        if(!city.equals("All") || !name.equals("All") || !updateDay.equals("All")) {
            selectQuery += " WHERE ";
            if (!city.equals("All"))
                cityClause = " city=\'" + city + "\'";
            if(!name.equals("All"))
                nameClause = " name=\'" + name + "\'";
            if(!updateDay.equals("All"))
                updateDayCaluse = " update_day=\'" + updateDay + "\'";
            existParametrs = true;
        }

        if(existParametrs) {
            if(!cityClause.equals("") && !nameClause.equals("") && !updateDayCaluse.equals(""))
                selectQuery += cityClause + " AND " + nameClause + " AND " + updateDayCaluse;
            else if (!cityClause.equals("") && !nameClause.equals(""))
                selectQuery += cityClause + " AND " + nameClause;
            else if (!cityClause.equals("") && !updateDayCaluse.equals(""))
                selectQuery += cityClause + " AND " + updateDayCaluse;
            else if (!nameClause.equals("") && !updateDayCaluse.equals(""))
                selectQuery += nameClause + " AND " + updateDayCaluse;
            else if (!cityClause.equals(""))
                selectQuery += cityClause;
            else if (!nameClause.equals(""))
                selectQuery += nameClause;
            else if (!updateDayCaluse.equals(""))
                selectQuery += updateDayCaluse;
        }

        Log.d(TAG, selectQuery);

        List<Shop> shops = new ArrayList<Shop>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Shop shop = new Shop(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                shop.setID(Integer.parseInt(cursor.getString(0)));
                // Adding contact to list
                shops.add(shop);
            } while (cursor.moveToNext());
        }

        return shops;
    }

    //get all cities
    public List<String> getAllCities()
    {
        List<String> cities = new ArrayList<String>();
        cities.add("All");

        String selectQuery = "SELECT city FROM " + TABLE_SHOPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do{
                String city = cursor.getString(0);
                boolean exist = false;
                for(int i = 0; i < cities.size(); i++)
                {
                   // Log.d(TAG, "city from db - " + city + " | " + cities.get(i) + " - city from list");
                    if(city.equals(cities.get(i)))
                       exist = true;
                }
                if(!exist)
                    cities.add(city);
            }
            while(cursor.moveToNext());
        }
        return cities;
    }

    //get all names
    public List<String> getAllNames()
    {
        List<String> names = new ArrayList<String>();
        names.add("All");

        String selectQuery = "SELECT name FROM " + TABLE_SHOPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do{
                String name = cursor.getString(0);
                boolean exist = false;
                for(int i = 0; i < names.size(); i++)
                {
                    // Log.d(TAG, "city from db - " + city + " | " + cities.get(i) + " - city from list");
                    if(name.equals(names.get(i)))
                        exist = true;
                }
                if(!exist)
                    names.add(name);
            }
            while(cursor.moveToNext());
        }
        return names;
    }

    public int getShopsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SHOPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //    cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, shop.getName());
        values.put(KEY_CITY, shop.getCity());
        values.put(KEY_ADDRESS, shop.getAddress());
        values.put(KEY_UPDATE_DAY, shop.getUpdateDay());

        // updating row
        return db.update(TABLE_SHOPS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(shop.getID()) });
    }

    public void deleteShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPS, KEY_ID + " = ?",
                new String[] { String.valueOf(shop.getID()) });
        db.close();
    }

    public void insertData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPS);
        Log.d("mLog", "deleted and created");
        onCreate(sqLiteDatabase);
        // Inserting Contacts
        Log.d(TAG, "Inserting ..");
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ЧАРИВНАЯ 46", "Saturday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ПОЧТОВАЯ, 141", "Friday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ГОГОЛЯ 32", "Tuesday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. СТАЛЕВАРОВ, 14А", "Wednesday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ЧАРИВНАЯ, 133", "Thursday"));
        addShop(new Shop("Econom Class", "Днепр", "Титова 31", "Wednesday"));
        addShop(new Shop("Econom Class", "Запорожье", "ПР. СОБОРНЫЙ, 148", "Monday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. БАЗАРНАЯ, 14Ж", "Friday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. РУСТАВИ, 2", "Friday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. НОВОКУЗНЕЦКАЯ, 27", "Saturday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ЖУКОВСКОГО, 31", "Friday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ГОРЬКОГО, 141", "Friday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. СЕВЕРО-КОЛЬЦЕВАЯ, 17", "Friday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ОРДЖОНИКИДЗЕ, 59", "Thursday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ПОЧТОВАЯ, 69", "Thursday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ЛАДОЖСКАЯ 9/12", "Wednesday"));
        addShop(new Shop("Econom Class", "Запорожье", "БУЛ. ЦЕНТРАЛЬНЫЙ, 7", "Tuesday"));
        addShop(new Shop("Econom Class", "Запорожье", "УЛ. ЛАХТИНСКАЯ, 13", "Tuesday"));
        addShop(new Shop("Econom Class", "Запорожье", "ПР. МЕТАЛЛУРГОВ, 1", "Monday"));
        addShop(new Shop("Econom Class", "Запорожье", "ПР. МОТОРОСТРОИТЕЛЕЙ, 11", "Monday"));
        addShop(new Shop("Massa", "Запорожье", "УЛ. ПОЧТОВАЯ, 71", "Thursday"));
        addShop(new Shop("Massa", "Запорожье", "УЛ. ПОБЕДЫ, 62", "All"));
        addShop(new Shop("Massa", "Запорожье", "ПР. ЮБИЛЕЙНЫЙ, 22Б", "All"));
        addShop(new Shop("Massa", "Запорожье", "ПР. МЕТАЛЛУРГОВ, 8Б", "All"));
        addShop(new Shop("Massa", "Запорожье", "УЛ. НОВОКУЗНЕЦКАЯ, 12В", "All"));
        addShop(new Shop("Шиворот-Навыворот", "Запорожье", "Хортицкое шоссе, 30а", "Friday"));
        addShop(new Shop("Шиворот-Навыворот", "Запорожье", "ул. 40 лет Советской Украины, 57в", "Wednesday"));
        addShop(new Shop("Шиворот-Навыворот", "Запорожье", "ЖУКОВСКОГО, 32", "Tuesday"));
        addShop(new Shop("Шиворот-Навыворот", "Запорожье", "НОВОКУЗНЕЦКАЯ, 12В", "Thursday"));
        addShop(new Shop("Шиворот-Навыворот", "Запорожье", "ГРЯЗНОВА, 6", "Wednesday"));
        addShop(new Shop("Humana", "Запорожье", "Просп. Соборный, 171", "All"));
        addShop(new Shop("Humana", "Запорожье", "Просп. Соборный, 53", "All"));
        addShop(new Shop("Humana", "Запорожье", "Ул. И. Сытова, 4", "All"));
        addShop(new Shop("Humana", "Запорожье", "Ул. Ладожская, 26", "All"));
        addShop(new Shop("Second City", "Запорожье", "Ул. Воронина, 9/12", "Friday"));

    }//Адрес:	ул. Димитрова, 48
    //Тел.:	(0612) 65 3023
}
