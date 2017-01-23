package edu.rosehulman.punttj.rosebuckets.model;

import android.media.Image;

/**
 * Created by alangavr on 1/15/2017.
 */

public class SubItem {

    private String description;
    private Image picture;
    private String comments;

    public SubItem() {
        this.description = "no description";
        this.comments = "no comments";
    }

    public SubItem (String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
