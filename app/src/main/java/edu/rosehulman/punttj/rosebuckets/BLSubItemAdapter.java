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

import edu.rosehulman.punttj.rosebuckets.fragments.BucketListSubItemFragment;
import edu.rosehulman.punttj.rosebuckets.model.BucketListItem;
import edu.rosehulman.punttj.rosebuckets.model.SubItem;

/**
 * Created by alangavr on 1/22/2017.
 */

public class BLSubItemAdapter extends RecyclerView.Adapter<BLSubItemAdapter.ViewHolder> {

    private List<SubItem> mSubItems;
    private DatabaseReference mFirebase;
    private BucketListSubItemFragment.OnSubItemSelectedListener mListener;
    private Context mContext;

    public BLSubItemAdapter(BucketListSubItemFragment.OnSubItemSelectedListener listener, Context context) {
        mSubItems = new ArrayList<>();
        mFirebase = FirebaseDatabase.getInstance().getReference();
        mListener = listener;
        mContext = context;
        addItem("sub item!");

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

    public void addItem(String title) {
        mSubItems.add(new SubItem(title));
    }

    void editBucketListSubItem(final int position){
        final SubItem item = mSubItems.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null, false);
        final EditText blEditText = (EditText) view.findViewById(R.id.dialog_edit_text);
        blEditText.setText(item.getTitle());
        builder.setTitle(R.string.edit_bucket_list);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setTitle(blEditText.getText().toString());
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
                    editBucketListSubItem(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
