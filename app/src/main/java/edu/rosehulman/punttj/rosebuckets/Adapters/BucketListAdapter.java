package edu.rosehulman.punttj.rosebuckets.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.punttj.rosebuckets.R;
import edu.rosehulman.punttj.rosebuckets.SharedPreferencesUtils;
import edu.rosehulman.punttj.rosebuckets.fragments.BucketListFragment;
import edu.rosehulman.punttj.rosebuckets.model.BucketList;

import static edu.rosehulman.punttj.rosebuckets.R.id.bucketList;

/**
 * Created by punttj on 1/16/2017.
 */

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    private BucketListFragment.OnBLSelectedListener mListener;
    private Context mContext;
    private List<BucketList> mBucketLists;
    private DatabaseReference mBucketListRef;
    private String mUid;

    public BucketListAdapter(BucketListFragment.OnBLSelectedListener listener, Context context) {
        mListener = listener;
        mContext = context;
        mBucketLists = new ArrayList<>();

        mUid = SharedPreferencesUtils.getCurrentUser(mContext);

        mBucketListRef = FirebaseDatabase.getInstance().getReference().child("bucketLists");

        Query query = mBucketListRef.orderByChild("uid").equalTo(mUid);
        query.addChildEventListener(new BucketListChildEventListener());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mBucketLists.get(position).getName());
    }

    public void addBucketList(BucketList bucketList) {
        mBucketListRef.push().setValue(bucketList);
    }

    public void removeBucketList(BucketList bucketList){
        if(mUid.equals(bucketList.getUid())){
            mBucketListRef.child(bucketList.getKey()).removeValue();
        }
    }

    public void addEditBucketList(final BucketList bl){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null, false);
        final EditText blEditText = (EditText) view.findViewById(R.id.dialog_edit_text);

        if(bl != null){
            blEditText.setText(bl.getName());
            builder.setTitle(R.string.edit_bucket_list);
        }else{
            builder.setTitle(R.string.create_new_bl);
        }

        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(bl != null){
                    bl.setName(blEditText.getText().toString());
                }else{
                    BucketList bucketList = new BucketList(blEditText.getText().toString());
                    bucketList.setUid(mUid);
                    addBucketList(bucketList);
                }
                notifyDataSetChanged();
            }
        });

        if(bl != null) {
            builder.setNeutralButton(R.string.edit_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    removeBucketList(bl);
                }
            });
        }
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return mBucketLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(bucketList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create new bucket list item fragment
                    mListener.onBLSelected(mBucketLists.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    addEditBucketList(mBucketLists.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    class BucketListChildEventListener implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            BucketList bucket = dataSnapshot.getValue(BucketList.class);
            bucket.setKey(dataSnapshot.getKey());
            bucket.setUid(mUid);
            mBucketLists.add(0, bucket);
            notifyItemInserted(0);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            BucketList updated = dataSnapshot.getValue(BucketList.class);
            for(BucketList bl: mBucketLists){
                if(bl.getKey().equals(key)){
                    bl.setName(updated.getName());
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            for(BucketList bl: mBucketLists){
                if(bl.getKey().equals(dataSnapshot.getKey())){
                    mBucketLists.remove(bl);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
