package edu.rosehulman.punttj.rosebuckets.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.rosehulman.punttj.rosebuckets.R;
import edu.rosehulman.punttj.rosebuckets.SharedPreferencesUtils;
import edu.rosehulman.punttj.rosebuckets.adapters.BucketListItemAdapter;
import edu.rosehulman.punttj.rosebuckets.model.BucketList;
import edu.rosehulman.punttj.rosebuckets.model.BucketListItem;

/**
 * Created by punttj on 1/22/2017.
 */

public class BucketListItemFragment extends Fragment{
    private OnBLItemSelectedListener mListener;
    private BucketListItemAdapter mAdapter;
    private FloatingActionButton mFab;
    private TextView titleText;

    public BucketListItemFragment(){

    }

    @Override
    public void onResume() {
        super.onResume();

        titleText = (TextView) getView().findViewById(R.id.title_text);

        String uid = SharedPreferencesUtils.getCurrentBucketList(getContext());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("bucketLists/"+uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BucketList list = dataSnapshot.getValue(BucketList.class);
                titleText.setText(list.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("ItemFragment", "onActivityCreated");
        // Need to wait for the activity to be created to have an action bar.
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        int defaultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(defaultColor));

        titleText = (TextView) getActivity().findViewById(R.id.title_text);
        mAdapter.setTitleText(titleText);

        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.addEditBucketListItem(null);
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ItemFragment", "onCreate");
        // The adapter needs the listener so that when a painting is selected, it can
        // ask the listener (the MainActivity) to switch out the fragment.

        mAdapter = new BucketListItemAdapter(mListener, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ItemFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_bucket_list_items, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        mAdapter.setTitleText(titleText);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.bucket_list_items_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BucketListItemFragment.OnBLItemSelectedListener) {
            mListener = (BucketListItemFragment.OnBLItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBLItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBLItemSelectedListener {
        void onBLItemSelected(BucketListItem item);
    }
}
