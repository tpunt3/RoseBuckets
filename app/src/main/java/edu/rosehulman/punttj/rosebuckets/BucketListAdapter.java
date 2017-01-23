package edu.rosehulman.punttj.rosebuckets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.punttj.rosebuckets.fragments.BucketListFragment;
import edu.rosehulman.punttj.rosebuckets.model.BucketList;

/**
 * Created by punttj on 1/16/2017.
 */

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    private BucketListFragment.OnBLSelectedListener mListener;
    private Context mContext;
    private List<BucketList> mBucketLists;

    public BucketListAdapter(BucketListFragment.OnBLSelectedListener listener, Context context) {
        mListener = listener;
        mContext = context;
        mBucketLists = new ArrayList<>();

        addBucketList("test");

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

    public void addBucketList(String name) {
        mBucketLists.add(mBucketLists.size(), new BucketList(name));
        notifyItemInserted(mBucketLists.size());
    }

    void editBucketList(final int position){
        final BucketList bl = mBucketLists.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null, false);
        final EditText blEditText = (EditText) view.findViewById(R.id.dialog_edit_text);
        blEditText.setText(bl.getName());
        builder.setTitle(R.string.edit_bucket_list);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bl.setName(blEditText.getText().toString());
                notifyItemChanged(position);
            }
        });
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
            mTextView = (TextView) itemView.findViewById(R.id.bucketList);

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
                    editBucketList(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
