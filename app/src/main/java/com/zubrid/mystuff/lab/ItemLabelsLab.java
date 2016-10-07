package com.zubrid.mystuff.lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zubrid.mystuff.database.BaseHelper;
import com.zubrid.mystuff.database.DbSchemas;
import com.zubrid.mystuff.database.LabelsCursorWrapper;
import com.zubrid.mystuff.model.Item;
import com.zubrid.mystuff.model.Label;

import java.util.ArrayList;
import java.util.Date;
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

    public ArrayList<Label> getLabels() {

        int orderNumber = 0;

        ArrayList<Label> labels = new ArrayList<>();
        LabelsCursorWrapper cursor = queryLabels(null, null);
        String firstLetterOfLastLabel  = "";

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                String firstLetter = "";

                Label label = cursor.getLabel();

                String title = label.getTitle();
                title = (title == null) ? "" : title;

                if (title.length() > 0) {
                    firstLetter = String.valueOf(title.charAt(0));
                }

                if (!firstLetter.equalsIgnoreCase(firstLetterOfLastLabel)) {
                    Label separator = new Label();
                    separator.setTitle(firstLetter.toUpperCase());
                    separator.setIsSeparator(true);
                    separator.setOrderNumber(orderNumber++);

                    labels.add(separator);
                    firstLetterOfLastLabel = firstLetter;
                }

                label.setOrderNumber(orderNumber++);
                labels.add(label);

                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        return labels;
    }

    private LabelsCursorWrapper queryLabels(String whereClause, String[] whereArgs) {

        //!String orderBy = DbSchemas.ItemLabelsTable.Cols.TITLE;

        Cursor cursor = mDatabase.query(
                DbSchemas.ItemLabelsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                ""//!orderBy
        );

        return new LabelsCursorWrapper(cursor);
    }

    public void addLabelToItem(Label label, Item item) {

        ContentValues values = getContentValues(label, item); //TODO here
        mDatabase.insert(DbSchemas.ItemLabelsTable.NAME, null, values);
    }

    public void deleteLabelFromItem(Label label, Item item) {

        //TODO
        String uuidString = label.getId().toString();

        mDatabase.delete(DbSchemas.ItemLabelsTable.NAME, DbSchemas.ItemLabelsTable.Cols.UUID + " = ?",
                new String[]{uuidString});

    }

    private static ContentValues getContentValues(Label label, Item item) {

        ContentValues values = new ContentValues();
        values.put(DbSchemas.ItemLabelsTable.Cols.UUID_LABEL, label.getId().toString());
        values.put(DbSchemas.ItemLabelsTable.Cols.UUID_ITEM, item.getId().toString());

        return values;
    }

    public Label getLabel(UUID id) {

        LabelsCursorWrapper cursor = queryLabels(
                DbSchemas.ItemLabelsTable.Cols.UUID + " =?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getLabel();

        } finally {
            cursor.close();
        }

    }

    public void updateLabel(Label label) {

        String uuidString = label.getId().toString();
        ContentValues values = getContentValues(label);

        int result;

        result = mDatabase.update(DbSchemas.ItemLabelsTable.NAME, values,
                DbSchemas.ItemLabelsTable.Cols.UUID + " = ?",
                new String[]{uuidString}
        );

        Log.i(TAG, "update result= " + result);
    }
}
