package com.zubrid.mystuff.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "myStuffBase.db";

    public BaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DbSchemas.ItemsTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                DbSchemas.ItemsTable.Cols.UUID   + ", " +
                DbSchemas.ItemsTable.Cols.TITLE  + ", " +
                DbSchemas.ItemsTable.Cols.DATE   + "" +
                DbSchemas.ItemsTable.Cols.DELETION_MARK   + "" +
                ")"
        );

        db.execSQL("create table " + DbSchemas.LabelsTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                DbSchemas.LabelsTable.Cols.UUID   + ", " +
                DbSchemas.LabelsTable.Cols.TITLE  + ", " +
                DbSchemas.LabelsTable.Cols.DATE   + "" +
                ")"
        );

        db.execSQL("create table " + DbSchemas.ItemLabelsTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                DbSchemas.ItemLabelsTable.Cols.UUID_ITEM   + ", " +
                DbSchemas.ItemLabelsTable.Cols.UUID_LABEL  + "" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
