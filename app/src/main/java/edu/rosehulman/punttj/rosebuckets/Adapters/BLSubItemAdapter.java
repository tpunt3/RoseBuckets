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
import edu.rosehulman.punttj.rosebuckets.fragments.BucketListSubItemFragment;
import edu.rosehulman.punttj.rosebuckets.model.SubItem;

/**
 * Created by alangavr on 1/22/2017.
 */

public class BLSubItemAdapter extends RecyclerView.Adapter<BLSubItemAdapter.ViewHolder> {

    private List<SubItem> mSubItems;
    private DatabaseReference mSubItemRef;
    private BucketListSubItemFragment.OnSubItemSelectedListener mListener;
    private Context mContext;
    private String parentUid;
    private DatabaseReference titleRef;
    private TextView mTitleText;

    public BLSubItemAdapter(BucketListSubItemFragment.OnSubItemSelectedListener listener, Context context) {
        mSubItems = new ArrayList<>();
        mSubItemRef = FirebaseDatabase.getInstance().getReference().child("subItems");

        parentUid = SharedPreferencesUtils.getCurrentBucketListItem(context);

        Query query = mSubItemRef.orderByChild("uid").equalTo(parentUid);
        query.addChildEventListener(new SubItemChildEventListener());

        mListener = listener;
        mContext = context;

        titleRef = FirebaseDatabase.getInstance().getReference().child("items");
        titleRef.addListenerForSingleValueEvent(new NameEventListener());

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_sub_item_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SubItem current = mSubItems.get(position);
        holder.mCheckBox.setText(current.getTitle());

    }

    public void setTitleText(TextView titleText){
        mTitleText = titleText;
    }

    public void addItem(SubItem item) {
        DatabaseReference newItem = mSubItemRef.push();
        item.setKey(newItem.getKey());
        newItem.setValue(item);
    }

    void removeItem(SubItem item){
        mSubItemRef.child(item.getKey()).removeValue();
    }

    public void addEditBucketListSubItem(final SubItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null, false);
        final EditText blEditText = (EditText) view.findViewById(R.id.dialog_edit_text);

        if(item !=  null){
            blEditText.setText(item.getTitle());
            builder.setTitle(R.string.edit_bucket_list);
        }else{
            builder.setTitle(R.string.create_sub_item);
        }

        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(item != null){
                    //todo update
                    item.setTitle(blEditText.getText().toString());
                }else{
                    SubItem newItem = new SubItem(blEditText.getText().toString());
                    newItem.setUid(parentUid);
                    newItem.setComments("");
                    newItem.setPath("");
                    newItem.setKey("");
                    addItem(newItem);
                }


                notifyDataSetChanged();
            }
        });
        builder.setNeutralButton(R.string.edit_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeItem(item);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }


    @Override
    public int getItemCount() {
        return mSubItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            mCheckBox = (CheckBox) itemView.findViewById(R.id.SIcheckBox);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onSubItemSelected(mSubItems.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                addEditBucketListSubItem(mSubItems.get(getAdapterPosition()));
                return true;
                }
            });
        }
    }

    class SubItemChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            SubItem item = dataSnapshot.getValue(SubItem.class);
            item.setKey(dataSnapshot.getKey());
            item.setUid(parentUid);
            mSubItems.add(0, item);
            notifyItemInserted(0);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            SubItem updated = dataSnapshot.getValue(SubItem.class);
            for(SubItem item: mSubItems){
                if(item.getKey().equals(key)){
                    item.update(updated);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            for(SubItem item: mSubItems){
                if(item.getKey().equals(dataSnapshot.getKey())){
                    mSubItems.remove(item);
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

    class NameEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snap : dataSnapshot.getChildren()){
                if (snap.getKey().equals(parentUid)){
                    mTitleText.setText(snap.child("name").getValue(String.class));
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
