package edu.rosehulman.punttj.rosebuckets.model;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alangavr on 1/15/2017.
 */

public class SubItem implements Parcelable {

    private String title;
    private Image picture;
    private String comments;
    private String uid;
    private String key;
    private String path;
    private boolean complete;

    public SubItem() {
        this.title = "no title";
        this.comments = "no comments";
    }

    public SubItem (String description){
        this.title = description;
    }


    protected SubItem(Parcel in) {
        title = in.readString();
        comments = in.readString();
        uid = in.readString();
        key = in.readString();
        complete = in.readByte() != 0;
    }

    public static final Creator<SubItem> CREATOR = new Creator<SubItem>() {
        @Override
        public SubItem createFromParcel(Parcel in) {
            return new SubItem(in);
        }

        @Override
        public SubItem[] newArray(int size) {
            return new SubItem[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(comments);
        parcel.writeByte((byte) (complete ? 1 : 0));
    }

    public void update(SubItem item){
        this.title = item.getTitle();
        this.comments = item.getComments();
        this.picture = item.getPicture();

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
