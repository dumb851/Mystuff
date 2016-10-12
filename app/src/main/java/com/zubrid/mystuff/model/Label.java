package com.zubrid.mystuff.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Label implements Serializable {

    //!another some text
    private UUID mId;
    private String mTitle;
    private boolean mIsSeparator;
    private Date mLastSavedDate;
    private int orderNumber;

    public Label() {
        this(UUID.randomUUID());
    }

    public Label(UUID id) {
        mId = id;
        mLastSavedDate = new Date();
        mIsSeparator = false;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {

        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setLastSavedDate(Date changeDate) {
        mLastSavedDate = changeDate;
    }

    public void setIsSeparator(boolean isSeparator) {
        mIsSeparator = isSeparator;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public boolean isSeparator() {
        return mIsSeparator;
    }

    public Date getLastSavedDate() {
        return mLastSavedDate;
    }

}
