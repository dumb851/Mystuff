package com.zubrid.mystuff.lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zubrid.mystuff.database.BaseHelper;
import com.zubrid.mystuff.database.DbSchemas;
import com.zubrid.mystuff.database.ItemsCursorWrapper;
import com.zubrid.mystuff.model.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ItemLab {

    public static final String TAG = "ItemLab";
    private static ItemLab sItemLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ItemLab(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new BaseHelper(context).getWritableDatabase();


    }

    public static ItemLab get(Context context) {

        if (sItemLab == null) {
            sItemLab = new ItemLab(context);
        }
        return sItemLab;
    }

    public ArrayList<Item> getItems() {

        int orderNumber = 0;

        ArrayList<Item> items = new ArrayList<>();

        ItemsCursorWrapper cursor = queryItems(null, null);

        String firstLetterOfLastItem  = "";

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                String firstLetter = "";

                Item item = cursor.getItem();

                String title = item.getTitle();
                title = (title == null) ? "" : title;

                if (title.length() > 0) {
                    firstLetter = String.valueOf(title.charAt(0));
                }

                if (!firstLetter.equalsIgnoreCase(firstLetterOfLastItem)) {
                    Item separator = new Item();
                    separator.setTitle(firstLetter.toUpperCase());
                    separator.setIsSeparator(true);
                    separator.setOrderNumber(orderNumber++);

                    items.add(separator);
                    firstLetterOfLastItem = firstLetter;
                }

                item.setOrderNumber(orderNumber++);
                items.add(item);


                cursor.moveToNext();

            }

        } finally {
            cursor.close();
        }

        return items;
    }

    public ArrayList<Item> getWithoutSeparatorsItems() {

        int orderNumber = 0;

        ArrayList<Item> items = new ArrayList<>();
        ItemsCursorWrapper cursor = queryItems(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                Item item = cursor.getItem();
                item.setOrderNumber(orderNumber++);
                items.add(item);

                cursor.moveToNext();

            }

        } finally {
            cursor.close();
        }

        return items;
    }

    private ItemsCursorWrapper queryItems(String whereClause, String[] whereArgs) {

        String orderBy = DbSchemas.ItemsTable.Cols.TITLE;

        Cursor cursor = mDatabase.query(
                DbSchemas.ItemsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy
        );

        return new ItemsCursorWrapper(cursor);
    }

    public void addItem(Item item) {

        ContentValues values = getContentValues(item);
        mDatabase.insert(DbSchemas.ItemsTable.NAME, null, values);
    }

    public void deleteItem(Item item) {

        deleteItemByUUID(item.getId());

    }

    public void deleteItemByUUID(UUID uuid) {

        String uuidString = uuid.toString();

        mDatabase.delete(DbSchemas.ItemsTable.NAME, DbSchemas.ItemsTable.Cols.UUID + " = ?",
                new String[]{uuidString});

    }

    private static ContentValues getContentValues(Item item) {

        ContentValues values = new ContentValues();
        values.put(DbSchemas.ItemsTable.Cols.UUID, item.getId().toString());
        values.put(DbSchemas.ItemsTable.Cols.TITLE, item.getTitle());
        values.put(DbSchemas.ItemsTable.Cols.DATE, new Date().getTime());

        return values;
    }

    public Item getItem(UUID id) {

        ItemsCursorWrapper cursor = queryItems(
                DbSchemas.ItemsTable.Cols.UUID + " =?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getItem();

        } finally {
            cursor.close();
        }

    }

    public void updateItem(Item item) {

        String uuidString = item.getId().toString();
        ContentValues values = getContentValues(item);

        int result;

        result = mDatabase.update(DbSchemas.ItemsTable.NAME, values,
                DbSchemas.ItemsTable.Cols.UUID + " = ?",
                new String[]{uuidString}
        );

        Log.i(TAG, "update result= " + result);
    }

    public void moveItemToTrash(Item item) {

        item.setDeletionMark(true);
        updateItem(item);

    }

}
