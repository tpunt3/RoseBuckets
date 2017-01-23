package edu.rosehulman.punttj.rosebuckets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.punttj.rosebuckets.fragments.BucketListItemFragment;
import edu.rosehulman.punttj.rosebuckets.model.BucketListItem;

/**
 * Created by punttj on 1/16/2017.
 */

public class BucketListItemAdapter extends RecyclerView.Adapter<BucketListItemAdapter.ViewHolder>{

    private List<BucketListItem> mBucketListItems;
    private BucketListItemFragment.OnBLItemSelectedListener mListener;
    private Context mContext;
    private DatabaseReference mFirebase;

    public BucketListItemAdapter(BucketListItemFragment.OnBLItemSelectedListener listener, Context context) {
        mListener = listener;
        mContext = context;

        mBucketListItems = new ArrayList<>();
        mFirebase = FirebaseDatabase.getInstance().getReference();

        addItem("BL Item");

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCheckbox.setText(mBucketListItems.get(position).getName());
    }

    public void addItem(String text){
        mBucketListItems.add(new BucketListItem(text));
    }

    void editBucketListItem(final int position){
        final BucketListItem item = mBucketListItems.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null, false);
        final EditText blEditText = (EditText) view.findViewById(R.id.dialog_edit_text);
        blEditText.setText(item.getName());
        builder.setTitle(R.string.edit_bucket_list);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setName(blEditText.getText().toString());
                notifyItemChanged(position);
            }
        });

        builder.setNeutralButton(R.string.edit_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create new bucket list item fragment
                    mListener.onBLItemSelected(mBucketListItems.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    editBucketListItem(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
