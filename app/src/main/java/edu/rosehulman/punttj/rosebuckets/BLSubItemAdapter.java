package edu.rosehulman.punttj.rosebuckets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.punttj.rosebuckets.fragments.BucketListSubItemFragment;
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
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSubItemSelected(current);
            }
        });
    }

    public void addItem(String title) {
        mSubItems.add(new SubItem(title));
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
        }
    }
}
