package com.zubrid.mystuff.model;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.UUID;

public class Image {

    private UUID mUUID;
    private UUID mOwnerUUID;
    private boolean mDeletionMark;

    public Image() {
        this(UUID.randomUUID());
    }

    public Image(UUID UUID) {
        mUUID = UUID;
    }

    public String getImageFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public File getImageFile(Context context) {

        File externalFilesDir = context.getApplicationContext()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, getImageFileName());
    }

    public UUID getId() {
        return mUUID;
    }

    public UUID getOwnerId() {
        return mOwnerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        mOwnerUUID = ownerUUID;
    }

    public boolean isDeletionMark() {
        return mDeletionMark;
    }

    public void setDeletionMark(boolean deletionMark) {
        mDeletionMark = deletionMark;
    }

    public int getDeletionMark() {

        if (mDeletionMark) {
            return 1;
        }

        return 0;
    }
}
