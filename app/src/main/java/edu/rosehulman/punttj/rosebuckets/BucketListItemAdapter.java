package edu.rosehulman.punttj.rosebuckets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.punttj.rosebuckets.model.BucketListItem;

/**
 * Created by punttj on 1/16/2017.
 */

public class BucketListItemAdapter extends RecyclerView.Adapter<BucketListItemAdapter.ViewHolder>{

    private List<BucketListItem> mBucketListItems;

    private DatabaseReference mFirebase;

    public BucketListItemAdapter() {
        mBucketListItems = new ArrayList<>();
        mFirebase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCheckbox.setText(mBucketListItems.get(position).getmText());
    }

    public void addItem(String text){
        mBucketListItems.add(new BucketListItem(text));
    }

    @Override
    public int getItemCount() {
        return mBucketListItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mCheckbox;

        public ViewHolder(View itemView) {
            super(itemView);

            mCheckbox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
}
