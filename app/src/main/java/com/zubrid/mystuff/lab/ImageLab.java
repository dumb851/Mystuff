package com.zubrid.mystuff.lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zubrid.mystuff.database.BaseHelper;
import com.zubrid.mystuff.database.DbSchemas;
import com.zubrid.mystuff.model.Image;

import java.util.ArrayList;
import java.util.UUID;

public class ImageLab {

    private static final String TAG = "ImageLab";
    private static ImageLab sImageLab;

    private SQLiteDatabase mDatabase;

    public ImageLab(Context context) {

        mDatabase = new BaseHelper(context).getWritableDatabase();

    }

    private static ContentValues getContentValues(Image image) {

        ContentValues values = new ContentValues();
        values.put(DbSchemas.ImagesTable.Cols.UUID,             image.getId().toString());
        values.put(DbSchemas.ImagesTable.Cols.UUID_OWNER,       image.getOwnerId().toString());
        values.put(DbSchemas.ImagesTable.Cols.DELETION_MARK,    image.getDeletionMark());

        return values;
    }

    public ArrayList<UUID> getImagesByOwner(UUID ownerUUID) {

        return null;
    }

    public boolean addImage(UUID ownerUUID, UUID imageUUID) {


        return true;
    }

}
