package com.example.shopmanage.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.shopmanage.model.SignUpUser;

import java.util.ArrayList;
import java.util.List;

public class UserDAO extends SQLiteOpenHelper {
    public Context context;

    private static final String TAG = "datauser";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String USER = "user";
    private static final String PASS = "pass";
    private static final String DATANAME = "signupdata";
    private static final String TABLENAME = "tablename";

    public UserDAO(Context context) {
        super(context, DATANAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String table = " create table " + TABLENAME + "(" + ID + " integer primary key, " + NAME +
                " text not null, " + EMAIL + " text not null, " + USER + " text not null, " + PASS + " text not null )"
                ;
        sqLiteDatabase.execSQL(table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" drop table if exists " + TABLENAME);
        onCreate(sqLiteDatabase);
    }

    public int add(SignUpUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, user.getFullname());
        contentValues.put(EMAIL, user.getEmail());
        contentValues.put(USER, user.getUser());
        contentValues.put(PASS, user.getPass());

        try {
            if ( db.insert(TABLENAME, null, contentValues) == -1){
                return -1;

            }
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }


        db.close();
        return 1;
    }

    public List<SignUpUser> getAll() {
        List<SignUpUser> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = " select * from " + TABLENAME;
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                SignUpUser signUpUser = new SignUpUser();
                signUpUser.setId(Integer.parseInt(cursor.getString(0)));
                signUpUser.setFullname(cursor.getString(1));

                signUpUser.setEmail(cursor.getString(2));
                signUpUser.setUser(cursor.getString(3));
                signUpUser.setPass(cursor.getString(4));

                list.add(signUpUser);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean checkUser(String users, String passs) {
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(" select * from tablename where user = '" + users + "' and pass = '" + passs + "'", null);
        Cursor cursor = db.rawQuery(" select * from tablename where user = '" + users + "' and pass = '" + passs + "'", null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            db.close();
            return false;
        } else {
            cursor.close();
            db.close();
            return true;
        }
    }

    // update
    public int updateHandler(SignUpUser upUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, upUser.getFullname());
        values.put(EMAIL, upUser.getEmail());
        return db.update(TABLENAME, values, ID + " = ? ", new String[]{String.valueOf(upUser.getId())});
    }

    public int delete(SignUpUser upUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLENAME, ID + " = ? ", new String[]{String.valueOf(upUser.getId())});

    }

    public int doiMatK(SignUpUser signUpUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PASS, signUpUser.getPass());
        return (int) db.update(TABLENAME, values,USER + " = ? ", new String[]{String.valueOf(signUpUser.getUser())});
    }
    public boolean check(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLENAME,null,USER + " = ? ",new String[]{String.valueOf(user)},null,null,null);
            cursor.moveToFirst();
            int i = cursor.getCount();
            cursor.close();
            if (i <= 0){
                return false;
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
