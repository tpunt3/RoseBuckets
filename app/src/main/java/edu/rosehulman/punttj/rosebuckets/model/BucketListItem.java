package edu.rosehulman.punttj.rosebuckets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alangavr on 1/15/2017.
 */

public class BucketListItem {

    private List<SubItem> subItems;
    private boolean completed;
    private float percentCompleted;

    public BucketListItem  () {
        this.subItems = new ArrayList<SubItem>();
    }

    public void completed() {
        this.completed = true;
    }


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
}
