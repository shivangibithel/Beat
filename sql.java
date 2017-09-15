package com.example.shivangibithel.beat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
public class sql extends SQLiteOpenHelper {

    public sql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
public void querydata(String sql)
{
    SQLiteDatabase db=getWritableDatabase();
    db.execSQL(sql);
}

public void insert(String t,byte [] g)
{
    SQLiteDatabase db=getWritableDatabase();
    String  sq="INSERT INTO t1 VALUES(null,?,?)";
    SQLiteStatement statement =db.compileStatement(sq);
    statement.clearBindings();
    statement.bindString(1,t);
    statement.bindBlob(2,g);
    statement.executeInsert();
}
public Cursor getdata(String sql)
{
    SQLiteDatabase db=getReadableDatabase();
    return db.rawQuery(sql,null);

}
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
