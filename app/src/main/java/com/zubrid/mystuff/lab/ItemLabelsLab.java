package com.zubrid.mystuff.lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zubrid.mystuff.database.BaseHelper;
import com.zubrid.mystuff.database.DbSchemas;
import com.zubrid.mystuff.database.MyCursorWrapper;
import com.zubrid.mystuff.model.ItemStuff;
import com.zubrid.mystuff.model.ItemLabel;
import com.zubrid.mystuff.model.Label;

import java.util.ArrayList;
import java.util.UUID;

public class ItemLabelsLab {

    public static final String TAG = "LabelLab";
    private static ItemLabelsLab sItemLabelsLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ItemLabelsLab(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new BaseHelper(context).getWritableDatabase();

    }

    public static ItemLabelsLab get(Context context) {

        if (sItemLabelsLab == null) {
            sItemLabelsLab = new ItemLabelsLab(context);
        }
        return sItemLabelsLab;
    }

    public ArrayList<ItemLabel> getLabelsByItem(ItemStuff itemStuff) {

        int orderNumber = 0;

        ArrayList<ItemLabel> itemLabels = new ArrayList<>();
        String whereClause = DbSchemas.ItemLabelsTable.Cols.UUID_ITEM + " = ?";

        String[] whereArgs = {itemStuff.getId().toString()};

        MyCursorWrapper cursor = queryLabels(whereClause, whereArgs);


        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                ItemLabel itemLabel = cursor.getItemWithLabel();

                itemLabels.add(itemLabel);

                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        return itemLabels;
    }

    private MyCursorWrapper queryLabels(String whereClause, String[] whereArgs) {

        //!String orderBy = DbSchemas.ItemLabelsTable.Cols.TITLE;
        //!whereClause = "";
        Cursor cursor = mDatabase.query(
                DbSchemas.ItemLabelsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                ""//!orderBy
        );

        return new MyCursorWrapper(cursor);
    }

    public void addLabelToItem(Label label, ItemStuff itemStuff) {

        ContentValues values = getContentValues(label, itemStuff);
        mDatabase.insert(DbSchemas.ItemLabelsTable.NAME, null, values);
    }

    public void deleteLabelFromItem(Label label, ItemStuff itemStuff) {

        String whereClause = DbSchemas.ItemLabelsTable.Cols.UUID_LABEL + " = ? AND "
                + DbSchemas.ItemLabelsTable.Cols.UUID_ITEM + " = ?";

        String uuidLabel = label.getId().toString();
        String uuidItem = itemStuff.getId().toString();

        mDatabase.delete(DbSchemas.ItemLabelsTable.NAME, whereClause,
                new String[]{uuidLabel, uuidItem});

    }

    private static ContentValues getContentValues(Label label, ItemStuff itemStuff) {

        ContentValues values = new ContentValues();
        values.put(DbSchemas.ItemLabelsTable.Cols.UUID_LABEL, label.getId().toString());
        values.put(DbSchemas.ItemLabelsTable.Cols.UUID_ITEM, itemStuff.getId().toString());

        return values;
    }

    public Label getLabel(UUID id) {

//!        LabelsCursorWrapper cursor = queryLabels(
//                DbSchemas.ItemLabelsTable.Cols.UUID + " =?",
//                new String[]{id.toString()}
//        );
//
//        try {
//            if (cursor.getCount() == 0) {
//                return null;
//            }
//
//            cursor.moveToFirst();
//            return cursor.getLabel();
//
//        } finally {
//            cursor.close();
//        }

        return null;
    }

    public void updateLabel(Label label) {

//!        String uuidString = label.getId().toString();
//        ContentValues values = getContentValues(label);
//
//        int result;
//
//        result = mDatabase.update(DbSchemas.ItemLabelsTable.NAME, values,
//                DbSchemas.ItemLabelsTable.Cols.UUID + " = ?",
//                new String[]{uuidString}
//        );
//
//        Log.i(TAG, "update result= " + result);
    }
}
