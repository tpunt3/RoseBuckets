package edu.rosehulman.punttj.rosebuckets.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alangavr on 1/15/2017.
 */

public class BucketList implements Parcelable{

    private String key;
    private String uid;
    private String name;
    private List<BucketListItem> items;
    private boolean completed;
    private float percentCompleted;

    public BucketList(){

    }

    public BucketList(String name) {
        this.name = name;
        items = new ArrayList<>();
    }

    protected BucketList(Parcel in) {
        key = in.readString();
        uid = in.readString();
        name = in.readString();
        completed = in.readByte() != 0;
        percentCompleted = in.readFloat();
    }

    public static final Creator<BucketList> CREATOR = new Creator<BucketList>() {
        @Override
        public BucketList createFromParcel(Parcel in) {
            return new BucketList(in);
        }

        @Override
        public BucketList[] newArray(int size) {
            return new BucketList[size];
        }
    };

    public void setUid(String uid){
        this.uid = uid;
    }
    public String getUid(){
        return this.uid;
    }

    public String getKey(){
        return this.key;
    }
    public void setKey(String key){
        this.key = key;
    }

    public void completed() {
        this.completed = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BucketListItem> getItems() {
        return items;
    }

    public void setItems(List<BucketListItem> items) {
        this.items = items;
    }


    public void addItems(BucketListItem b) {
        items.add(b);
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
        parcel.writeString(key);
        parcel.writeString(uid);
        parcel.writeString(name);
        parcel.writeByte((byte) (completed ? 1 : 0));
        parcel.writeFloat(percentCompleted);
    }
}
