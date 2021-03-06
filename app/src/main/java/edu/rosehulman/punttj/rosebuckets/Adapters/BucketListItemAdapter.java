package edu.rosehulman.punttj.rosebuckets.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.punttj.rosebuckets.R;
import edu.rosehulman.punttj.rosebuckets.SharedPreferencesUtils;
import edu.rosehulman.punttj.rosebuckets.fragments.BucketListItemFragment;
import edu.rosehulman.punttj.rosebuckets.model.BucketListItem;

import static android.R.attr.data;
import static android.R.attr.key;

/**
 * Created by punttj on 1/16/2017.
 */

public class BucketListItemAdapter extends RecyclerView.Adapter<BucketListItemAdapter.ViewHolder>{

    private List<BucketListItem> mBucketListItems;
    private BucketListItemFragment.OnBLItemSelectedListener mListener;
    private Context mContext;
    private DatabaseReference mItemRef;
    private String mUid;
    private DatabaseReference titleRef;
    private TextView mTitleText;

    public BucketListItemAdapter(BucketListItemFragment.OnBLItemSelectedListener listener, Context context) {
        mListener = listener;
        mContext = context;

        mBucketListItems = new ArrayList<>();
        mItemRef = FirebaseDatabase.getInstance().getReference().child("items");

        titleRef = FirebaseDatabase.getInstance().getReference().child("bucketLists");

        mUid = SharedPreferencesUtils.getCurrentBucketList(context);

        Query query = mItemRef.orderByChild("uid").equalTo(mUid);
        query.addChildEventListener(new ItemChildEventListener());

        titleRef.addListenerForSingleValueEvent(new NameEventListener());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCheckbox.setText(mBucketListItems.get(position).getName());
        holder.mCheckbox.setChecked(mBucketListItems.get(position).isCompleted());
    }

    public void setTitleText(TextView titleText){
        mTitleText = titleText;
    }

    public void addItem(BucketListItem item){
        mItemRef.push().setValue(item);
    }

    void remove(BucketListItem item){
        mItemRef.child(item.getKey()).removeValue();
    }

    public void addEditBucketListItem(final BucketListItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null, false);
        final EditText blEditText = (EditText) view.findViewById(R.id.dialog_edit_text);

        if(item != null){
            blEditText.setText(item.getName());
            builder.setTitle(R.string.edit_bucket_list);
        }else{
            builder.setTitle(R.string.create_new_item);
        }

        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(item != null){
                    item.setName(blEditText.getText().toString());
                    mItemRef.child(item.getKey()).setValue(item);
                }else{
                    BucketListItem newItem = new BucketListItem(blEditText.getText().toString());
                    newItem.setUid(mUid);
                    addItem(newItem);
                }
                notifyDataSetChanged();
            }
        });

        if(item != null) {
            builder.setNeutralButton(R.string.edit_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    remove(item);
                }
            });
        }
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return mBucketListItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mCheckbox;
        private BucketListItem item;

        public ViewHolder(View itemView) {
            super(itemView);

            mCheckbox = (CheckBox) itemView.findViewById(R.id.checkBox);

            mCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item = mBucketListItems.get(getAdapterPosition());
                    if (mCheckbox.isChecked()){
                        item.setCompleted(true);
                    }else{
                        item.setCompleted(false);
                    }
                    mItemRef.child(item.getKey()).setValue(item);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item = mBucketListItems.get(getAdapterPosition());
                    //create new bucket list item fragment
                    mListener.onBLItemSelected(item);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    item = mBucketListItems.get(getAdapterPosition());
                    addEditBucketListItem(item);
                    return true;
                }
            });
        }
    }

    class ItemChildEventListener implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            BucketListItem item = dataSnapshot.getValue(BucketListItem.class);
            item.setKey(dataSnapshot.getKey());
            item.setUid(mUid);
            mBucketListItems.add(0, item);
            notifyItemInserted(0);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            BucketListItem updated = dataSnapshot.getValue(BucketListItem.class);
            for(BucketListItem item: mBucketListItems){
                if(item.getKey().equals(key)){
                    item.setName(updated.getName());
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            for(BucketListItem item: mBucketListItems){
                if(item.getKey().equals(dataSnapshot.getKey())){
                    mBucketListItems.remove(item);
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

    class NameEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot snap : dataSnapshot.getChildren()){
                if (snap.getKey().equals(mUid)){
                    mTitleText.setText(snap.child("name").getValue(String.class));
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
