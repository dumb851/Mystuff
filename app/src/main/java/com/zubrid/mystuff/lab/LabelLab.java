package com.zubrid.mystuff.lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zubrid.mystuff.database.BaseHelper;
import com.zubrid.mystuff.database.DbSchemas;
import com.zubrid.mystuff.database.MyCursorWrapper;
import com.zubrid.mystuff.model.Label;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class LabelLab {

    public static final String TAG = "LabelLab";
    private static LabelLab sLabelLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private LabelLab(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new BaseHelper(context).getWritableDatabase();

    }

    public static LabelLab get(Context context) {

        if (sLabelLab == null) {
            sLabelLab = new LabelLab(context);
        }
        return sLabelLab;
    }

    public ArrayList<Label> getLabels() {

        return getLabels(true);
    }

    public ArrayList<Label> getLabels(Boolean withSeparators) {
        int orderNumber = 0;

        ArrayList<Label> labels = new ArrayList<>();
        MyCursorWrapper cursor = queryLabels(null, null);
        String firstLetterOfLastLabel = "";

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                String firstLetter = "";

                Label label = cursor.getLabel();

                if (withSeparators) {
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

    private MyCursorWrapper queryLabels(String whereClause, String[] whereArgs) {

        String orderBy = DbSchemas.LabelsTable.Cols.TITLE;

        Cursor cursor = mDatabase.query(
                DbSchemas.LabelsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy
        );

        return new MyCursorWrapper(cursor);
    }

    public void addLabel(Label label) {

        ContentValues values = getContentValues(label);
        mDatabase.insert(DbSchemas.LabelsTable.NAME, null, values);
    }

    public void deleteLabel(Label label) {

        String uuidString = label.getId().toString();

        mDatabase.delete(DbSchemas.LabelsTable.NAME, DbSchemas.LabelsTable.Cols.UUID + " = ?",
                new String[]{uuidString});

    }

    private static ContentValues getContentValues(Label label) {

        ContentValues values = new ContentValues();
        values.put(DbSchemas.LabelsTable.Cols.UUID, label.getId().toString());
        values.put(DbSchemas.LabelsTable.Cols.TITLE, label.getTitle());
        values.put(DbSchemas.LabelsTable.Cols.DATE, new Date().getTime());

        return values;
    }

    public Label getLabel(UUID id) {

        MyCursorWrapper cursor = queryLabels(
                DbSchemas.LabelsTable.Cols.UUID + " =?",
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

        result = mDatabase.update(DbSchemas.LabelsTable.NAME, values,
                DbSchemas.LabelsTable.Cols.UUID + " = ?",
                new String[]{uuidString}
        );

        Log.i(TAG, "update result= " + result);
    }
}
