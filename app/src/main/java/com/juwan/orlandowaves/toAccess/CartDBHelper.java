package com.juwan.orlandowaves.toAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Juwan on 11/9/2017.
 */

public class CartDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "carts.db";
    public static final String TABLE_NAME = "games_cart";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_OPPONENT = "opponent";
    public static final String COL_DATE = "date";
    public static final String COL_TYPE = "type";
    public static final String COL_QUANTITY= "quantity";
    public static final String COL_LOCATION= "location";
    public static final String COL_EVENT = "event";
    public static final String COL_PRICE= "price";
    public static final String COL_fPRICE= "finalprice";
    public static final String COL_TIPOFF= "tipoff";
    public static final String COL_URL= "url";
    public static final String SINLGE= "Single Game Ticket";
    public static final String MERCH= "Merchandise";
    public static final String SEASON= "Season Pass";

    public CartDBHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }
//name + event=description + price + quantity + type

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,opponent TEXT, date TEXT, location TEXT,type TEXT, event TEXT, tipoff TEXT, quantity TEXT, price TEXT, finalprice TEXT, url TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean AddToCart(games game,String type, String price, String quantity) {
        String holder = price;
        price = price.replace("$","");
        Log.e(TAG, "PRICE*********: " + price);
        Double priceD = Double.parseDouble(price);
        int quantityI = Integer.parseInt(quantity);
        priceD = priceD * quantityI;
        String fPrice = "$" + priceD;
        price = holder;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME,SINLGE);
        contentValues.put(COL_OPPONENT,game.getOpponent());
        contentValues.put(COL_DATE,game.getDate());
        contentValues.put(COL_TYPE,type);
        contentValues.put(COL_TIPOFF,game.getTime());
        contentValues.put(COL_LOCATION,game.getLocation());
        contentValues.put(COL_QUANTITY, quantity);
        contentValues.put(COL_EVENT,game.getEvent());
        contentValues.put(COL_PRICE,price);
        contentValues.put(COL_fPRICE,fPrice);
        contentValues.put(COL_URL,game.getImage_url());
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String id,String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //math
        String justPrice = contentValues.getAsString(COL_PRICE);
        Cursor getPriceString = db.rawQuery("select price from "+TABLE_NAME+" where _id = "+id,null);
        getPriceString.moveToFirst();
        justPrice = getPriceString.getString((getPriceString.getColumnIndexOrThrow("price")));
        Log.e(TAG, "justPRICE*********: " + justPrice);
        justPrice = justPrice.replace("$","");
        Double priceD = Double.parseDouble(justPrice);
        int quantityI = Integer.parseInt(quantity);
        priceD = priceD * quantityI;
        String fPrice = "$" + priceD;
        //math
        contentValues.put(COL_QUANTITY,quantity);
        contentValues.put(COL_fPRICE,fPrice);
        int result = db.update(TABLE_NAME, contentValues, "_id = ?",new String[] { id });
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id = ?",new String[] {id});
    }
}
