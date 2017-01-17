package edu.rosehulman.punttj.rosebuckets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.punttj.rosebuckets.model.BucketList;

import static android.R.attr.name;
import static android.media.CamcorderProfile.get;

/**
 * Created by punttj on 1/16/2017.
 */

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    private RecyclerView mRecyclerView;
    private List<BucketList> mBucketLists;

    public BucketListAdapter(RecyclerView recyclerView){
        mRecyclerView = recyclerView;
        mBucketLists = new ArrayList<>();

        addBucketList("test");

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mBucketLists.get(position).getName());
    }

    public void addBucketList(String name){
        mBucketLists.add(mBucketLists.size(), new BucketList(name));
        notifyItemInserted(mBucketLists.size());
    }

    @Override
    public int getItemCount() {
        return mBucketLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.bucketList);
        }
    }
}
