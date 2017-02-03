package edu.rosehulman.punttj.rosebuckets.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.rosehulman.punttj.rosebuckets.adapters.BLSubItemAdapter;
import edu.rosehulman.punttj.rosebuckets.R;
import edu.rosehulman.punttj.rosebuckets.model.SubItem;

/**
 * Created by punttj on 1/22/2017.
 */

public class BucketListSubItemFragment extends Fragment {

    private OnSubItemSelectedListener mListener;
    private BLSubItemAdapter mAdapter;
    private FloatingActionButton mFab;

    public BucketListSubItemFragment() {
        //required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The adapter needs the listener so that when a painting is selected, it can
        // ask the listener (the MainActivity) to switch out the fragment.

        mAdapter = new BLSubItemAdapter(mListener, getContext());

        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.addEditBucketListSubItem(null);
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bucket_list_sub_item, container, false);
        TextView titleText = (TextView) view.findViewById(R.id.subTitle);
        mAdapter.setTitleText(titleText);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.bucket_list_sub_items_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubItemSelectedListener){
            mListener = (OnSubItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                    + " must implement OnSubItemSelectedListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSubItemSelectedListener {
        void onSubItemSelected(SubItem subItem);
    }



}
