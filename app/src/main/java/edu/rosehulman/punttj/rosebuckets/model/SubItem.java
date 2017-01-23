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
    }
}
