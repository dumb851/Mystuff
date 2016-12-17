package com.zubrid.mystuff.model;

import java.util.UUID;

public class Image {

    private UUID mUUID;
    private UUID mOwnerUUID;
    private boolean mDeletionMark;

    public Image(UUID UUID) {
        mUUID = UUID;
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
