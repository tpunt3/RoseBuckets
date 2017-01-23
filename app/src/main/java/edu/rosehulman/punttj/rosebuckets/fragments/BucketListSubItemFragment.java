package edu.rosehulman.punttj.rosebuckets.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.punttj.rosebuckets.BLSubItemAdapter;
import edu.rosehulman.punttj.rosebuckets.R;
import edu.rosehulman.punttj.rosebuckets.model.SubItem;

/**
 * Created by punttj on 1/22/2017.
 */

public class BucketListSubItemFragment extends Fragment {

    private OnSubItemSelectedListener mSubItemSelectedListener;

    public BucketListSubItemFragment() {
        //required empty constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_bucket_list_sub_item, container, false);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        //TODO: might need to send it context as well?
        BLSubItemAdapter adapter = new BLSubItemAdapter(mSubItemSelectedListener);
        view.setAdapter(adapter);
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubItemSelectedListener){
            mSubItemSelectedListener = (OnSubItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                    + " must implement OnSubItemSelectedListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        mSubItemSelectedListener = null;
    }

    public interface OnSubItemSelectedListener {
        void onSubItemSelected(SubItem subItem);
    }



}
