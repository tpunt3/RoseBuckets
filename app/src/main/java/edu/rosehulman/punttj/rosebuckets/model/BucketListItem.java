package edu.rosehulman.punttj.rosebuckets.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alangavr on 1/15/2017.
 */

public class BucketListItem implements Parcelable {

    private List<SubItem> subItems;
    private String name;
    private boolean completed;
    private float percentCompleted;
    private String key;
    private String uid;

    public BucketListItem  () {
        this.subItems = new ArrayList<SubItem>();
    }

    public BucketListItem (String text) {
        this.name = text;
        this.subItems = new ArrayList<SubItem>();
    }

    protected BucketListItem(Parcel in) {
        subItems = in.createTypedArrayList(SubItem.CREATOR);
        name = in.readString();
        completed = in.readByte() != 0;
        percentCompleted = in.readFloat();
        key = in.readString();
        uid = in.readString();
    }

    public static final Creator<BucketListItem> CREATOR = new Creator<BucketListItem>() {
        @Override
        public BucketListItem createFromParcel(Parcel in) {
            return new BucketListItem(in);
        }

        @Override
        public BucketListItem[] newArray(int size) {
            return new BucketListItem[size];
        }
    };

    public void completed() {
        this.completed = true;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public List<SubItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<SubItem> subItems) {
        this.subItems = subItems;
    }

    public void addSubItem(SubItem s) {
        subItems.add(s);
    }


    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public float getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(float percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(subItems);
        parcel.writeString(name);
        parcel.writeByte((byte) (completed ? 1 : 0));
        parcel.writeFloat(percentCompleted);
        parcel.writeString(key);
        parcel.writeString(uid);
    }
}
