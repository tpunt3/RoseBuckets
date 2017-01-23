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

import edu.rosehulman.punttj.rosebuckets.model.SubItem;

/**
 * Created by alangavr on 1/22/2017.
 */

public class BLSubItemListAdapter extends RecyclerView.Adapter<BLSubItemListAdapter.ViewHolder> {

    private List<SubItem> mSubItems;
    private DatabaseReference mFirebase;

    public BLSubItemListAdapter() {
        mSubItems = new ArrayList<>();
        mFirebase = FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public BLSubItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_sub_item_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCheckBox.setText(mSubItems.get(position).getDescription());
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            mCheckBox = (CheckBox) itemView.findViewById(R.id.SIcheckBox);
        }
    }
}
