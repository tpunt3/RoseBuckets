package edu.rosehulman.punttj.rosebuckets.fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.rosehulman.punttj.rosebuckets.SharedPreferencesUtils;
import edu.rosehulman.punttj.rosebuckets.adapters.BucketListAdapter;
import edu.rosehulman.punttj.rosebuckets.R;
import edu.rosehulman.punttj.rosebuckets.model.BucketList;

/**
 * Created by punttj on 1/22/2017.
 */

public class BucketListFragment extends Fragment{

    private OnBLSelectedListener mListener;
    private BucketListAdapter mAdapter;
    private FloatingActionButton mFab;

    public BucketListFragment(){

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Need to wait for the activity to be created to have an action bar.
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));

        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.addEditBucketList(null);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The adapter needs the listener so that when a painting is selected, it can
        // ask the listener (the MainActivity) to switch out the fragment.
        mAdapter = new BucketListAdapter(mListener, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bucket_list, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.bucket_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        TextView title = (TextView)view.findViewById(R.id.welcome);
        title.setText("Welcome " + SharedPreferencesUtils.getCurrentUserName(getContext()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBLSelectedListener) {
            mListener = (OnBLSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBLSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBLSelectedListener {
        void onBLSelected(BucketList bl);
    }
}
