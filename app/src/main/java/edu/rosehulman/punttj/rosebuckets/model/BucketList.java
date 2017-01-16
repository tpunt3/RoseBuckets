package edu.rosehulman.punttj.rosebuckets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alangavr on 1/15/2017.
 */

public class BucketList {

    private String name;
    private List<BucketListItem> items;
    private boolean completed;
    private float percentCompleted;

    public BucketList(String name) {
        this.name = name;
        items = new ArrayList<>();
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
}
